package com.yihexinda.dataservice.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Maps;
import com.yihexinda.core.constants.RedisConstant;
import com.yihexinda.core.constants.ResultConstant;
import com.yihexinda.core.utils.DateUtils;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.*;
import com.yihexinda.dataservice.service.*;
import com.yihexinda.dataservice.utils.RedisUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author chenzeqi
 * @date 2018/12/24
 */
@RestController
@RequestMapping("/myRoute/client")
@Slf4j
public class MyRouteApiResource {


    @Autowired
    private MyRouteService myRouteService;
    @Autowired
    private TRoutePeakService routePeakService;
    @Autowired
    private TRouteOffpeakService routeOffpeakService;
    @Autowired
    private TLineDriverBindService lineDriverBindService;
    @Autowired
    private TLineService lineService;
    @Autowired
    private TViaService viaService;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * @author chenzeqi
     * 查询我的行程列表
     * @type 1请求平峰列表 2 请求高峰列表
     * @deviceId 司机id
     * @param
     * @return
     */
    @PostMapping(value = "/getMyRouteList")
    public ResultVo getMyRouteList(@RequestBody Map<String,Object> condition) {
        return myRouteService.getMyRouteList(condition);
    }


    /**
     * @author chenzeqi
     * 查询我的行程详情
     * @type 1请求平峰列表 2 请求高峰列表
     * @deviceId 司机id
     * @routeId  行程id
     * @param
     * @return
     */
    @PostMapping(value = "/getMyRouteDetails")
    public ResultVo getMyRouteDetails(@RequestBody Map<String,Object> condition) {
        return myRouteService.getMyRouteDetails(condition);
    }


