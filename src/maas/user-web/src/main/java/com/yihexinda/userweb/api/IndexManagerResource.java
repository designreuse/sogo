package com.yihexinda.userweb.api;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yihexinda.core.annotation.NoRequireLogin;
import com.yihexinda.core.constants.RedisConstant;
import com.yihexinda.core.utils.*;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.userweb.client.OrderClient;
import com.yihexinda.userweb.client.RegionClient;
import com.yihexinda.userweb.client.StationClient;
import com.yihexinda.userweb.utils.HanyuPinyinUtil;
import com.yihexinda.userweb.utils.RedisUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 首页接口管理，加载站点信息
 *
 * @author wenbn
 * @version 1.0
 * @date 2018/12/3 0003
 */
@Api(description = "C端首页接口")
@RestController()
@RequestMapping("/api/index")
public class IndexManagerResource {

    @Autowired
    private StationClient stationClient;
    @Autowired
    private RegionClient regionClient;
    @Autowired
    private OrderClient orderClient;
    @Autowired
    private RedisUtil redisUtil;


    /**
     * wenbn
     * 首页加载原来的行程记录
     * @return  判断是否高峰
     */
    @ApiOperation(value = "首页加载原来的行程记录", httpMethod = "POST")
    @RequestMapping(value = "/init" ,method = RequestMethod.POST)
    @NoRequireLogin
    public ResultVo init(@ApiIgnore @RequestBody Map<String,Object> data) throws ParseException {
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
        //当前时间是否为高峰 行程类型( 0 平峰 1高峰 )
        String route_type = "0";
        //高峰
        if (currentTime.after(stratTime) && currentTime.before(endTime)) {
            route_type = "1";
        }
        //2 加载用户最近一次使用的订单
        data.put("userId",RequestUtil.analysisToken(StringUtil.trim(data.get("token"))).getUserId());
        data.put("route_type",route_type);
        ResultVo orderInfo = orderClient.queryRecentOrderOrder(data);
        Map<String,Object> orderMap = (Map<String, Object>) orderInfo.getDataSet();
        orderMap.put("type",route_type);
        return success.setDataSet(orderMap);
    }


