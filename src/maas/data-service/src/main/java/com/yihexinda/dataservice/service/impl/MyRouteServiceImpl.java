package com.yihexinda.dataservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.zxing.common.StringUtils;
import com.yihexinda.core.abst.AbstractPageTemplate;
import com.yihexinda.core.constants.Constants;
import com.yihexinda.core.constants.RedisConstant;
import com.yihexinda.core.constants.ResultConstant;
import com.yihexinda.core.utils.DateUtils;
import com.yihexinda.core.utils.JsonUtil;
import com.yihexinda.core.utils.RequestUtil;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.*;
import com.yihexinda.dataservice.dao.*;
import com.yihexinda.dataservice.service.MyRouteService;
import com.yihexinda.dataservice.service.TDriverCarBindService;
import com.yihexinda.dataservice.service.TRouteOffpeakService;
import com.yihexinda.dataservice.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.management.ServiceNotFoundException;
import javax.naming.ServiceUnavailableException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author chenzeqi
 * @date 2018/12/24
 */
@Service
public class MyRouteServiceImpl implements MyRouteService {

    @Autowired
    private MyRouteDao myRouteDao;

    @Autowired
    private TRouteOffpeakDao routeOffPeakDao;

    @Autowired
    private TRoutePeakDao routePeakDao;

    @Autowired
    private TCarDao carDao;

