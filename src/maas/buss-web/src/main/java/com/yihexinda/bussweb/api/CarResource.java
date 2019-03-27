package com.yihexinda.bussweb.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yihexinda.bussweb.client.CarClient;
import com.yihexinda.bussweb.utils.RedisUtil;
import com.yihexinda.core.annotation.NoRequireLogin;
import com.yihexinda.core.constants.RedisConstant;
import com.yihexinda.core.dto.Json;
import com.yihexinda.core.utils.JsonUtil;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TCarDto;
import com.yihexinda.data.dto.TCarPositionDto;
import com.yihexinda.data.dto.TLineDriverBindDto;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @author wenbn
 * @version 1.0
 * @date 2018/11/29 0029
 */
@Api(description = "车辆管理接口")
@RestController
@RequestMapping("/api/buss")
@Slf4j
public class CarResource {

    @Autowired
    private CarClient carClient;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * wenbn
     * 查询车辆
     * @param
     * @return
     */
    @ApiOperation(value = "查询车辆", httpMethod = "GET")
    @RequestMapping(value = "/carList" ,method = RequestMethod.GET)
    public ResultVo carList() {

        return carClient.carList();
    }


    /**
     * emmet
     * 发送车辆实时位置
     * @param condition
     * @return
     */
    @ApiOperation(value = "发送车辆实时位置", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "carId", value = "车辆id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "longitude", value = "经度", required = true, dataType = "String"),
            @ApiImplicitParam(name = "latitude", value = "纬度", required = true, dataType = "String")
    })
    @PostMapping(value = "addCarPosition")
    @NoRequireLogin
    public ResultVo sendCarPosition(@ApiIgnore @RequestBody Map<String,Object> condition) throws Exception {
        String carId = StringUtil.trim(condition.get("carId"));
        String longitude = StringUtil.trim(condition.get("longitude"));
        String latitude = StringUtil.trim(condition.get("latitude"));
        if(StringUtil.isEmpty(carId) || StringUtil.isEmpty(longitude) || StringUtil.isEmpty(latitude)){
            return ResultVo.error(ResultVo.Status.REQUIRED_PARAMETER_ERROR);
        }
        log.info("接收到车辆定位 carId:"+carId+"longitude:"+longitude+"latitude:"+latitude);
        TCarPositionDto carPosition = new TCarPositionDto();
        carPosition.setCarId(carId);
        carPosition.setLongitude(StringUtil.getDouble(longitude));
        carPosition.setLatitude(StringUtil.getDouble(latitude));
        carPosition.setCreateDate(new Date());
        //判断车辆是否到站
        carClient.carGetDown(carPosition);
        //carClient.updateOrderStatus();
        return carClient.addCarPosition(carPosition);
    }

    /**
     * wenbn
     * 司机确认到站
     * @param condition
     * @return
     */
    @ApiOperation(value = "司机确认到站", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "carId", value = "车辆id", required = true, dataType = "String")
    })
    @PostMapping(value = "confirmArrive")
    @NoRequireLogin
    public ResultVo confirmArrive(@ApiIgnore @RequestBody Map<String,Object> condition) throws Exception {
        String carId = StringUtil.trim(condition.get("carId"));
        if(StringUtil.isEmpty(carId)){
            return ResultVo.error(ResultVo.Status.REQUIRED_PARAMETER_ERROR);
        }
        log.info("司机确认到站 carId:"+carId);
        //司机确认到站
        return carClient.confirmArrive(condition);
    }


    /**
     * pengfeng
     * 查询未绑定车辆列表
     * @return  未绑定车辆列表
     */
    @ApiOperation(value = "查询未绑定车辆列表", httpMethod = "GET")
    @RequestMapping(value = "/getCarList" ,method = RequestMethod.GET)
    @NoRequireLogin
    public ResultVo getCarList() {
        return carClient.getCarList();
    }



    /**
     * wenbn
     * 判断是否高峰
     * @return  判断是否高峰
     */
    @ApiOperation(value = "判断是否高峰", httpMethod = "POST")
    @RequestMapping(value = "/isPeak" ,method = RequestMethod.POST)
    @NoRequireLogin
    public ResultVo isPeak() throws ParseException {
        ResultVo success = ResultVo.success();
        Date nowTime = new Date();
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
        if (currentTime.after(stratTime) && currentTime.before(endTime)) {
            //高峰
            success.setDataSet(0);
        }else{
            //平峰
            success.setDataSet(1);
        }
        return success;
    }


    /**
     * wenbn
     * 平峰实时导航
     * @return  平峰实时导航
     */
    @ApiOperation(value = "平峰实时导航", httpMethod = "POST")
    @RequestMapping(value = "/navigation" ,method = RequestMethod.POST)
    @NoRequireLogin
    @ApiImplicitParams({
            @ApiImplicitParam(name = "carId", value = "车辆id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String")
    })
    public ResultVo navigation(@RequestBody Map<String,Object> condition){
        String carId = StringUtil.trim(condition.get("carId"));
        if(StringUtil.isEmpty(carId)){
            return ResultVo.error(ResultVo.Status.REQUIRED_PARAMETER_ERROR);
        }
        return carClient.navigation(condition);
    }


    /**
     * wenbn
     * 高峰实时导航
     * @return  高峰实时导航
     */
    @ApiOperation(value = "高峰实时导航", httpMethod = "POST")
    @RequestMapping(value = "/real/navigation" ,method = RequestMethod.POST)
    @NoRequireLogin
    @ApiImplicitParams({
            @ApiImplicitParam(name = "carId", value = "车辆id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String")
    })
    public ResultVo realNavigation(@RequestBody Map<String,Object> condition){
//        ResultVo success = ResultVo.success();
        String carId = StringUtil.trim(condition.get("carId"));
        //根据线路ids查询司机信息
//        if(StringUtil.isEmpty(carId)){
//            return ResultVo.error(ResultVo.Status.REQUIRED_PARAMETER_ERROR);
//        }
//        String maas_peak = redisUtil.get("maas_peak_station" + carId);
//        if(!StringUtil.isEmpty(maas_peak)){
//            List<Map<Object, Object>> maps = JsonUtil.parseJSON2List(maas_peak);
//            success.setDataSet(maps);
//        }
        return carClient.realNavigation(condition);
    }

}
