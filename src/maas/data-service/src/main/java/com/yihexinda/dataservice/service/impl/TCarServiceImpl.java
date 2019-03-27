package com.yihexinda.dataservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yihexinda.core.constants.Constants;
import com.yihexinda.core.constants.MessageConstant;
import com.yihexinda.core.constants.RedisConstant;
import com.yihexinda.core.constants.SysParamConstant;
import com.yihexinda.core.utils.*;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.*;
import com.yihexinda.dataservice.dao.TCarDao;
import com.yihexinda.dataservice.json.JSONUtil;
import com.yihexinda.dataservice.service.*;
import com.yihexinda.dataservice.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * <p>
 * 车辆信息表 服务实现类
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
@Service
@Slf4j
public class TCarServiceImpl extends ServiceImpl<TCarDao, TCarDto> implements TCarService {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

    private static List<String> carIds = Lists.newArrayList();
    private static Map<String,Object> carClients = new ConcurrentHashMap<String, Object>();
    private static Map<String,Object> carTimes = new ConcurrentHashMap<String, Object>();


    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    /**
     * 线路服务
     */
    @Resource
    private TLineService tLineService;
    /**
     * 高峰线路服务
     */
    @Resource
    private TRoutePeakService tRoutePeakService;
    /**
     * 平峰线路服务
     */
    @Resource
    private TRouteOffpeakService tRouteOffpeakService;
    /**
     * 站点服务
     */
    @Resource
    private TStationService tStationService;
    /**
     * 站点统计服务
     */
    @Resource
    private TRouteStationService tRouteStationService;
    /**
     * 订单服务
     */
    @Resource
    private TOrderService tOrderService;
    @Resource
    private TLineDriverBindService rLineDriverBindService;
    @Autowired
    private TLineDriverBindService lineDriverBindService;
    @Autowired
    private TDriverCarBindService driverCarBindService;
    @Autowired
    private TDriverService driverService;
    @Autowired
    private TCarPositionService carPositionService;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 查询车辆是否到站
     *
     * @param carPosition 车辆实时位置
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean carGetDownCopy(TCarPositionDto carPosition) {
        Date date = new Date();
        //根据车辆查询线路
        String carId = carPosition.getCarId();

        //检查车辆是否有高峰信息
        String toDate = DateUtils.formatDate(new Date(), DateUtils.PATTERN_yyyy_MM_dd);
        String driverId = redisUtil.get(RedisConstant.SYS_TODAY_CAR_DRIVER_BIND_VALUE + toDate + carId);
        TLineDriverBindDto one1 = rLineDriverBindService.getOne(new QueryWrapper<TLineDriverBindDto>()
                .eq("driver_id", driverId)
        );
        if(one1!= null){
            //如果有未完成的行程
            TRoutePeakDto routePeakDto = tRoutePeakService.getOne(new QueryWrapper<TRoutePeakDto>()
                    .eq("driver_user_id", driverId)
                    .eq("route_state","0")
            );
            if(routePeakDto!= null){
                String lineId = one1.getLineId();
                String routeId = routePeakDto.getId();
                //查出线路所有站点信息
                String lineStationkey = redisUtil.get("maas_peak_line_station" + lineId);
                List<Map<Object, Object>> lineMaps = JsonUtil.parseJSON2List(lineStationkey);
                lineMaps = lineMaps.stream().filter(line ->
                        {
                            try {
                                Double type = StringUtil.getDouble(StringUtil.trim(line.get("type")));
                                return type.intValue() == 1;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return false;
                        }
                ).collect(Collectors.toList());

                HashMap<Object, Object> objectObjectHashMap = Maps.newHashMap();
                objectObjectHashMap.putAll(lineMaps.get(0));
                objectObjectHashMap.put("station_id",StringUtil.getAsInt(StringUtil.trim(lineMaps.get(0).get("station_id")))*100);
                lineMaps.add(objectObjectHashMap);
                Map<String, Object> map = tRouteStationService.getMap(new QueryWrapper<TRouteStationDto>()
                        .select("id","station_id")
                        .eq("route_id", routeId)
                        .orderByDesc("create_date")
                        .last("limit 1")
                );

                String currStation = "";
                //上一站
                Map<Object, Object> lastMap = Maps.newHashMap();
                //当前站
                Map<Object, Object> curMap = Maps.newHashMap();
                //下一站
                Map<Object, Object> nextMap = Maps.newHashMap();
                if(map == null){
                    curMap.putAll(lineMaps.get(0));
                    nextMap.putAll(lineMaps.get(1));
                }else{
                    currStation = StringUtil.trim(map.get("station_id"));
                    boolean isEnd = false;
                    for (int i = 0; i < lineMaps.size(); i++) {
                        Map<Object, Object> lineMap = lineMaps.get(i);
                        String id = StringUtil.trim(lineMap.get("station_id"));
                        if(id.equals(currStation)){
                            lastMap.putAll(lineMap);
                            if(i == lineMaps.size()-1){
                                isEnd = true;
                            }else{
                                curMap.putAll(lineMaps.get(i+1));
                            }
                            break;
                        }
                    }
                    //如果下一站为空  则证明已经到了最后一站
                    if(isEnd) {
                        log.info("行程ID:"+routePeakDto.getId()+"==已经到了最后一站");
                        tRouteStationService.removeById(StringUtil.trim(map.get("id")));
                        //结束行程
                        TRoutePeakDto update = new TRoutePeakDto();
                        update.setId(routePeakDto.getId());
                        update.setUpdateDate(date);
                        update.setRouteState("1");
                        update.setEndDate(date);
                        boolean b1 = tRoutePeakService.updateById(update);
                        log.info("结束行程ID:"+routePeakDto.getId()+" status:"+b1);

                        //车辆到站
                        List<TOrderDto> tOrderDtoList = tOrderService.list(new QueryWrapper<TOrderDto>()
                                .select("id")
                                .eq("order_status", '2')
                                .eq("route_id", routeId)
                        );
                        if (tOrderDtoList != null && tOrderDtoList.size() > 0) {
                            tOrderDtoList.forEach(tOrderDto ->{
                                tOrderDto.setOrderStatus("3");
                                tOrderDto.setRideEndDate(date);
                            });
                            //批量修改订单
                            boolean b = tOrderService.updateBatchById(tOrderDtoList);
                            log.info("行程ID为:"+routePeakDto.getId()+"  修改行程对应订单的下车时间"+b);
                        }

                        //判断是否继续行程
                        try {
                            //当前时间
                            String peakTimeRange = redisUtil.get(RedisConstant.SYS_PEAK_TIME_RANGE);
                            String currentTimeRange = dateFormat.format(date);
                            String[] time = peakTimeRange.split("-");
                            //当前时间
                            Date currentTime = dateFormat.parse(currentTimeRange);
                            //7:30高峰开始时间
                            Date stratTime = dateFormat.parse(time[0]);
                            //9:20高峰结束派单
                            Date endTime = null;
                            if(currentTime.after(dateFormat.parse("12:00"))){
                                endTime = dateFormat.parse("21:50");
                            }else{
                                endTime = dateFormat.parse("9:40");
                            }
                            if (currentTime.after(stratTime) && currentTime.before(endTime)) {
                                //添加新的行程
                                Map<String, Object> queryMap = tRoutePeakService.getMap(new QueryWrapper<TRoutePeakDto>()
                                        .eq("driver_user_id", driverId)
                                        .eq("route_state", "0")
                                );
                                if(queryMap == null) {
                                    synchronized (this) {
                                        Map<String, Object> queryMap1 = tRoutePeakService.getMap(new QueryWrapper<TRoutePeakDto>()
                                                .eq("driver_user_id", driverId)
                                                .eq("route_state", "0")
                                        );
                                        if (queryMap1 == null) {
                                            TRoutePeakDto insertRoutePeakDto = new TRoutePeakDto();
                                            insertRoutePeakDto.setLineId(lineId);
                                            insertRoutePeakDto.setDriverUserId(driverId);
                                            insertRoutePeakDto.setCarId(carId);
                                            insertRoutePeakDto.setCreateDate(date);
                                            insertRoutePeakDto.setStartDate(date);
                                            insertRoutePeakDto.setRouteState("0");
                                            insertRoutePeakDto.setLineStartId(routePeakDto.getLineStartId());
                                            insertRoutePeakDto.setLineEndId(routePeakDto.getLineEndId());
                                            boolean save = tRoutePeakService.save(insertRoutePeakDto);
                                            //type 0 高峰 1平峰
                                            if (save) {
                                                log.info("续航高峰司机成功......");
                                            }
                                        }
                                    }
                                }
                            }else{
                                log.info("续航高峰司机失败......当前时间已超过9：20,不再开始高峰行程");
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                }
                // 计算距离
                double v = 120;
                try {
                    v = MapUtil.GetDistance(carPosition.getLongitude(), carPosition.getLatitude(), StringUtil.getDouble(StringUtil.trim(curMap.get("longitude"))),StringUtil.getDouble(StringUtil.trim(curMap.get("latitude"))));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (v <= 100) {
                    String stationId = StringUtil.trim(curMap.get("station_id"));

                    //车辆到站
                    List<TOrderDto> tOrderDtoList = tOrderService.list(new QueryWrapper<TOrderDto>()
                            .select("id")
                            .eq("car_id", carId)
                            .eq("order_status", '2')
                            .eq("end_station_id", stationId)
                    );
                    if (tOrderDtoList != null && tOrderDtoList.size() > 0) {
                        tOrderDtoList.forEach(tOrderDto ->{
                            tOrderDto.setOrderStatus("3");
                            tOrderDto.setRideEndDate(date);
                        });
                        //批量修改订单
                        boolean b = tOrderService.updateBatchById(tOrderDtoList);
                        log.info("行程ID:"+routePeakDto.getId()+"==车辆已经到达"+stationId+"站点，开始结束订单:"+tOrderDtoList.toString());
                    }

                    //查询行程是否添加过记录
                    Map<String, Object> routeStationMap = tRouteStationService.getMap(new QueryWrapper<TRouteStationDto>()
                            .eq("route_id", routeId)
                            .eq("station_id", stationId)
                    );
                    if(routeStationMap == null) {
                        //添加数据
                        TRouteStationDto one = new TRouteStationDto();
                        one.setStationId(stationId);
                        one.setRouteId(routeId);
                        one.setRouteType("1");
                        //若还未到第一个站
                        if (StringUtil.isEmpty(currStation)) {
                            one.setPreStationMile(0d);
                        } else {
                            try {
                                one.setPreStationMile(MapUtil.GetDistance(
                                        StringUtil.getDouble(StringUtil.trim(curMap.get("longitude"))),
                                        StringUtil.getDouble(StringUtil.trim(curMap.get("latitude"))),
                                        StringUtil.getDouble(StringUtil.trim(lastMap.get("longitude"))),
                                        StringUtil.getDouble(StringUtil.trim(lastMap.get("latitude")))
                                ));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        one.setCreateDate(new Date());
                        boolean save = tRouteStationService.save(one);
                        log.info("行程ID:"+routePeakDto.getId()+"==车辆已经到达"+stationId+"站点，开始添加车辆停靠站点:"+save);
                    }

                }
                return true;
            }
            if (checkOffPeakRoute(carPosition, date, carId)) return true;

        }else {
            //平峰信息
            if (checkOffPeakRoute(carPosition, date, carId)) return true;
        }
        return true;
    }

    /**
     * 查询车辆是否到站(增加GPS判断)
     *
     * @param carPosition 车辆实时位置
     * @return boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean carGetDown(TCarPositionDto carPosition) {
        Date date = new Date();
        //根据车辆查询线路
        String carId = carPosition.getCarId();

        //检查车辆是否有高峰信息
        String toDate = DateUtils.formatDate(new Date(), DateUtils.PATTERN_yyyy_MM_dd);
        String driverId = redisUtil.get(RedisConstant.SYS_TODAY_CAR_DRIVER_BIND_VALUE + toDate + carId);
//        driverId ="0002";
        TLineDriverBindDto one1 = rLineDriverBindService.getOne(new QueryWrapper<TLineDriverBindDto>()
                    .eq("driver_id", driverId)
        );
        if(one1!= null){
            //如果有未完成的行程
            TRoutePeakDto routePeakDto = tRoutePeakService.getOne(new QueryWrapper<TRoutePeakDto>()
                        .eq("driver_user_id", driverId)
                        .eq("route_state","0")
            );
            if(routePeakDto!= null){
                String lineId = one1.getLineId();
                String routeId = routePeakDto.getId();
                //查出线路所有站点信息
                String lineStationkey = redisUtil.get("maas_peak_line_station" + lineId);
                List<Map<Object, Object>> lineMaps = JsonUtil.parseJSON2List(lineStationkey);
                /*lineMaps = lineMaps.stream().filter(line ->
                            {
                                try {
                                    Double type = StringUtil.getDouble(StringUtil.trim(line.get("type")));
                                    return type.intValue() == 1;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return false;
                            }
                ).collect(Collectors.toList());*/


                HashMap<Object, Object> objectObjectHashMap = Maps.newHashMap();
                objectObjectHashMap.putAll(lineMaps.get(0));
                objectObjectHashMap.put("station_id",StringUtil.getAsInt(StringUtil.trim(lineMaps.get(0).get("station_id")))*100);
                lineMaps.add(objectObjectHashMap);
                Map<String, Object> map = tRouteStationService.getMap(new QueryWrapper<TRouteStationDto>()
                            .select("id","station_id")
                            .eq("route_id", routeId)
                            .orderByDesc("create_date")
                            .last("limit 1")
                );

                //查找当前车辆距离最近的站点,得出最近距离minDistance和站点信息minStation
                double minDistance=0;
                Map<Object, Object> minStation=null;
                Map<Object, Object> preStation=null;
                Map<Object, Object> prePreStation=null;
                //if(map!= null && !map.isEmpty()) {
                    for (int i = 0; i < lineMaps.size(); i++) {
                        Map<Object, Object> lineMap1 = lineMaps.get(i);
                        try {
                            double distance = MapUtil.GetDistance(carPosition.getLongitude(), carPosition.getLatitude(), StringUtil.getDouble(StringUtil.trim(lineMap1.get("longitude"))), StringUtil.getDouble(StringUtil.trim(lineMap1.get("latitude"))));
                            if (i == 0) {
                                minDistance = MapUtil.GetDistance(carPosition.getLongitude(), carPosition.getLatitude(), StringUtil.getDouble(StringUtil.trim(lineMap1.get("longitude"))), StringUtil.getDouble(StringUtil.trim(lineMap1.get("latitude"))));
                            } else if (i != 0 && distance <= minDistance) {
                                if(minStation!=null&&!minStation.isEmpty()){
                                    double index1 = Double.parseDouble(lineMap1.get("index").toString());
                                    double index2 = Double.parseDouble(minStation.get("index").toString());
                                    if (distance == minDistance && index1 > index2) {
                                        continue;
                                    }
                                }

                                minDistance = distance;
                                minStation = lineMap1;
                                if (minStation != null) {
                                    preStation = lineMaps.get(i - 1);
                                    if(i>1) {
                                        prePreStation = lineMaps.get(i - 2);
                                    }
                                }

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                //}
                String currStation = "";
                //上一站
                Map<Object, Object> lastMap = Maps.newHashMap();
                //当前站
                Map<Object, Object> curMap = Maps.newHashMap();
                //下一站
                Map<Object, Object> nextMap = Maps.newHashMap();
                if(map == null){
                    curMap.putAll(lineMaps.get(0));
                    nextMap.putAll(lineMaps.get(1));
                }else{
                    currStation = StringUtil.trim(map.get("station_id"));
                    boolean isEnd = false;
                    for (int i = 0; i < lineMaps.size(); i++) {
                        Map<Object, Object> lineMap = lineMaps.get(i);
                        String id = StringUtil.trim(lineMap.get("station_id"));

                        if(id.equals(currStation)){
                            lastMap.putAll(lineMap);
                            if(i == lineMaps.size()-1){
                                isEnd = true;
                            }else{
                                curMap.putAll(lineMaps.get(i+1));//查到的行程的下一站
                            }
                            break;
                        }

                    }


                    //如果下一站为空  则证明已经到了最后一站
                    if(isEnd) {
                        log.info("行程ID:"+routePeakDto.getId()+"==已经到了最后一站");
                        tRouteStationService.removeById(StringUtil.trim(map.get("id")));
                        //结束行程
                        TRoutePeakDto update = new TRoutePeakDto();
                        update.setId(routePeakDto.getId());
                        update.setUpdateDate(date);
                        update.setRouteState("1");
                        update.setEndDate(date);
                        boolean b1 = tRoutePeakService.updateById(update);
                        log.info("结束行程ID:"+routePeakDto.getId()+" status:"+b1);

                        //车辆到站
                        List<TOrderDto> tOrderDtoList = tOrderService.list(new QueryWrapper<TOrderDto>()
                                    .select("id")
                                    .eq("order_status", '2')
                                    .eq("route_id", routeId)
                        );
                        if (tOrderDtoList != null && tOrderDtoList.size() > 0) {
                            tOrderDtoList.forEach(tOrderDto ->{
                                tOrderDto.setOrderStatus("3");
                                tOrderDto.setUpdateDate(date);
                                tOrderDto.setRideEndDate(date);
                            });
                            //批量修改订单
                            boolean b = tOrderService.updateBatchById(tOrderDtoList);
                            log.info("行程ID为:"+routePeakDto.getId()+"  修改行程对应订单的下车时间"+b);
                        }

                        //判断是否继续行程
                        try {
                            //当前时间
                            String peakTimeRange = redisUtil.get(RedisConstant.SYS_PEAK_TIME_RANGE);
                            String currentTimeRange = dateFormat.format(date);
                            String[] time = peakTimeRange.split("-");
                            //当前时间
                            Date currentTime = dateFormat.parse(currentTimeRange);
                            //7:30高峰开始时间
                            Date stratTime = dateFormat.parse(time[0]);
                            //9:20高峰结束派单
                            Date endTime = null;
                            if(currentTime.after(dateFormat.parse("12:00"))){
                                endTime = dateFormat.parse("21:50");
                            }else{
                                endTime = dateFormat.parse("9:40");
                            }
                            if (currentTime.after(stratTime) && currentTime.before(endTime)) {
                                //添加新的行程
                                Map<String, Object> queryMap = tRoutePeakService.getMap(new QueryWrapper<TRoutePeakDto>()
                                            .eq("driver_user_id", driverId)
                                            .eq("route_state", "0")
                                );
                                if(queryMap == null) {
                                    synchronized (this) {
                                        Map<String, Object> queryMap1 = tRoutePeakService.getMap(new QueryWrapper<TRoutePeakDto>()
                                                    .eq("driver_user_id", driverId)
                                                    .eq("route_state", "0")
                                        );
                                        if (queryMap1 == null) {
                                            TRoutePeakDto insertRoutePeakDto = new TRoutePeakDto();
                                            insertRoutePeakDto.setLineId(lineId);
                                            insertRoutePeakDto.setDriverUserId(driverId);
                                            insertRoutePeakDto.setCarId(carId);
                                            insertRoutePeakDto.setCreateDate(date);
                                            insertRoutePeakDto.setStartDate(date);
                                            insertRoutePeakDto.setRouteState("0");
                                            insertRoutePeakDto.setLineStartId(routePeakDto.getLineStartId());
                                            insertRoutePeakDto.setLineEndId(routePeakDto.getLineEndId());
                                            boolean save = tRoutePeakService.save(insertRoutePeakDto);
                                            //type 0 高峰 1平峰
                                            if (save) {
                                                log.info("续航高峰司机成功......");
                                            }
                                        }
                                    }
                                }
                            }else{
                                log.info("续航高峰司机失败......当前时间已超过9：20,不再开始高峰行程");
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                }
                double v = 120;
                //判断查找的当前站点与车辆最近的站点是否相符合
                if(minStation!=null&&!StringUtil.trim(minStation.get("station_id")).equals(StringUtil.trim(curMap.get("station_id")))){
                    //不符合的情况下
                    curMap=minStation;
                    v=minDistance;
                    lastMap=preStation;
                    //添加缺少的行程途径站点信息(之前一站）
                    String preStationId=StringUtil.trim(preStation.get("station_id"));
                    //查询行程是否添加过记录
                    Map<String, Object> routeStationMap = tRouteStationService.getMap(new QueryWrapper<TRouteStationDto>()
                                .eq("route_id", routeId)
                                .eq("station_id", preStationId)
                    );
                    if(routeStationMap == null) {
                        //添加数据
                        TRouteStationDto one = new TRouteStationDto();
                        one.setStationId(preStationId);
                        one.setRouteId(routeId);
                        one.setRouteType("1");
                        if((prePreStation!=null&&!prePreStation.isEmpty())||(preStation!=null&&!preStation.isEmpty())){
                            //若还未到第一个站
                            if (StringUtil.isEmpty(currStation)) {
                                one.setPreStationMile(0d);
                            } else {
                                try {
                                    one.setPreStationMile(MapUtil.GetDistance(
                                                StringUtil.getDouble(StringUtil.trim(preStation.get("longitude"))),
                                                StringUtil.getDouble(StringUtil.trim(preStation.get("latitude"))),
                                                StringUtil.getDouble(StringUtil.trim(prePreStation.get("longitude"))),
                                                StringUtil.getDouble(StringUtil.trim(prePreStation.get("latitude")))
                                    ));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        one.setCreateDate(new Date());
                        boolean save = tRouteStationService.save(one);
                    }
                }
                // 计算距离

                try {
                    v = MapUtil.GetDistance(carPosition.getLongitude(), carPosition.getLatitude(), StringUtil.getDouble(StringUtil.trim(curMap.get("longitude"))),StringUtil.getDouble(StringUtil.trim(curMap.get("latitude"))));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (v <= 100) {
                    String stationId = StringUtil.trim(curMap.get("station_id"));

                    //车辆到站
                    List<TOrderDto> tOrderDtoList = tOrderService.list(new QueryWrapper<TOrderDto>()
                                .select("id")
                                .eq("car_id", carId)
                                .eq("order_status", '2')
                                .eq("end_station_id", stationId)
                    );
                    if (tOrderDtoList != null && tOrderDtoList.size() > 0) {
                        tOrderDtoList.forEach(tOrderDto ->{
                            tOrderDto.setOrderStatus("3");
                            tOrderDto.setUpdateDate(date);
                            tOrderDto.setRideEndDate(date);
                        });
                        //批量修改订单
                        boolean b = tOrderService.updateBatchById(tOrderDtoList);
                        log.info("行程ID:"+routePeakDto.getId()+"==车辆已经到达"+stationId+"站点，开始结束订单:"+tOrderDtoList.toString());
                    }

                    //查询行程是否添加过记录
                    Map<String, Object> routeStationMap = tRouteStationService.getMap(new QueryWrapper<TRouteStationDto>()
                                .eq("route_id", routeId)
                                .eq("station_id", stationId)
                    );
                    if(routeStationMap == null) {
                        //添加数据
                        TRouteStationDto one = new TRouteStationDto();
                        one.setStationId(stationId);
                        one.setRouteId(routeId);
                        one.setRouteType("1");
                        //若还未到第一个站
                        if (StringUtil.isEmpty(currStation)) {
                            one.setPreStationMile(0d);
                        } else {
                            try {
                                one.setPreStationMile(MapUtil.GetDistance(
                                            StringUtil.getDouble(StringUtil.trim(curMap.get("longitude"))),
                                            StringUtil.getDouble(StringUtil.trim(curMap.get("latitude"))),
                                            StringUtil.getDouble(StringUtil.trim(lastMap.get("longitude"))),
                                            StringUtil.getDouble(StringUtil.trim(lastMap.get("latitude")))
                                ));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        one.setCreateDate(new Date());
                        boolean save = tRouteStationService.save(one);
                        log.info("行程ID:"+routePeakDto.getId()+"==车辆已经到达"+stationId+"站点，开始添加车辆停靠站点:"+save);
                    }

                }
                return true;
            }
            if (checkOffPeakRoute(carPosition, date, carId)) return true;

        }else {
            //平峰信息
            if (checkOffPeakRoute(carPosition, date, carId)) return true;
        }
        return true;
    }

    /**
     * 平峰控制到站
     * @param carPosition
     * @param date
     * @param carId
     * @return
     */
    private boolean checkOffPeakRoute(TCarPositionDto carPosition, Date date, String carId) {
        //平峰信息
        log.info("平峰控制到站"+carId);
        TRouteOffpeakDto tRouteOffpeakDto = tRouteOffpeakService.getOne(new QueryWrapper<TRouteOffpeakDto>()
                .eq("route_state", "0")
                .eq("car_id", carId)
                .apply("to_char(create_date,'yyyy-MM-dd')={0}", DateUtils.formatDate(date, Constants.DATE_PATTERN))
        );
        if (tRouteOffpeakDto != null) {
            //获取途经站点
            String midwayStation = tRouteOffpeakDto.getMidwayStation();
            if (StringUtil.isEmpty(midwayStation)) {
                return true;
            }
            //
            List<String> midwayStations = Arrays.asList(midwayStation.split(","));
            //查询缓存的站点信息
            String stations = redisUtil.get(RedisConstant.SYS_STATIONS_CACHE_KEY);
            List<Map<Object, Object>> stationList = JsonUtil.parseJSON2List(stations);

            //只获取平峰的站点

            //只需要计算是否到了下一站
            String currStation = tRouteOffpeakDto.getCurrStation();
            //上一站
            Map<Object, Object> lastMap = Maps.newHashMap();
            //当前站
            Map<Object, Object> curMap = Maps.newHashMap();
            if (currStation.equals("-1")) {
                //还未到第一站
                for (Map<Object, Object> station : stationList) {
                    String id = StringUtil.trim(station.get("id"));
                    if (id.equals(midwayStations.get(0))) {
                        curMap.putAll(station);
                        break;
                    }
                }
            } else {
                //计算上一站与下一站的距离
                List<Map<Object, Object>> newStationList = Lists.newArrayList();
                for (String _midwayStation : midwayStations) {
                    for (Map<Object, Object> station : stationList) {
                        String id = StringUtil.trim(station.get("id"));
                        if (_midwayStation.equals(id)) {
                            newStationList.add(station);
                            break;
                        }
                    }
                }
                Boolean isEnd = false;
                int asInt = StringUtil.getAsInt(currStation);
                if(asInt != newStationList.size()-1){
                    lastMap = newStationList.get(asInt);
                    curMap = newStationList.get(asInt+1);
                }else{
                    lastMap = newStationList.get(asInt-1);
                    curMap = newStationList.get(asInt);
                    isEnd = true;
                }

                //如果下一站为空  则已经到了最后一站
                if (isEnd) {
                    //结束行程
                    TRouteOffpeakDto update = new TRouteOffpeakDto();
                    String routeId = tRouteOffpeakDto.getId();
                    update.setId(routeId);
                    update.setUpdateDate(date);
                    update.setRouteState("1");
                    update.setEndDate(date);
                    tRouteOffpeakService.updateById(update);
                    //行程结束
                    //车辆到站
                    List<TOrderDto> tOrderDtoList = tOrderService.list(new QueryWrapper<TOrderDto>()
                            .select("id")
                            .eq("order_status", '2')
                            .eq("route_id", routeId)
                    );
                    if (tOrderDtoList != null && tOrderDtoList.size() > 0) {
                        tOrderDtoList.forEach(tOrderDto -> {
                            tOrderDto.setOrderStatus("3");
                            tOrderDto.setUpdateDate(date);
                            tOrderDto.setRideEndDate(date);
                        });
                        //批量修改订单
                        tOrderService.updateBatchById(tOrderDtoList);
                    }
                    return true;
                }
            }

            // 计算距离
            double v = 120;
            try {
                v = MapUtil.GetDistance(carPosition.getLongitude(), carPosition.getLatitude(), StringUtil.getDouble(StringUtil.trim(curMap.get("longitude"))), StringUtil.getDouble(StringUtil.trim(curMap.get("latitude"))));
                log.info("当前车辆信息 longitude:"+carPosition.getLongitude()
                        + "  latitude:"+carPosition.getLatitude());
                log.info("当前站点信息 longitude:"+StringUtil.getDouble(StringUtil.trim(curMap.get("longitude")))
                        + "  latitude:"+StringUtil.getDouble(StringUtil.trim(curMap.get("latitude")))
                );
                log.info("平峰控制到站计算距离"+v+"，当前站点："+StringUtil.trim(curMap.get("id")));
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (v <= 100) {
                String routeId = tRouteOffpeakDto.getId();
                String stationId = StringUtil.trim(curMap.get("id"));
                //车辆到站
                List<TOrderDto> tOrderDtoList = tOrderService.list(new QueryWrapper<TOrderDto>()
                        .select("id")
                        .eq("car_id", carId)
                        .eq("order_status", '2')
                        .eq("end_station_id", stationId)
                );
                if (tOrderDtoList != null && tOrderDtoList.size() > 0) {
                    tOrderDtoList.forEach(tOrderDto -> {
                        tOrderDto.setOrderStatus("3");
                        tOrderDto.setUpdateDate(date);
                        tOrderDto.setRideEndDate(date);
                    });
                    //批量修改订单
                    tOrderService.updateBatchById(tOrderDtoList);
                }

                //查询行程是否添加过记录
                Map<String, Object> map = tRouteStationService.getMap(new QueryWrapper<TRouteStationDto>()
                        .eq("route_id", routeId)
                        .eq("station_id", stationId)
                );
                if (map == null) {
                    //添加数据
                    TRouteStationDto one = new TRouteStationDto();
                    one.setStationId(stationId);
                    one.setRouteId(routeId);
                    one.setOffNum(tOrderDtoList.size());//添加站点下车人数
                    one.setRouteType("0");
                    //若还未到第一个站
                    if (currStation.equals("-1")) {
                        one.setPreStationMile(0d);
                    } else {
                        one.setPreStationMile(MapUtil.GetDistance((Double) curMap.get("longitude"), (Double) curMap.get("latitude"), (Double) lastMap.get("longitude"), (Double) lastMap.get("latitude")));
                    }
                    tRouteStationService.save(one);
                    log.info("添加平峰到站当前站点："+StringUtil.trim(curMap.get("id")));
                }

                //修改平峰当前站数据
                int asInt = StringUtil.getAsInt(currStation);
                tRouteOffpeakDto.setCurrStation(asInt+1+"");
                String allPosition = tRouteOffpeakDto.getAllPosition();
                List<Map<Object, Object>> maps = JsonUtil.parseJSON2List(allPosition);
                if(asInt == -1){
                    maps = maps.subList(1,maps.size());
                }else{
                    maps = maps.subList(StringUtil.getAsInt(currStation)+1,maps.size());
                }
                tRouteOffpeakDto.setAllPosition(JsonUtil.object2Json(maps));
                tRouteOffpeakService.updateById(tRouteOffpeakDto);

                //添加车辆到站标识
                int carStatus = StringUtil.getAsInt(StringUtil.trim(carClients.get(carId)),0);
                //如果车辆未到站
                if(carStatus == 0){
                    log.info("车辆到达站点："+StringUtil.trim(curMap.get("id")));
                    //将停靠的时间传给前端,
                    //为了防止服务端与客户端的时差，故只传给前端秒数。
                    carTimes.put(carId,180);
                    kafkaTemplate.send(MessageConstant.BUSS_STOP_TIMES_TOPIC_MESSAGE, JsonUtil.toJson(carTimes));
                    carTimes.remove(carId);
                    //停止接单时间
                    redisUtil.set("stop_order_times"+carId,DateUtils.addMinutes(date, 3).getTime(),180l);
                    redisUtil.set("overdue_order_times^"+routeId+":"+midwayStations.get(asInt+1), 1,180l);
                    //获取车辆位置
                    List<Map<String, Object>> vehicleList = getVehicleList();
                    if(vehicleList != null && vehicleList.size()>0){
                        Map<String,Object> maasCommonData = Maps.newHashMap();
                        maasCommonData.put("cancleOrder",null);
                        maasCommonData.put("finishedOrderId", null);
                        maasCommonData.put("completeOrder",null);
                        maasCommonData.put("unTakeOrder",null);
                        maasCommonData.put("takeOrder",null);
                        maasCommonData.put("timeOutOrder",null);
                        maasCommonData.put("vehicleList",vehicleList);
                        carIds.add(carId);
                        maasCommonData.put("unableVehicleList",carIds);

                        //车辆到站，调用停止接单的算法
                        log.info("车辆到站，调用停止接单算法请求数据:"+JSONUtil.toJson(maasCommonData));
                        JSONObject jsonObject = HttpClientUtil.httpPost(SysParamConstant.COMMON_URL, JSONUtil.toJson(maasCommonData));
                        log.info("车辆到站，调用停止接单算法响应数据:"+jsonObject);

                    }
                    //设置车辆已到站
                    carClients.put(carId,1);
//                }else if(carStatus == 1){
//                    //判断是否到
//                    String stopsTimesCache = redisUtil.get("stop_order_times" + carId);
//                    //如果缓存键不存在，则说明车辆已经在站点停靠了3分钟
//                    if(StringUtil.isEmpty(stopsTimesCache)){
//                        //通知前端开始调用导航
//
//                    }
                }
            }else{
                //carStatus 1 车辆到站
                int carStatus = StringUtil.getAsInt(StringUtil.trim(carClients.get(carId)),0);
                if(carStatus == 1){
                    //1分钟后开始接单
                    log.info("车辆"+carId+"1分钟后开始接单");
                    String stop_order_times_cache = redisUtil.get("stop_order_times" + carId);
                    if(StringUtil.isEmpty(stop_order_times_cache)){
                        redisUtil.set("start_order_times"+carId,DateUtils.addMinutes(date, 1).getTime(),60l);
                        carClients.put(carId,2);
                    }
                }else if(carStatus == 2){
                    String startTimesCache = redisUtil.get("start_order_times" + carId);
                    if(StringUtil.isEmpty(startTimesCache)){
                        log.info("车辆"+carId+"出站"+StringUtil.trim(curMap.get("id"))+"并开始接单");
                        //调用算法开始接单
                        //获取车辆位置
                        List<Map<String, Object>> vehicleList = getVehicleList();
                        if(vehicleList != null && vehicleList.size()>0){

                            //查询车辆的已完成订单
                            List<Map<String, Object>> orderMaps = tOrderService.listMaps(new QueryWrapper<TOrderDto>()
                                    .select("id")
                                    .eq("car_id", carId)
                                    .eq("order_status", '3')
                            );

                            Map<String,Object> maasCommonData = Maps.newHashMap();
                            List<String> finishedOrders = Lists.newArrayList();
                            if(orderMaps!= null  && orderMaps.size()>0){
                                orderMaps.forEach((order) -> finishedOrders.add(StringUtil.trim(order.get("id"))));
                            }
                            maasCommonData.put("cancleOrder",null);
                            maasCommonData.put("finishedOrderId", finishedOrders);
                            maasCommonData.put("completeOrder", null);
                            maasCommonData.put("unTakeOrder",null);
                            maasCommonData.put("takeOrder",null);
                            maasCommonData.put("vehicleList",vehicleList);
                            maasCommonData.put("timeOutOrder",null);
                            carIds.remove(carId);
                            maasCommonData.put("unableVehicleList",carIds);

                            //车辆离站，调用开始接单算法请求数据
                            log.info("车辆离站，调用开始接单算法请求数据:"+JSONUtil.toJson(maasCommonData));
                            JSONObject jsonObject = HttpClientUtil.httpPost(SysParamConstant.COMMON_URL, JSONUtil.toJson(maasCommonData));
                            log.info("车辆离站，调用开始接单算法请求数据:"+jsonObject);
                            carClients.put(carId,0);
                        }
                    }
                }
            }
        }
        return false;
    }



    /**
     * 司机确认到站
     * @param condition
     * @return
     */
    public ResultVo confirmArrive(Map<String, Object> condition){
        String carId = StringUtil.trim(condition.get("carId"));
        Date date = new Date();
        //检查车辆是否有高峰信息
        String toDate = DateUtils.formatDate(new Date(), DateUtils.PATTERN_yyyy_MM_dd);
        String driverId = redisUtil.get(RedisConstant.SYS_TODAY_CAR_DRIVER_BIND_VALUE + toDate + carId);
        TLineDriverBindDto one1 = rLineDriverBindService.getOne(new QueryWrapper<TLineDriverBindDto>()
                .eq("driver_id", driverId)
        );
        //如果司机绑定了高峰行程，则先判断存在高峰行程是否结束
        if(one1!= null) {
            //如果有未完成的行程
            TRoutePeakDto routePeakDto = tRoutePeakService.getOne(new QueryWrapper<TRoutePeakDto>()
                    .eq("driver_user_id", driverId)
                    .eq("route_state", "0")
            );
            //有高峰行程
            if (routePeakDto != null) {
                String lineId = one1.getLineId();
                String routeId = routePeakDto.getId();
                //查出线路所有站点信息
                String lineStationkey = redisUtil.get("maas_peak_line_station" + lineId);
                List<Map<Object, Object>> lineMaps = JsonUtil.parseJSON2List(lineStationkey);
                //过滤锚点数据
                lineMaps = lineMaps.stream().filter(line ->
                            {
                                try {
                                    Double type = StringUtil.getDouble(StringUtil.trim(line.get("type")));
                                    return type.intValue() == 1;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return false;
                            }
                ).collect(Collectors.toList());

                Map<String, Object> map = tRouteStationService.getMap(new QueryWrapper<TRouteStationDto>()
                        .select("id","station_id")
                        .eq("route_id", routeId)
                        .orderByDesc("create_date")
                        .last("limit 1")
                );

                //上一站
                Map<Object, Object> lastMap = Maps.newHashMap();
                //当前站
                Map<Object, Object> curMap = Maps.newHashMap();
                //下一站点
                String nextStation = null;
                if(map != null){
                    //当前站点
                    String currStation = StringUtil.trim(map.get("station_id"));
                    boolean isEnd = false;
                    for (int i = 0; i < lineMaps.size(); i++) {
                        Map<Object, Object> lineMap = lineMaps.get(i);
                        String id = StringUtil.trim(lineMap.get("station_id"));
                        if(id.equals(currStation)){
                            lastMap.putAll(lineMap);//查到的行程的下一站
                            if(i == lineMaps.size()-1) {
                                isEnd = true;
                            }else{
                                curMap = lineMaps.get(i+1);
                                nextStation =  StringUtil.trim(curMap.get("station_id"));
                            }
                            break;
                        }
                    }

                    //如果是最后一个站点，则结束行程
                    if(isEnd){
                        log.info("行程ID:"+routePeakDto.getId()+"==已经到了最后一站");
                        //结束行程
                        TRoutePeakDto update = new TRoutePeakDto();
                        update.setId(routePeakDto.getId());
                        update.setUpdateDate(date);
                        update.setRouteState("1");
                        update.setEndDate(date);
                        boolean b1 = tRoutePeakService.updateById(update);
                        log.info("结束行程ID:"+routePeakDto.getId()+" status:"+b1);

                        //车辆到站
                        List<TOrderDto> tOrderDtoList = tOrderService.list(new QueryWrapper<TOrderDto>()
                                .select("id")
                                .eq("order_status", '2')
                                .eq("route_id", routeId)
                        );
                        if (tOrderDtoList != null && tOrderDtoList.size() > 0) {
                            tOrderDtoList.forEach(tOrderDto ->{
                                tOrderDto.setOrderStatus("3");
                                tOrderDto.setUpdateDate(date);
                                tOrderDto.setRideEndDate(date);
                            });
                            //批量修改订单
                            boolean b = tOrderService.updateBatchById(tOrderDtoList);
                            log.info("行程ID为:"+routePeakDto.getId()+"  修改行程对应订单的下车时间"+b);
                        }

                        //判断是否继续行程
                        try {
                            //当前时间
                            String peakTimeRange = redisUtil.get(RedisConstant.SYS_PEAK_TIME_RANGE);
                            String currentTimeRange = dateFormat.format(date);
                            String[] time = peakTimeRange.split("-");
                            //当前时间
                            Date currentTime = dateFormat.parse(currentTimeRange);
                            //7:30高峰开始时间
                            Date stratTime = dateFormat.parse(time[0]);
                            //9:20高峰结束派单
                            Date endTime = null;
                            if(currentTime.after(dateFormat.parse("12:00"))){
                                endTime = dateFormat.parse("21:50");
                            }else{
                                endTime = dateFormat.parse("9:40");
                            }
                            if (currentTime.after(stratTime) && currentTime.before(endTime)) {
                                //添加新的行程
                                Map<String, Object> queryMap = tRoutePeakService.getMap(new QueryWrapper<TRoutePeakDto>()
                                        .eq("driver_user_id", driverId)
                                        .eq("route_state", "0")
                                );
                                if(queryMap == null) {
                                    synchronized (this) {
                                        Map<String, Object> queryMap1 = tRoutePeakService.getMap(new QueryWrapper<TRoutePeakDto>()
                                                .eq("driver_user_id", driverId)
                                                .eq("route_state", "0")
                                        );
                                        if (queryMap1 == null) {
                                            TRoutePeakDto insertRoutePeakDto = new TRoutePeakDto();
                                            insertRoutePeakDto.setLineId(lineId);
                                            insertRoutePeakDto.setDriverUserId(driverId);
                                            insertRoutePeakDto.setCarId(carId);
                                            insertRoutePeakDto.setCreateDate(date);
                                            insertRoutePeakDto.setStartDate(date);
                                            insertRoutePeakDto.setRouteState("0");
                                            insertRoutePeakDto.setLineStartId(routePeakDto.getLineStartId());
                                            insertRoutePeakDto.setLineEndId(routePeakDto.getLineEndId());
                                            boolean save = tRoutePeakService.save(insertRoutePeakDto);
                                            //type 0 高峰 1平峰
                                            if (save) {
                                                log.info("续航高峰司机成功......");
                                            }
                                        }
                                    }
                                }
                            }else{
                                log.info("续航高峰司机失败......当前时间已超过9：20,不再开始高峰行程");
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return ResultVo.success();
                    }

                }

                //查询行程是否添加过记录
                Map<String, Object> routeStationMap = tRouteStationService.getMap(new QueryWrapper<TRouteStationDto>()
                        .eq("route_id", routeId)
                        .eq("station_id", nextStation)
                );
                if(routeStationMap == null) {
                    //添加数据
                    TRouteStationDto one = new TRouteStationDto();
                    one.setStationId(nextStation);
                    one.setRouteId(routeId);
                    one.setRouteType("1");
                    //若还未到第一个站
                    if(map == null){
                        one.setPreStationMile(0d);
                    } else {
                        try {
                            one.setPreStationMile(MapUtil.GetDistance(
                                    StringUtil.getDouble(StringUtil.trim(lastMap.get("longitude"))),
                                    StringUtil.getDouble(StringUtil.trim(lastMap.get("latitude"))),
                                    StringUtil.getDouble(StringUtil.trim(curMap.get("longitude"))),
                                    StringUtil.getDouble(StringUtil.trim(curMap.get("latitude")))
                            ));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    one.setCreateDate(new Date());
                    boolean save = tRouteStationService.save(one);
                }

                return ResultVo.success();
            }else{

                //处理平峰到站

                return ResultVo.success();
            }
        }
        //处理平峰到站

        //添加平峰到站信息

        return null;
    }


    /**
     * 平峰控制到站
     * @param carPosition
     * @param date
     * @param carId
     * @return
     */
    private ResultVo finalCheckOffPeakRoute(TCarPositionDto carPosition, Date date, String carId) {
        ResultVo success = ResultVo.success();
        return success;
    }


    /**
     * 查询车辆列表
     * @return
     */
    private List<Map<String,Object>> getVehicleList() {
        //查询当前可用的司机
        List<Map<String, Object>> driverList = driverService.listMaps(new QueryWrapper<TDriverDto>()
                .select("id")
                .eq("status","1")
        );
        if(driverList!=null && driverList.size()>0){
            log.info("当前没有可用的司机......");
            List<String> driverIds = Lists.newArrayList();
            driverList.forEach(driver->driverIds.add(StringUtil.trim(driver.get("id"))));
            //查询司机当天对应的车辆信息
            List<Map<String, Object>> driverCarBindLists = driverCarBindService.listMaps(new QueryWrapper<TDriverCarBindDto>()
                    .select("car_id", "driver_id")
                    .in("driver_id",driverIds)
                    .eq("bind_status","1")
            );
            if(driverCarBindLists != null && driverCarBindLists.size() > 0){

                List<String> carIds = Lists.newArrayList();
                driverCarBindLists.forEach(driverCar->carIds.add(StringUtil.trim(driverCar.get("car_id"))));
                Map<String,Object> condition = Maps.newHashMap();
                condition.put("carIds",carIds);
                //查询车辆现在所属位置
                Map<String,Object> respMap = null;
                try {
                    respMap = carPositionService.calculateVehicleDirectionAngle(condition);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                List<Map<String,Object>> vehicleList = (List<Map<String, Object>>) respMap.get("vehicleList");
                return vehicleList;
            }else{
                log.info("当前司机没有绑定的车辆......");
            }
        }
        return null;
    }

    /**
     * 查询车辆总座位数
     *
     * @return ResultVo
     */
    @Override
    public Integer getCarCount() {
        return this.baseMapper.getCarCount();
    }


    /**  未测试(数据模拟困难，经纬度不懂)
     * 查询车辆是否到站
     *
     * @param carPosition 车辆实时位置
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean carGetDown1(TCarPositionDto carPosition) {
        //根据车辆查询线路
        TRoutePeakDto tRoutePeakDto = tRoutePeakService.getByCarId(carPosition.getCarId());
        if (tRoutePeakDto == null) {
            QueryWrapper<TRouteOffpeakDto> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("route_state", "0");
            queryWrapper.eq("car_id", carPosition.getCarId());
            //平峰信息
            TRouteOffpeakDto tRouteOffpeakDto = tRouteOffpeakService.getOne(queryWrapper);
            if (tRouteOffpeakDto != null) {
                if (StringUtil.isEmpty(tRouteOffpeakDto.getMidwayStation())) {
                    return true;
                }
                QueryWrapper<TStationDto> stationDtoQueryWrapper = new QueryWrapper<>();
                stationDtoQueryWrapper.in("id", Arrays.asList(tRouteOffpeakDto.getMidwayStation().split(",")));
                //平峰所有站点经纬度
                List<TStationDto> stationDtoList = tStationService.list(stationDtoQueryWrapper);
                if (stationDtoList.size() > 0) {

                    for (int i = 0; i < stationDtoList.size(); i++) {
                        //首发站排除
                        if (i != 0) {
                            // 计算距离
                            if (MapUtil.GetDistance(carPosition.getLongitude(), carPosition.getLatitude(), stationDtoList.get(i).getLongitude(), stationDtoList.get(i).getLatitude()) <= 50) {
                                QueryWrapper<TOrderDto> queryOrderWrapper = new QueryWrapper<>();
                                queryOrderWrapper.eq("car_id", carPosition.getCarId());
                                queryOrderWrapper.eq("order_status", '2');
                                queryOrderWrapper.eq("end_station_id", stationDtoList.get(i).getId());
                                List<TOrderDto> tOrderDtoList = tOrderService.list(queryOrderWrapper);
                                if (tOrderDtoList.size() > 0) {
                                    tOrderDtoList.forEach(tOrderDto -> tOrderDto.setOrderStatus("3"));
                                    //批量修改订单
                                    tOrderService.updateBatchById(tOrderDtoList);
                                }
                                QueryWrapper<TRouteStationDto> queryStationWrapper = new QueryWrapper<>();
                                queryWrapper.eq("station_id", stationDtoList.get(i).getId());
                                queryWrapper.eq("route_id", tRouteOffpeakDto.getId());
                                queryWrapper.eq("driver_id", tRouteOffpeakDto.getDriverUserId());
                                TRouteStationDto tRouteStationDto = tRouteStationService.getOne(queryStationWrapper);
                                //添加距离上一个站点里程数
                                tRouteStationDto.setPreStationMile(MapUtil.GetDistance(stationDtoList.get(i).getLongitude(), stationDtoList.get(i).getLatitude(), stationDtoList.get(i - 1).getLongitude(), stationDtoList.get(i - 1).getLatitude()));
                                //添加站点下车人数
                                tRouteStationDto.setOffNum(tOrderDtoList.size());
                                tRouteStationService.updateById(tRouteStationDto);
                                //修改当前到站标识
                                tRouteOffpeakDto.setCurrStation(stationDtoList.get(i).getId());
                                if (i==stationDtoList.size()){
                                    tRouteOffpeakDto.setRouteState("1");
                                }
                                tRouteOffpeakService.updateById(tRouteOffpeakDto);
                                break;
                            }
                        }
                    }
                }
            }
        } else {
            if (StringUtil.isNotEmpty(tRoutePeakDto.getLineId())) {
                //查出线路所有站点信息
                List<TLineDto> lineDtoList = tLineService.lineVia(tRoutePeakDto.getLineId());
                for (int i = 0; i < lineDtoList.size(); i++) {
                    //首发站排除
                    if (i != 0) {
                        // 计算距离
                        if (MapUtil.GetDistance(carPosition.getLongitude(), carPosition.getLatitude(), lineDtoList.get(i).getLongitude(), lineDtoList.get(i).getLatitude()) <= 50) {
                            QueryWrapper<TOrderDto> queryWrapper = new QueryWrapper<>();
                            queryWrapper.eq("car_id", carPosition.getCarId());
                            queryWrapper.eq("order_status", '2');
                            queryWrapper.eq("end_station_id", lineDtoList.get(i).getStationId());
                            List<TOrderDto> tOrderDtoList = tOrderService.list(queryWrapper);
                            if (tOrderDtoList.size() > 0) {
                                tOrderDtoList.forEach(tOrderDto -> tOrderDto.setOrderStatus("3"));
                                //批量修改订单
                                tOrderService.updateBatchById(tOrderDtoList);
                            }
                            QueryWrapper<TRouteStationDto> queryStationWrapper = new QueryWrapper<>();
                            queryWrapper.eq("station_id", lineDtoList.get(i).getStationId());
                            queryWrapper.eq("route_id", tRoutePeakDto.getId());
                            queryWrapper.eq("driver_id", tRoutePeakDto.getDriverUserId());
                            TRouteStationDto tRouteStationDto = tRouteStationService.getOne(queryStationWrapper);
                            //添加距离上一个站点里程数
                            tRouteStationDto.setPreStationMile(MapUtil.GetDistance(lineDtoList.get(i).getLongitude(), lineDtoList.get(i).getLatitude(), lineDtoList.get(i - 1).getLongitude(), lineDtoList.get(i - 1).getLatitude()));
                            //添加站点下车人数
                            tRouteStationDto.setOffNum(tOrderDtoList.size());
                            tRouteStationService.updateById(tRouteStationDto);
                            //修改行程结束
                            if (i==lineDtoList.size()){
                                tRoutePeakDto.setRouteState("1");
                                tRoutePeakService.updateById(tRoutePeakDto);
                            }
                            break;
                        }
                    }
                }
            }
        }
        return true;
    }

}