    /**
     * 首页加载站点
     *
     * @return
     */
    @PostMapping("/loadStation")
    @ApiOperation(value = "首页加载站点", httpMethod = "POST")
    @NoRequireLogin
    @ApiImplicitParams({
            @ApiImplicitParam(name = "longitude", value = "经度", required = true, paramType = "String"),
            @ApiImplicitParam(name = "latitude", value = "纬度", required = true, paramType = "String"),
            @ApiImplicitParam(name = "type", value = "出行类型 0:实时  1:预约", required = true, paramType = "String"),
            @ApiImplicitParam(name = "token", value = "用户登录标识", paramType = "String")
    })
    public ResultVo loadStation(@RequestBody Map<String,Object> data) throws Exception {
        Map<String,Object> reqData = Maps.newHashMap();
        String stations = redisUtil.get(RedisConstant.SYS_STATIONS_CACHE_KEY);
        List<Map<Object, Object>> stationList = null;
        if (StringUtil.isEmpty(stations)) {
            Map<String, Object> condition = Maps.newHashMap();
            condition.put("station", 1);
            ResultVo resultVo = stationClient.loadStations(condition);
            if (resultVo.getResult() != 0) {
                return resultVo;
            }
            stationList = (List<Map<Object, Object>>) resultVo.getDataSet();
        } else {
            stationList = JsonUtil.parseJSON2List(stations);
        }

        int type = StringUtil.getAsInt(StringUtil.trim(data.get("type")),0);
        if(type == 0){
            //判断是否是高峰
            Date nowTime = new Date();
//            nowTime = DateUtils.parseDate("2018-12-27 20:45:00",DateUtils.PATTERN_yyyy_MM_dd_HH_mm_ss);
            String peakTimeRange = redisUtil.get(RedisConstant.SYS_PEAK_TIME_RANGE);
            SimpleDateFormat date = new SimpleDateFormat("HH:mm");
            String currentTimeRange = date.format(nowTime);
            String[] time = peakTimeRange.split("-");
            //当前时间
            Date currentTime = date.parse(currentTimeRange);
            //高峰开始时间
            Date stratTime = date.parse(time[0]);
            //高峰结束时间
            Date endTime = date.parse(time[1]);
            //高峰
            if (currentTime.after(stratTime) && currentTime.before(endTime)) {
                stationList = filterStation(stationList,1);
                String sys_peak_lines = redisUtil.get("SYS_PEAK_LINES");//高峰对应的线路
                if(!StringUtil.isEmpty(sys_peak_lines)){
                    String viaStationKey = redisUtil.get("SYS_PEAK_VIA_STATIONS_CACHE_KEY");
                    List<Map<Object, Object>> viaStationListMap = JsonUtil.parseJSON2List(viaStationKey);
                    for (int i = stationList.size()-1 ;i>=0 ;i--) {
                        Map<Object, Object> station = stationList.get(i);
                        String stationId = StringUtil.trim(station.get("id"));
                        boolean isValid = true;
                        for (Map<Object, Object> via : viaStationListMap) {
                            String station_id = StringUtil.trim(via.get("station_id"));
                            if(stationId.equals(station_id)){
                                isValid = false;
                                break;
                            }
                        }
                        if(isValid){
                            stationList.remove(i);
                        }
                    }
                }
            }else{
                //只返回平峰站点
                //"type" '0 锚点 1 高峰站点  2 平峰站点';
                stationList = filterStation(stationList,2);
            }
        }else{
            //只返回平峰站点
            //"type" '0 锚点 1 高峰站点  2 平峰站点';
            stationList = filterStation(stationList,2);
        }

        //
        double longitude = StringUtil.getDouble(StringUtil.trim(reqData.get("longitude")), -1);
        double latitude = StringUtil.getDouble(StringUtil.trim(reqData.get("latitude")), -1);
        boolean isCalculate = longitude != -1 & latitude != -1;
        //保存最近的距离
        double nearest = 10000d;
        int index = -1;
        for (int i = stationList.size()-1 ;i>=0 ;i--) {
            Map<Object, Object> station = stationList.get(i);
            //计算站点位置
            if (isCalculate) {
                double distance = MapUtil.GetDistance(
                        longitude,
                        latitude,
                        StringUtil.getDouble(StringUtil.trim(station.get("longitude"))),
                        StringUtil.getDouble(StringUtil.trim(station.get("latitude")))
                );
                if(nearest>distance){
                    nearest = distance ;
                    index = i ;
                }
            }
            station.put("pinyin", HanyuPinyinUtil.getPinyinString(StringUtil.trim(station.get("site_name"))));
        }
        if (isCalculate) {
            Map<Object, Object> stationMap = stationList.get(index);
            stationMap.put("near", 1);
        }
        return ResultVo.success().setDataSet(stationList);

    }