    @Autowired
    private TRouteStationDao routeStationDao;

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
    @Override
    public ResultVo getMyRouteList(@RequestBody Map<String,Object> condition) {
        int pageIndex = StringUtil.getAsInt(StringUtil.trim(condition.get("pageIndex")), 1);
        int pageSize = StringUtil.getAsInt(StringUtil.trim(condition.get("pageSize")), 10);
        ResultVo resultVo = new ResultVo();
        List<MyRouteDto> myRouteDtoList = null;
        try{
            //司机id
            String driverId = StringUtil.trim(condition.get("driverId"));
            if(driverId == null){
                throw new ServiceNotFoundException("deviceId is null");
            }
            // 1请求平峰列表 2 请求高峰列表
            //行程类型( 0 平峰 1高峰 )
            String type = StringUtil.trim(condition.get("type"),"0");
            Date date = new Date();
            if("0".equals(type)){
                resultVo = new AbstractPageTemplate<Map<String, Object>>() {
                    @Override
                    protected List<Map<String, Object>> executeSql() {
                        List<Map<String, Object>> routeOffPeakListMap = routeOffPeakDao.selectMaps(
                            new QueryWrapper<TRouteOffpeakDto>()
                                .select("id","driver_user_id","create_date","start_date", "end_date", "car_id","midway_station")
                                .eq("route_state", "1")
                                .eq("driver_user_id",driverId)
                                .orderByDesc("create_date")
                        );
                        return routeOffPeakListMap;
                    }
                }.preparePageTemplate(pageIndex, pageSize);

                List<Map<String, Object>> routeOffPeakListMap = (List<Map<String, Object>>) resultVo.getDataSet();
                if(routeOffPeakListMap == null || routeOffPeakListMap.size()==0){
                    return resultVo;
                }

                //查询所有站点
                String stationKey = redisUtil.get(RedisConstant.SYS_STATIONS_CACHE_KEY);
                List<Map<Object, Object>> stationListMap = JsonUtil.parseJSON2List(stationKey);
                List<String> carIds = Lists.newArrayList();
                List<String> routeIds = Lists.newArrayList();

                for (Map<String, Object> routeOffPeakMap : routeOffPeakListMap) {
                    String midway_station = StringUtil.trim(routeOffPeakMap.get("midway_station"));
                    String car_id = StringUtil.trim(routeOffPeakMap.get("car_id"));
                    String id = StringUtil.trim(routeOffPeakMap.get("id"));
                    if(!carIds.contains(car_id)){
                        carIds.add(car_id);
                    }
                    if(!routeIds.contains(id)){
                        routeIds.add(id);
                    }
                    if(StringUtil.isEmpty(midway_station)){
                        continue;
                    }
                    StringBuffer buf = new StringBuffer();
                    String[] split = midway_station.split(",");
                    for (String s : split) {
                        for (Map<Object, Object> station : stationListMap) {
                            if(s.equals(StringUtil.trim(station.get("id")))){
                                buf.append(StringUtil.trim(station.get("site_name")));
                                buf.append(",");
                                break;
                            }
                        }
                    }
                    routeOffPeakMap.put("station_name",buf.substring(0,buf.length()-1));
                }

                //查询车辆信息
                List<Map<String, Object>> carListMap = carDao.selectMaps(new QueryWrapper<TCarDto>()
                        .select("id", "pass_nums", "car_no")
                        .in("id",carIds)
                );

                //统计上下车
                List<Map<String, Object>> routeStatisticsListMap = routeStationDao.selectMaps(new QueryWrapper<TRouteStationDto>()
                        .select("route_id", "sum(on_num) total_num ", "sum(pre_station_mile) total_mile")
                        .in("route_id",routeIds)
                        .groupBy("route_id")
                );


                for (Map<String, Object> routePeak : routeOffPeakListMap) {
                    String car_id = StringUtil.trim(routePeak.get("car_id"));
                    for (Map<String, Object> car : carListMap) {
                        String id = StringUtil.trim(car.get("id"));
                        if(car_id.equals(id)){
                            routePeak.put("pass_nums",car.get("pass_nums"));
                            routePeak.put("car_no",car.get("car_no"));
                            break;
                        }
                    }
                    String id = StringUtil.trim(routePeak.get("id"));
                    for (Map<String, Object> routeStationMap : routeStatisticsListMap) {
                        String route_id = StringUtil.trim(routeStationMap.get("route_id"));
                        if(id.equals(route_id)){
                            routeStationMap.remove("route_id");
                            routePeak.putAll(routeStationMap);
                            break;
                        }
                    }
                    Date start_date = (Date) routePeak.get("start_date");
                    Date end_date = (Date) routePeak.get("end_date");
                    if(start_date != null && end_date!= null){
                        routePeak.put("total_time",(end_date.getTime()-start_date.getTime())/60000);
                    }
                }

//                SimpleDateFormat sbj = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
//                String format = sbj.format(date);
//                Date dataFormat = sbj.parse(format);
//                myRouteDtoList = myRouteDao.getMyRouteOffpeakList(driverId,dataFormat);
//                //计算总时长
//                if(myRouteDtoList.size()>0){
//                    for(int i = 0;i < myRouteDtoList.size(); i++){
//                        long time = myRouteDtoList.get(i).getEndTime().getTime() -  myRouteDtoList.get(i).getCreateDate().getTime();
//                        myRouteDtoList.get(i).setTotalTime((int) (time / 1000 / 60));
//                    }
//                    resultVo.setDataSet(myRouteDtoList);
//                }
            }else if("1".equals(type)){
                 resultVo = new AbstractPageTemplate<Map<String, Object>>() {
                        @Override
                        protected List<Map<String, Object>> executeSql() {
                            List<Map<String, Object>> routePeakListMap = routePeakDao.selectMaps(
                                    new QueryWrapper<TRoutePeakDto>()
                                            .select("id", "line_id", "driver_user_id", "create_date","start_date", "end_date", "car_id")
                                            .eq("route_state", "1")
                                            .eq("driver_user_id",driverId)
                                            .orderByDesc("create_date")
                            );
                            return routePeakListMap;
                        }
                 }.preparePageTemplate(pageIndex, pageSize);

                List<Map<String, Object>> routePeakListMap = (List<Map<String, Object>>) resultVo.getDataSet();
                if(routePeakListMap == null || routePeakListMap.size()==0){
                    return resultVo;
                }
                //保存线路ids
                //查询高峰线路站点
                String viaStationKey = redisUtil.get(RedisConstant.SYS_PEAK_VIA_STATIONS_CACHE_KEY);
                List<Map<Object, Object>> viaStationListMap = JsonUtil.parseJSON2List(viaStationKey);

                //查询所有站点
                String stationKey = redisUtil.get(RedisConstant.SYS_STATIONS_CACHE_KEY);
                List<Map<Object, Object>> stationListMap = JsonUtil.parseJSON2List(stationKey);

                List<String> lineIds = Lists.newArrayList();
                List<String> carIds = Lists.newArrayList();
                List<String> routeIds = Lists.newArrayList();
                Map<String,Object> line2StationMap = Maps.newHashMap();

                for (Map<String, Object> routePeak : routePeakListMap) {
                    String line_id = StringUtil.trim(routePeak.get("line_id"));
                    String car_id = StringUtil.trim(routePeak.get("car_id"));
                    String id = StringUtil.trim(routePeak.get("id"));
                    //保存车辆信息
                    if(!routeIds.contains(id)){
                        routeIds.add(id);
                    }
                    if(!carIds.contains(car_id)){
                        carIds.add(car_id);
                    }
                    if(!lineIds.contains(line_id)){
                        lineIds.add(line_id);
                        line2StationMap.put(line_id,null);
                    }
                }

                //查询车辆信息
                List<Map<String, Object>> carListMap = carDao.selectMaps(new QueryWrapper<TCarDto>()
                        .select("id", "pass_nums", "car_no")
                        .in("id",carIds)
                );

                //统计上下车
                List<Map<String, Object>> routeStationListMap = routeStationDao.selectMaps(new QueryWrapper<TRouteStationDto>()
                        .select("route_id", "sum(on_num) total_num ", "sum(pre_station_mile) total_mile")
                        .in("route_id",routeIds)
                        .groupBy("route_id")
                );

                Iterator<Map.Entry<String, Object>> iterator = line2StationMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map<String,Object> result = Maps.newHashMap();
                    Map.Entry<String, Object> entry = iterator.next();
                    //保存站点信息
                    List<String> stationList = Lists.newArrayList();
                    List<Map<Object,Object>> lineStations = new ArrayList<>();
                    String key = StringUtil.trim(entry.getKey());
                    for (int i = viaStationListMap.size() - 1 ;i>= 0 ;i--) {
                        Map<Object, Object> map= viaStationListMap.get(i);
                        Double stationType = StringUtil.getDouble(StringUtil.trim(map.get("type")));
                        if(StringUtil.trim(map.get("line_id")).equals(key) && stationType.intValue() == 1){
                            lineStations.add(map);
                            viaStationListMap.remove(map);
                        }
                    }
                    lineStations.sort((a,b) -> StringUtil.getAsInt(StringUtil.trim(a.get("index"))) - StringUtil.getAsInt(StringUtil.trim(b.get("index"))));
                    lineStations.forEach(station -> stationList.add(StringUtil.trim(station.get("station_id"))));
                    //保存站点名称
                    StringBuilder buf = new StringBuilder();
                    for (String s : stationList) {
                        for (Map<Object, Object> station : stationListMap) {
                            if(s.equals(StringUtil.trim(station.get("id")))){
                                buf.append(StringUtil.trim(station.get("site_name")));
                                buf.append(",");
                                break;
                            }
                        }
                    }
                    if(buf!= null && buf.length()>0){
                        result.put("station_name",buf.substring(0,buf.length()-1));
                    }else{
                        result.put("station_name",null);
                    }
                    entry.setValue(result);
                }

                for (Map<String, Object> routePeak : routePeakListMap) {
                    String car_id = StringUtil.trim(routePeak.get("car_id"));
                    for (Map<String, Object> car : carListMap) {
                        String id = StringUtil.trim(car.get("id"));
                        if(car_id.equals(id)){
                            routePeak.put("pass_nums",car.get("pass_nums"));
                            routePeak.put("car_no",car.get("car_no"));
                            break;
                        }
                    }
                    String line_id = StringUtil.trim(routePeak.get("line_id"));
                    Map<String,Object> result = (Map<String, Object>) line2StationMap.get(line_id);
                    routePeak.putAll(result);

                    String id = StringUtil.trim(routePeak.get("id"));
                    for (Map<String, Object> routeStationMap : routeStationListMap) {
                        String route_id = StringUtil.trim(routeStationMap.get("route_id"));
                        if(id.equals(route_id)){
                            routeStationMap.remove("route_id");
                            routePeak.putAll(routeStationMap);
                            break;
                        }
                    }

                    Date start_date = (Date) routePeak.get("start_date");
                    Date end_date = (Date) routePeak.get("end_date");
                    if(start_date != null && end_date!= null){
                        routePeak.put("total_time",(end_date.getTime()-start_date.getTime())/60000);
                    }
                }
//                myRouteDtoList = myRouteDao.getMyRoutePeakList(driverId,dataFormat);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return resultVo;
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
    @Override
    public ResultVo getMyRouteDetails(Map<String, Object> condition) {
        ResultVo vo = ResultVo.success();
        try {
            //行程id
            String routeId = StringUtil.trim(condition.get("routeId"));
            // 0请求平峰列表 1 请求高峰列表
            String type = StringUtil.trim(condition.get("type"),"0");
            if(StringUtil.isEmpty(routeId)){
                return ResultVo.error(ResultVo.Status.REQUIRED_PARAMETER_ERROR);
            }
            if("0".equals(type)){
                TRouteOffpeakDto routeOffpeakDto = routeOffPeakDao.selectById(routeId);
                if(routeOffpeakDto == null){
                   return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE,"行程不存在");
                }
            }else if("1".equals(type)){
                TRoutePeakDto routePeakDto = routePeakDao.selectById(routeId);
                if(routePeakDto == null){
                    return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE,"行程不存在");
                }
            }

            //统计上下车
            List<Map<String, Object>> routeStatisticsListMap = routeStationDao.selectMaps(new QueryWrapper<TRouteStationDto>()
                .select("route_id", "station_id", "off_num","on_num","create_date")
                .eq("route_id",routeId)
            );

            //查询所有站点
            String stationKey = redisUtil.get(RedisConstant.SYS_STATIONS_CACHE_KEY);
            List<Map<Object, Object>> stationListMap = JsonUtil.parseJSON2List(stationKey);

            for (Map<String, Object> routeStatisticsMap : routeStatisticsListMap) {
                String station_id = StringUtil.trim(routeStatisticsMap.get("station_id"));
                for (Map<Object, Object> station : stationListMap) {
                    if(station_id.equals(StringUtil.trim(station.get("id")))){
                        routeStatisticsMap.put("station_name",StringUtil.trim(station.get("site_name")));
                        break;
                    }
                }
            }
            vo.setDataSet(routeStatisticsListMap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return vo;
    }

    /**
     * 查询是否有行程
     *
     * @param driverId 司机id
     * @return ResultVo
     */
    @Override
    public Map getRouteInfo(String driverId) {
        return myRouteDao.getRouteInfo(driverId);
    }
}