    /**
     * 查询是否有进行中的行程
     * @param data  司机id
     * @return  ResultVo
     */
//    @PostMapping(value = "/getRouteInfo/{driverId}")
//    public ResultVo getRouteInfo(@RequestBody Map<String,Object> data) throws ParseException {
    @PostMapping(value = "/getRouteInfo")
    public ResultVo getRouteInfo(@RequestBody Map<String,Object> data) throws ParseException {
        //司机id
        String driverId = StringUtil.trim(data.get("driverId"));
        //type 1检查是否有行程 2 可以派单
        String type = StringUtil.trim(data.get("type"));
        if (StringUtil.isEmpty(driverId)){
            return  ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE,"司机id为空");
        }
        //1、检查是否是高峰
        ResultVo success = ResultVo.success();
        Date nowTime = new Date();
//        nowTime = DateUtils.parseDate("2018-12-27 07:45:00",DateUtils.PATTERN_yyyy_MM_dd_HH_mm_ss);
        String peakTimeRange = redisUtil.get(RedisConstant.SYS_PEAK_TIME_RANGE);
        SimpleDateFormat date = new SimpleDateFormat("HH:mm");
        String currentTimeRange = date.format(nowTime);
        String[] time = peakTimeRange.split("-");
        //当前时间
        Date currentTime = date.parse(currentTimeRange);
        //7:30高峰开始时间
        Date stratTime = date.parse(time[0]);
        //9:30高峰结束时间
        Date endTime = date.parse(time[1]);
        //高峰
        Map<String,Object> resultMap = Maps.newHashMap();
        if (currentTime.after(stratTime) && currentTime.before(endTime)) {
            //查询高峰是否有进行中的订单
            Map<String, Object> routePeakMap = routePeakService.getMap(new QueryWrapper<TRoutePeakDto>()
                    .eq("driver_user_id", driverId)
                    .eq("route_state", "0")
            );
            if(routePeakMap == null){
                if(type.equals("1")){
                    return ResultVo.error(1,"暂无高峰行程");
                }
                //添加新的行程
                //根据司机信息查询线路信息
                Map<String, Object> lineDriverBindMap = lineDriverBindService.getMap(new QueryWrapper<TLineDriverBindDto>()
                        .select("driver_id","line_id")
                        .eq("driver_id", driverId)
                );
                if(lineDriverBindMap == null){
                    return ResultVo.error(-1,"该司机没有绑定高峰线路");
                }
                String lineId = StringUtil.trim(lineDriverBindMap.get("line_id"));
                if(StringUtil.isEmpty(lineId)){
                    return ResultVo.error(-1,"该司机没有绑定高峰线路");
                }

                //查询线路是否启用
                TLineDto byId = lineService.getOne(new QueryWrapper<TLineDto>()
                    .eq("line_state","1")
                    .eq("id",lineId)
                );
                if(byId == null){
                    log.info("线路 :"+lineId +" 已停用");
                    return ResultVo.error(-1,"该司机没有绑定高峰线路");
                }


                //查询高峰线路的途经站点表
//                List<Map<String, Object>> lineViaList = viaService.listMaps(new QueryWrapper<TViaDto>()
//                        .select("id via_id","line_id","index","station_id","type")
//                        .eq("line_id", lineId)
//                );
//
//                lineViaList.sort((a,b) -> StringUtil.getAsInt(StringUtil.trim(a.get("index"))) - StringUtil.getAsInt(StringUtil.trim(b.get("index"))));

                String keyDate = DateUtils.formatDate(nowTime, DateUtils.PATTERN_yyyy_MM_dd);
                //将车辆与司机绑定的信息缓存
                String carId = redisUtil.get(RedisConstant.SYS_TODAY_CAR_DRIVER_BIND_VALUE + keyDate + driverId);

               synchronized (this){
                   Map<String, Object> queryMap = routePeakService.getMap(new QueryWrapper<TRoutePeakDto>()
                           .eq("driver_user_id", driverId)
                           .eq("route_state", "0")
                   );
                   if(queryMap == null){
                       TRoutePeakDto routePeakDto = new TRoutePeakDto();
                       routePeakDto.setLineId(lineId);
                       routePeakDto.setDriverUserId(driverId);
                       routePeakDto.setCarId(carId);
                       routePeakDto.setCreateDate(nowTime);
                       routePeakDto.setStartDate(nowTime);
                       routePeakDto.setRouteState("0");
//                       routePeakDto.setLineStartId(StringUtil.trim(lineViaList.get(0).get("station_id")));
//                       routePeakDto.setLineEndId(StringUtil.trim(lineViaList.get(lineViaList.size()-1).get("station_id")));
                       routePeakDto.setLineStartId(byId.getLineStartId());
                       routePeakDto.setLineEndId(byId.getLineEndId());
                       boolean save = routePeakService.save(routePeakDto);
                       //type 0 高峰 1平峰
                       if(save){
                           resultMap.put("id",routePeakDto.getId());
                           resultMap.put("type","0");
                           success.setDataSet(resultMap);
                           return success;
                       }
                   }
                   resultMap.put("id",routePeakMap.get("id"));
                   resultMap.put("type","0");
                   success.setDataSet(resultMap);
                   return success;
               }
            }
            resultMap.put("id",routePeakMap.get("id"));
            resultMap.put("type","0");
            success.setDataSet(resultMap);
            return success;
        }else{
            //查询平峰是否有进行中的订单
            Map<String, Object> routeOffPeakMap = routeOffpeakService.getMap(new QueryWrapper<TRouteOffpeakDto>()
                    .eq("driver_user_id", driverId)
                    .eq("route_state", "0")
            );
            if(routeOffPeakMap != null){
                resultMap.put("id",routeOffPeakMap.get("id"));
                resultMap.put("type","1");
                success.setDataSet(resultMap);
                return success;
            }
        }
        return success;
    }



    /**
     * 结束行程
     * @type 0 高峰 1平峰
     * @routeId 行程ID
     * @param
     * @return
     */
    @PostMapping(value = "/finishRoute")
    ResultVo finishRoute(@RequestBody Map<String, Object> data){
        String routeId = StringUtil.trim(data.get("routeId"));
        String type = StringUtil.trim(data.get("type"));
        if(StringUtil.isEmpty(routeId) || StringUtil.isEmpty(type)){
            return ResultVo.error(ResultVo.Status.REQUIRED_PARAMETER_ERROR);
        }
        Date date = new Date();
        //type 0 高峰 1平峰
        if(type.equals("0")){
            TRoutePeakDto query = routePeakService.getById(routeId);
            if(query== null){
                return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE,"高峰行程不存在");
            }
            if(query.getRouteState().equals("1"))
                return ResultVo.success();
            TRoutePeakDto update = new TRoutePeakDto();
            update.setId(routeId);
            update.setRouteState("1");
            update.setEndDate(date);
            boolean b = routePeakService.updateById(update);
            if(!b){
                return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE,"修改失败");
            }
        }else if(type.equals("1")){
            TRouteOffpeakDto query = routeOffpeakService.getById(routeId);
            if(query== null){
                return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE,"平峰行程不存在");
            }
            if(query.getRouteState().equals("1"))
                return ResultVo.success();
            TRouteOffpeakDto update = new TRouteOffpeakDto();
            update.setId(routeId);
            update.setUpdateDate(date);
            update.setEndDate(date);
            update.setRouteState("1");
            boolean b = routeOffpeakService.updateById(update);
            if(!b){
                return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE,"修改失败");
            }
        }else{
            return ResultVo.error(ResultVo.Status.REQUIRED_PARAMETER_ERROR);
        }
        return ResultVo.success();
    }


    /**
     * 检查行程是否结束
     * @type type 0 高峰 1平峰
     * @routeId 行程ID
     * @param
     * @return
     */
    @PostMapping(value = "/checkFinish")
    public ResultVo checkFinish(@RequestBody Map<String,Object> data) {
        String routeId = StringUtil.trim(data.get("routeId"));
        String type = StringUtil.trim(data.get("type"));
        if(StringUtil.isEmpty(routeId) || StringUtil.isEmpty(type)){
            return ResultVo.error(ResultVo.Status.REQUIRED_PARAMETER_ERROR);
        }
        Date date = new Date();
        //type 0 高峰 1平峰
        if(type.equals("0")){
            TRoutePeakDto query = routePeakService.getById(routeId);
            if(query== null){
                return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE,"高峰行程不存在");
            }
            if(query.getRouteState().equals("1")) {
                //已完成
                return ResultVo.success().setMessage("行程已完成");
            }else{
                return ResultVo.error(1,"行程未完成");
            }
        }else if(type.equals("1")){
            TRouteOffpeakDto query = routeOffpeakService.getById(routeId);
            if(query== null){
                return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE,"平峰行程不存在");
            }
            if(query.getRouteState().equals("1")) {
                //已完成
                return ResultVo.success().setMessage("行程已完成");
            }else{
                return ResultVo.error(1,"行程未完成");
            }
        }else{
            return ResultVo.error(ResultVo.Status.REQUIRED_PARAMETER_ERROR);
        }
    }

}