    /**
     * 过滤站点
     * @param stationList
     * @param type
     * @return
     */
    private List<Map<Object, Object>> filterStation(List<Map<Object, Object>> stationList,int type) {
        return stationList.stream().filter(station -> {
            try {
                Double stationtype = StringUtil.getDouble(StringUtil.trim(station.get("type")));
                return stationtype.intValue() == type;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }).collect(Collectors.toList());
    }

    /**
     * 首页加载省市区
     *
     * @return
     */
    @PostMapping("/loadRegions")
    @ApiOperation(value = "首页加载省市区", httpMethod = "POST")
    @NoRequireLogin
    public ResultVo loadRegions() {
        ResultVo vo = ResultVo.success();
        String regions = redisUtil.get(RedisConstant.SYS_RESIONS_CACHE_KEY);
        if (!StringUtil.isEmpty(regions)) {
            List<Map<Object, Object>> regionList = JsonUtil.parseJSON2List(regions);
            vo.setDataSet(regionList);
            return vo;
        }
        return regionClient.regionList();

    }


    /**
     * wenbn
     * 加载下一个站点的数据
     * @return  判断是否高峰
     */
    @ApiOperation(value = "判断是否高峰", httpMethod = "POST")
    @RequestMapping(value = "/check/isPeak" ,method = RequestMethod.POST)
    @NoRequireLogin
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startStationId", value = "开始站点", required = true, paramType = "String"),
    })

    public ResultVo isPeak(@ApiIgnore @RequestBody Map<String,Object> data) throws ParseException {
        //1、检查是否是高峰
        ResultVo success = ResultVo.success();
        Date nowTime = new Date();
//        nowTime = DateUtils.parseDate("2018-12-27 20:45:00",DateUtils.PATTERN_yyyy_MM_dd_HH_mm_ss);
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
        String startStationId = StringUtil.trim(data.get("startStationId"));
        //高峰
        if (currentTime.after(stratTime) && currentTime.before(endTime)) {
            //2判断用户选择的起点，加载高峰线路的终点 //上车的站点id
            String sys_peak_lines = redisUtil.get("SYS_PEAK_LINES");//高峰对应的线路
            if(!StringUtil.isEmpty(sys_peak_lines)){
                String[] lines = sys_peak_lines.split(",");
                data.put("lines", Arrays.asList(lines));
            }
            ResultVo resultVo = stationClient.queryStationsByLines(data);
            List<Map<String,Object>> viaStationList = (List<Map<String, Object>>) resultVo.getDataSet();
            Map<String,Map<String,Object>> filter = Maps.newHashMap();
            List<Map<String,Object>> newStationList = Lists.newArrayList();

            List<String> lineIds = Lists.newArrayList();
            for (int i = viaStationList.size()-1 ;i>=0 ;i--) {
                Map<String, Object> station = viaStationList.get(i);
                String stationId = StringUtil.trim(station.get("station_id"));
                if(stationId.equals(startStationId)){
                    lineIds.add(StringUtil.trim(station.get("line_id")));
                }
            }


            newStationList = viaStationList.stream().filter(newStation ->
                    {
                        if(lineIds.contains(StringUtil.trim(newStation.get("line_id")))){
                            String stationId = StringUtil.trim(newStation.get("station_id"));
                            if(!stationId.equals(startStationId)){
                                Map<String,Object> id = filter.get(stationId);
                                if(id == null ){
                                    newStation.put("pinyin", HanyuPinyinUtil.getPinyinString(StringUtil.trim(newStation.get("site_name"))));
                                    newStation.put("id",StringUtil.trim(newStation.get("station_id")));
                                    filter.put(stationId,newStation);
                                    return true;
                                }
                            }
                        }
                        return false;
                    }
            ).collect(Collectors.toList());



//            viaStationList = viaStationList.stream().filter(newStation ->lineIds.contains(StringUtil.trim(newStation.get("line_id")))).collect(Collectors.toList());
//            for (int i = viaStationList.size()-1 ;i>=0 ;i--) {
//                Map<String, Object> station = viaStationList.get(i);
//                String stationId = StringUtil.trim(station.get("station_id"));
//                if(!stationId.equals(startStationId)){
//                    Map<String,Object> id = filter.get(stationId);
//                    if(id == null ){
//                        station.put("pinyin", HanyuPinyinUtil.getPinyinString(StringUtil.trim(station.get("site_name"))));
//                        filter.put(stationId,station);
//                        newStationList.add(station);
//                    }
//                }
//            }

            return resultVo.setDataSet(newStationList);
        }
        return success;
    }


    /**
     * 首页加载预约时间
     *
     * @return
     */
    @PostMapping("/loadSysAppoTime")
    @ApiOperation(value = "首页加载预约时间", httpMethod = "POST")
    @NoRequireLogin
    public ResultVo loadAppoTime() {
        ResultVo vo = ResultVo.success();
        String sysAppoTime = redisUtil.get(RedisConstant.SYS_APPOINTMENT_TIME_KEY);
        List<String> list = null;
        if (!StringUtil.isEmpty(sysAppoTime)) {
            list = JsonUtil.json2List(sysAppoTime, String.class);
        } else {
            try {
                list = DateUtils.getIntervalTimeList("20:00:00", "22:00:00", 10);
            } catch (ParseException e) {
                e.printStackTrace();
                return ResultVo.error(ResultVo.Status.SYS_REQUIRED_FAILURE);
            }
            if (list != null && list.size() > 0) {
                redisUtil.set(RedisConstant.SYS_APPOINTMENT_TIME_KEY, JsonUtil.list2Json(list), (long) 60 * 60 * 24 * 30);
            }
        }
        vo.setDataSet(list);
        return vo;
    }


}
