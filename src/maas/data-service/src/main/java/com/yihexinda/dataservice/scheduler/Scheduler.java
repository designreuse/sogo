package com.yihexinda.dataservice.scheduler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yihexinda.core.constants.Constants;
import com.yihexinda.core.constants.MessageConstant;
import com.yihexinda.core.constants.RedisConstant;
import com.yihexinda.core.constants.SysParamConstant;
import com.yihexinda.core.response.CommonResponse;
import com.yihexinda.core.response.RouteListObj;
import com.yihexinda.core.response.UserVehicle;
import com.yihexinda.core.utils.*;
import com.yihexinda.data.dto.*;
import com.yihexinda.dataservice.json.JSONUtil;
import com.yihexinda.dataservice.json.UtilDateProcessor;
import com.yihexinda.dataservice.service.*;
import com.yihexinda.dataservice.utils.RedisUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.processors.JsonValueProcessor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author Jack
 * @date 2018/11/5.
 */
@Component
@Slf4j
@EnableScheduling
public class Scheduler {

    @Autowired
    private TOrderService orderService;
    @Autowired
    private TCarPositionService carPositionService;
    @Autowired
    private TStationService stationService;
    @Autowired
    private TRouteOffpeakService routeOffpeakService;
    @Autowired
    private TRoutePeakService routePeakService;
    @Autowired
    private TDriverCarBindService driverCarBindService;
    @Autowired
    private TLineService lineService;
    @Autowired
    private TLineDriverBindService lineDriverBindService;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private TLinkService linkService;
    @Autowired
    private TNodeService nodeService;
    @Autowired
    private TViaService viaService;
    @Autowired
    private TRouteStationService routeStationService;
    @Autowired
    private TDriverService driverService;
    @Autowired
    private TCarService carService;

    @Autowired
    private RedisUtil redisUtil;


    /**
     * 实时订单，每隔10秒执行一次计算
     */
    @Scheduled(cron = "*/10 * * * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void maasRealCommonScheduled() throws Exception {
        Date date = new Date();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.select(
                "id", "passengers_num", "start_station_id",
                "end_station_id", "user_id", "create_date", "order_status");
        queryWrapper.isNull("car_id");
        queryWrapper.eq("route_type", "0");//行程类型( 0 平峰 1高峰 )
        queryWrapper.eq("order_type", "0");//实时出行
        queryWrapper.eq("is_pay", "1");//已支付
        queryWrapper.between("pay_date", DateUtils.addMinutes(date, -1), date);
        queryWrapper.eq("order_status", "1");//待使用

        //实时订单
        List<Map<String, Object>> unTakeOrderList = orderService.listMaps(queryWrapper);
        if(null != unTakeOrderList && unTakeOrderList.size()>0){
            //保存站点Ids
            List<String> stationIds = Lists.newArrayList();

            for (Map<String, Object> orderMap : unTakeOrderList) {
                //过滤不必要的参数
                Date create_date = new Date(((Date) orderMap.get("create_date")).getTime());
                orderMap.put("orderID", orderMap.get("id"));
                orderMap.put("ordertime",create_date);
                orderMap.put("people", orderMap.get("passengers_num"));
                orderMap.put("starttime", DateUtils.addSeconds(create_date,3));
                orderMap.put("state", orderMap.get("order_status"));
                orderMap.put("userID", orderMap.get("user_id"));
                orderMap.put("boardtime", null);//保存上车时间

                orderMap.remove("order_status");
                orderMap.remove("id");
                orderMap.remove("create_date");
                orderMap.remove("passengers_num");

                stationIds.add(StringUtil.trim(orderMap.get("start_station_id")));
                stationIds.add(StringUtil.trim(orderMap.get("end_station_id")));
            }

            //查询当前可用的司机
            List<Map<String, Object>> driverList = driverService.listMaps(new QueryWrapper<TDriverDto>()
                    .select("id")
                    .eq("status","1")
            );
            if(driverList==null || driverList.size()==0){
                log.info("当前没有可用的司机......");
                return ;
            }

            List<String> driverIds = Lists.newArrayList();
            driverList.forEach(driver->driverIds.add(StringUtil.trim(driver.get("id"))));
            //查询司机当天对应的车辆信息
            List<Map<String, Object>> driverCarBindLists = driverCarBindService.listMaps(new QueryWrapper<TDriverCarBindDto>()
                    .select("car_id", "driver_id")
                    .in("driver_id",driverIds)
                    .eq("bind_status","1")
            );
            if(driverCarBindLists == null || driverCarBindLists.size() == 0){
                log.info("当前司机没有绑定的车辆......");
                return ;
            }

            List<String> carIds = Lists.newArrayList();
            driverCarBindLists.forEach(driverCar->carIds.add(StringUtil.trim(driverCar.get("car_id"))));
            Map<String,Object> condition = Maps.newHashMap();
            condition.put("carIds",carIds);

            //查询车辆现在所属位置及计算方向角
            Map<String, Object> reqData = carPositionService.calculateVehicleDirectionAngle(condition);
            List<Map<String, Object>> vehicleList = (List<Map<String, Object>>) reqData.get("vehicleList");
            List<String> vehicleIds = (List<String>) reqData.get("vehicleIds");

            //查询车辆订单信息
            Map<String, Object> respTakeOrderMap = orderService.queryTakeOrder(vehicleIds);
            //车上订单
            List<Map<String, Object>> takeOrderList = (List<Map<String, Object>>) respTakeOrderMap.get("takeOrderList");
            //保存站点位置
            stationIds.addAll((List<String>)respTakeOrderMap.get("stationIds"));

            //查询上一次计算已完成的订单
            String maas_common = redisUtil.get("maas_common");
            Map<String, Object> responCompleteOrderMap = null;
            if(StringUtil.isEmpty(maas_common)){
                responCompleteOrderMap = orderService.queryCompleteOrder(DateUtils.addSeconds(date,10),date);
            }else{
                responCompleteOrderMap = orderService.queryCompleteOrder(
                        new Date(Long.valueOf(maas_common)),
                        date
                );
            }
            //返回的已完成订单
            List<Map<String, Object>> completeOrderList = (List<Map<String, Object>>) responCompleteOrderMap.get("completeOrderList");
            //返回的站点信息
//        stationIds = (List<String>) responCompleteOrderMap.get("stationIds");
            stationIds.addAll((List<String>) responCompleteOrderMap.get("stationIds"));

            //list去重
            HashSet h = new HashSet(stationIds);
            stationIds.clear();
            stationIds.addAll(h);

            //查询站点坐标
            List<Map<String, Object>> stationList = stationService.queryStationByIds(stationIds);

            //整合算法数据
            Map<String, Object> maasCommonData = orderService.structureMaasCommonData(stationList, unTakeOrderList, completeOrderList, takeOrderList, vehicleList);
            //保存取消的订单
            maasCommonData.put("cancleOrder",null);
            maasCommonData.put("finishedOrderId", null);

            //将查询结转换为json,放到json中
            Map<String, JsonValueProcessor> processors = new HashMap<String, JsonValueProcessor>();
            processors.put("java.util.Date", new UtilDateProcessor("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
            System.out.println("请求数据：" + JSONUtil.toJson(maasCommonData, processors));
            JSONObject jsonObject = HttpClientUtil.httpPost(SysParamConstant.COMMON_URL, JSONUtil.toJson(maasCommonData, processors));
            System.out.println("返回数据："+StringUtil.trim(jsonObject));
            if (!StringUtil.isEmpty(StringUtil.trim(jsonObject))) {
                parseCommonData(jsonObject.toString());
            }
        }

    }


    /**
     * 平峰无实时订单，每隔10秒执行一次计算
     */
    @Scheduled(cron = "*/10 * * * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void maasCommonScheduled() throws Exception {
        log.info("start push data scheduled!");
        Date date = new Date();
        QueryWrapper queryWrapper =new QueryWrapper();
        queryWrapper.select(
                "id","passengers_num","trip_time","start_station_id",
                "end_station_id","user_id","create_date","order_status");
        queryWrapper.isNull("car_id");
        queryWrapper.eq("order_type","1");//预约出行
        queryWrapper.eq("is_pay","1");//已支付
//        queryWrapper.apply("to_char(trip_time,'yyyy-MM-dd')={0}",DateUtils.formatDate(date, Constants.DATE_PATTERN));
        queryWrapper.between("trip_time",date,DateUtils.addMinutes(date,10));
        queryWrapper.eq("order_status","1");//待使用

        //保存未上车订单列表
        List<Map<String,Object>> unTakeOrderList = orderService.listMaps(queryWrapper);
        if(null != unTakeOrderList && unTakeOrderList.size()>0){
            //保存站点Ids
            List<String> stationIds = Lists.newArrayList();
            //查询当前可用的司机
            List<Map<String, Object>> driverList = driverService.listMaps(new QueryWrapper<TDriverDto>()
                    .select("id")
                    .eq("status","1")
            );
            if(driverList==null || driverList.size()==0){
                log.info("当前没有可用的司机......");
                return ;
            }

            List<String> driverIds = Lists.newArrayList();
            driverList.forEach(driver->driverIds.add(StringUtil.trim(driver.get("id"))));
            //查询司机当天对应的车辆信息
            List<Map<String, Object>> driverCarBindLists = driverCarBindService.listMaps(new QueryWrapper<TDriverCarBindDto>()
                    .select("car_id", "driver_id")
                    .in("driver_id",driverIds)
                    .eq("bind_status","1")
            );
            if(driverCarBindLists == null || driverCarBindLists.size() == 0){
                log.info("当前司机没有绑定的车辆......");
                return ;
            }

            List<String> carIds = Lists.newArrayList();
            driverCarBindLists.forEach(driverCar->carIds.add(StringUtil.trim(driverCar.get("car_id"))));
            Map<String,Object> condition = Maps.newHashMap();
            condition.put("carIds",carIds);
            //查询车辆现在所属位置
            Map<String,Object> respMap = carPositionService.calculateVehicleDirectionAngle(condition);
            List<Map<String,Object>> vehicleList = (List<Map<String, Object>>) respMap.get("vehicleList");
            List<String> vehicleIds = (List<String>) respMap.get("vehicleIds");

            //查询车辆订单信息
            Map<String,Object> respTakeOrderMap = orderService.queryTakeOrder(vehicleIds);
            //车上订单
            List<Map<String,Object>> takeOrderList = (List<Map<String, Object>>) respTakeOrderMap.get("takeOrderList");
            //保存站点位置
            stationIds.addAll((List<String>) respTakeOrderMap.get("stationIds"));

            //查询上一次计算已完成的订单
            String maas_common = redisUtil.get("maas_common");
            Map<String, Object> responCompleteOrderMap = null;
            if(StringUtil.isEmpty(maas_common)){
                responCompleteOrderMap = orderService.queryCompleteOrder(DateUtils.addSeconds(date,10),date);
            }else{
                responCompleteOrderMap = orderService.queryCompleteOrder(
                        new Date(Long.valueOf(maas_common)),
                        date
                );
            }
            //返回的已完成订单
            List<Map<String, Object>> completeOrderList = (List<Map<String, Object>>) responCompleteOrderMap.get("completeOrderList");
            //返回的站点信息
            stationIds.addAll((List<String>) responCompleteOrderMap.get("stationIds"));

            //记录未上车订单
            for (Map<String, Object> unTake : unTakeOrderList) {
                unTake.put("orderID",unTake.get("id"));
                unTake.put("ordertime",new Date(((Date)unTake.get("create_date")).getTime()));
                unTake.put("people",unTake.get("passengers_num"));
                unTake.put("starttime",new Date(((Date)unTake.get("trip_time")).getTime()));
                unTake.put("state",unTake.get("order_status"));
                unTake.put("userID",unTake.get("user_id"));
                unTake.put("boardtime",null);//保存上车时间
                unTake.remove("order_status");
                unTake.remove("id");
                unTake.remove("create_date");
                unTake.remove("passengers_num");
                unTake.remove("trip_time");
                unTake.remove("user_id");
                stationIds.add(StringUtil.trim(unTake.get("start_station_id")));
                stationIds.add(StringUtil.trim(unTake.get("end_station_id")));
            }

            //查询站点坐标
            List<Map<String, Object>> stationList = stationService.queryStationByIds(stationIds);

            //整合算法数据
            Map<String,Object> maasCommonData = orderService.structureMaasCommonData(stationList,unTakeOrderList,completeOrderList,takeOrderList,vehicleList);
            //保存取消的订单
            maasCommonData.put("cancleOrder",Maps.newHashMap());
            maasCommonData.put("finishedOrderId", null);
            //将查询结转换为json,放到json中
            Map<String, JsonValueProcessor> processors = new HashMap<String, JsonValueProcessor>();
            processors.put("java.util.Date", new UtilDateProcessor("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
            System.out.println("请求数据："+JSONUtil.toJson(maasCommonData, processors));
            JSONObject jsonObject = HttpClientUtil.httpPost(SysParamConstant.COMMON_URL, JSONUtil.toJson(maasCommonData, processors));

            System.out.println("返回数据："+StringUtil.trim(jsonObject));
            if(!StringUtil.isEmpty(StringUtil.trim(jsonObject))){
                parseCommonData(jsonObject.toString());
            }
        }
        log.info("end push data scheduled!");
    }

    /**
     * 解析平峰算法数据
     */
    private void parseCommonData(String jsonStr){
        if(StringUtil.isEmpty(jsonStr)){
            log.error("平峰算法调度返回结果为空");
        }
        CommonResponse response = (CommonResponse) JsonUtil.getInstance().fromJson(jsonStr, CommonResponse.class);
        //用户派车列表，修改用户订单里的cart
        List<UserVehicle> userVehicleList = response.getUserVehicleList();
        Map<String, List<String>> userStationMap = response.getUserStationMap();
        Date date = new Date();
        //需要推送至司机
        //车辆路线列表
        List<RouteListObj> vehicleRouteList = response.getVehicleRouteList();
        //记录车辆对应的线路ID
        Map<String,String> car_route_map = Maps.newHashMap();
        Map<String,List<Map<String,Object>>> routeMap = Maps.newHashMap();
        //新调整
        Map<String,List<Map<Object,Object>>> newRouteMap = Maps.newHashMap();
        if(null!=vehicleRouteList && vehicleRouteList.size()>0){

            List<String> vehicleIds = Lists.newArrayList();
            for (RouteListObj route : vehicleRouteList) {
                vehicleIds.add(route.getVehicleID());
            }
            List<Map<String,Object>> carBindList  = driverCarBindService.listMaps(new QueryWrapper<TDriverCarBindDto>()
                    .select("driver_id", "car_id")
                    .eq("bind_status", "1")
                    .in("car_id", vehicleIds)
            );

            //查询今天内未完成的平峰行程
            List<TRouteOffpeakDto> unFinishRouteList = routeOffpeakService.list(new QueryWrapper<TRouteOffpeakDto>()
                    .select("id","car_id","midway_station","curr_station")
                    .eq("route_state", "0")
                    .apply("to_char(create_date,'yyyy-MM-dd')={0}",DateUtils.formatDate(date, Constants.DATE_PATTERN))
                    .in("car_id", vehicleIds)
            );

            List<String> linkList = null;
            String stations = redisUtil.get(RedisConstant.SYS_STATIONS_CACHE_KEY);
            List<Map<Object, Object>> cacheStationList = JsonUtil.parseJSON2List(stations);

            //插入新的平峰行程
            ArrayList<TRouteOffpeakDto> createRouteOffpeakList = Lists.newArrayList();
            //保存要修改的平峰行程
            ArrayList<TRouteOffpeakDto> updateRouteOffpeakList = Lists.newArrayList();
            TRouteOffpeakDto routeOffpeakDto = null;
            //过滤掉未完成的行程,并将link数据保存
            for (RouteListObj route : vehicleRouteList) {
                //获取站点列表
                ArrayList<String> stationList = route.getStationList();

                routeOffpeakDto = new TRouteOffpeakDto();
                linkList = Lists.newArrayList();
                String vehicleID = route.getVehicleID();
                boolean isFind = false;
                String routeId = "";
                //如果车辆有未完成的行程 ，则记录线路id
                for (TRouteOffpeakDto unFinishRoute : unFinishRouteList) {
                    if(unFinishRoute.getCarId().equals(vehicleID)){
                        isFind = true;
                        //记录车辆对应的线路ID
                        routeId = unFinishRoute.getId();
                        car_route_map.put(vehicleID,routeId);
                        routeOffpeakDto = unFinishRoute;
                    }
                }

                //封装前端需要的数据
                Map<String, ArrayList<String>> linkMap = route.getLinkMap();
                String vehicleId = vehicleID;

                List<Map<Object,Object>> bussNavigationData = Lists.newArrayList();
                for (String station : stationList) {
                    for (Map<Object, Object> cache : cacheStationList) {
                        String id = StringUtil.trim(cache.get("id"));
                        if(station.equals(id)){
                            bussNavigationData.add(cache);
                            break;
                        }
                    }
                    ArrayList<String> strings = linkMap.get(vehicleId + "," + station);
                    vehicleId = station;
//                    linkList.addAll(strings);
                    linkList.addAll(strings.subList(1,strings.size()-1));
                }

                newRouteMap.put(vehicleID,bussNavigationData);

                //查询所有的link信息
                List<Map<String,Object>> linkInfoList = linkService.listMaps(
                        new QueryWrapper<TLinkDto>()
                                .select("link_id","from_node","to_node")
                                .in("link_id",linkList)
                );

                System.out.println("查询的link数据："+linkInfoList.toString());

                List<String> linkPosition = Lists.newArrayList();
                //
                int len = linkInfoList.size()-1;
                for (int i = 0 ;i <= len ;i++) {
                    Map<String, Object> link = linkInfoList.get(i);
                    String from_node = StringUtil.trim(link.get("from_node"));
                    if( i == len ){
                        linkPosition.add(from_node);
                        String end_node = StringUtil.trim(link.get("end_node"));
                        linkPosition.add(end_node);
                    }else{
                        link.remove("to_node");
                        linkPosition.add(from_node);
                    }
                }

                //查询node节点信息
                List<Map<String,Object>> nodeList = nodeService.listMaps(new QueryWrapper<TNodeDto>()
                        .in("node_id", linkPosition)
                );
                Map<String, Map<String, Object>> idToLinkMap =Maps.newHashMap();
                for (int i = 0 ;i <= len ;i++) {
                    Map<String, Object> link = linkInfoList.get(i);
                    String link_id = StringUtil.trim(link.get("link_id"));
                    Map<String, Object> newlink = null;
                    String from_node = StringUtil.trim(link.get("from_node"));
                    String to_node = StringUtil.trim(link.get("to_node"));
                    for (Map<String, Object> node : nodeList) {
                        String node_id = StringUtil.trim(node.get("node_id"));
                        if(node_id.equals(from_node)){
                            link.putAll(node);
                            break;
                        }
                        if(node_id.equals(to_node)){
                            newlink = Maps.newHashMap();
                            newlink.putAll(link);
                            newlink.putAll(node);
                        }
                    }
                    if(i == len){
                        linkInfoList.add(newlink);
                    }else{
                        idToLinkMap.put(link_id,link);
                    }
                }

                //矫正顺序
                List<Map<String,Object>> newlinkInfoList = Lists.newArrayList();
                for (String link : linkList) {
                    Map<String, Object> map = idToLinkMap.get(link);
                    if(map!= null && map.size()>0){
                        newlinkInfoList.add(map);
                    }

                }
                Map<String, Object> map = linkInfoList.get(linkInfoList.size() - 1);
                if(map!= null && map.size() >0){
                    newlinkInfoList.add(map);
                }
                //返回司机端的导航数据，交通要求用站点进行导航
                routeMap.put(vehicleID,newlinkInfoList);


                //如果没有未完成的行程则新增;有未完成的信息则修改行程途经站点及结束站点
                if(!isFind){
                    routeOffpeakDto.setCarId(vehicleID);
                    routeOffpeakDto.setCreateDate(date);
                    routeOffpeakDto.setStartDate(date);
                    routeOffpeakDto.setRouteState("0");
                    //新添加
                    routeOffpeakDto.setCurrStation("-1");
                    routeOffpeakDto.setMidwayStation(StringUtils.join(stationList, ","));
                    routeOffpeakDto.setAllPosition(JsonUtil.object2Json(bussNavigationData));
//                    routeOffpeakDto.setAllPosition(JsonUtil.object2Json(newlinkInfoList));
                    for (int i = carBindList.size()-1 ; i>= 0 ;i--) {
                        Map<String, Object> carBindMap = carBindList.get(i);
                        if (vehicleID.equals(StringUtil.trim(carBindMap.get("car_id")))) {
                            //站点信息为未完成
                            routeOffpeakDto.setDriverUserId(StringUtil.trim(carBindMap.get("driver_id")));
                            carBindList.remove(carBindMap);
                            break;
                        }
                    }
                    createRouteOffpeakList.add(routeOffpeakDto);
                }else{
                    //修改行程途经站点
                    routeOffpeakDto.setId(routeId);
                    //当前站点
                    String currStation = routeOffpeakDto.getCurrStation();
                    String beforePosition = routeOffpeakDto.getAllPosition();
                    if(currStation.equals("-1")){
                        routeOffpeakDto.setMidwayStation(StringUtils.join(stationList, ","));
                    }else{
                        //获取上一次的
                        String midwayStation = routeOffpeakDto.getMidwayStation();
                        String[] split = midwayStation.split(",");
                        List<String> strings = Arrays.asList(split);
                        int asInt = StringUtil.getAsInt(currStation);
                        List<String> list = null;
                        if(asInt == 0 ){
                            list = Lists.newArrayList();
                            list.add(strings.get(0));
                        }else{
                            list = new ArrayList(strings.subList(0,asInt+1));
                        }
                        list.addAll(stationList);
                        routeOffpeakDto.setMidwayStation(StringUtils.join(list, ","));
                    }
                    routeOffpeakDto.setUpdateDate(date);
                    String newPosition = JsonUtil.object2Json(bussNavigationData);
                    routeOffpeakDto.setAllPosition(newPosition);
                    if(beforePosition.equals(newPosition)){
                        newRouteMap.remove(routeOffpeakDto.getCarId());
                    }
                    updateRouteOffpeakList.add(routeOffpeakDto);
                }

            }


            //保存新增的平峰行程
            if(null != createRouteOffpeakList && createRouteOffpeakList.size()>0){
                boolean saveBatch = routeOffpeakService.saveBatch(createRouteOffpeakList);
                for (TRouteOffpeakDto tRouteOffpeakDto : createRouteOffpeakList) {
                    car_route_map.put(tRouteOffpeakDto.getCarId(),tRouteOffpeakDto.getId());
                }
            }

            if(null != updateRouteOffpeakList && updateRouteOffpeakList.size()>0){
                routeOffpeakService.batchUpdateRouteOffpeak(updateRouteOffpeakList);
            }


            //修改用户订单信息
            if(null!=userVehicleList && userVehicleList.size()>0){
                List<TOrderDto> orderList = Lists.newArrayList();
                TOrderDto order = null;
                for (UserVehicle userVehicle : userVehicleList) {
                    order = new TOrderDto();
                    String orderID = userVehicle.getOrderID();
                    String vehicleID = userVehicle.getVehicleID();
                    //获取用户行驶路线
                    List<String> userStations = userStationMap.get(orderID);
                    //获取车辆的行驶路线
                    List<Map<String, Object>> linkInfoList = routeMap.get(vehicleID);
                    order.setId(orderID);
                    order.setUpdateDate(date);
                    order.setCarId(vehicleID);
                    order.setRouteId(car_route_map.get(vehicleID));
                    order.setAllotDate(date);
                    for (int i = carBindList.size()-1 ; i>= 0 ;i--) {
                        Map<String, Object> carBindMap = carBindList.get(i);
                        if (vehicleID.equals(StringUtil.trim(carBindMap.get("car_id")))) {
                            //站点信息为未完成
                            order.setDriverId(StringUtil.trim(carBindMap.get("driver_id")));
                            break;
                        }
                    }
                    //只截取订单所需的行驶路径
                    int start = 0;
                    int end = -1;
                    for (int i = 0;i<linkInfoList.size();i++) {
                        Map<String, Object> link = linkInfoList.get(i);
                        String link_id = StringUtil.trim(link.get("link_id"));
                        if(link_id.equals(userStations.get(1))){
                            start = i;
                        }
                        if(link_id.equals(userStations.get(userStations.size()-2))){
                            end = i;
                            break;
                        }
                    }
                    if(end == -1){
                        end =linkInfoList.size()-2;
                    }
                    List<Map<String, Object>> newlinkInfo = linkInfoList.subList(start, end + 1);
                    System.out.println("C端画线数据："+JsonUtil.object2Json(newlinkInfo));
                    order.setOrderPosition(JsonUtil.object2Json(newlinkInfo));
                    orderList.add(order);
                }
                if(null != orderList && orderList.size()>0){
                    orderService.updateBatchById(orderList);
                }
            }
            redisUtil.set("maas_common",date.getTime(), (long) (60*60*24));
            kafkaTemplate.send(MessageConstant.BUSS_ROUTE_NAVIGATION_TOPIC_MESSAGE, JsonUtil.toJson(newRouteMap));
        }
    }


    /**
     * 高峰，每隔10秒执行一次计算
     */
    @Scheduled(cron = "*/10 * 20-22 * * ?")
//    @Scheduled(cron = "*/10 31-59 7-8 * * *")
    @Transactional(rollbackFor = Exception.class)
    public void maasPeakScheduled1() throws Exception {
        log.info("start maasPeakScheduled!");
        executeMaasPeak();
        log.info("end maasPeakScheduled!");
    }

    @Scheduled(cron = "*/10 * 8-10 * * *")
//    @Scheduled(cron = "*/10 0-30 8-9 * * *")
    @Transactional(rollbackFor = Exception.class)
    public void maasPeakScheduled2() throws Exception {
        log.info("start maasPeakScheduled!");
        executeMaasPeak();
        log.info("end maasPeakScheduled!");
    }


    /**
     * 执行高峰算法
     * @throws Exception
     */
    public void executeMaasPeak() throws Exception{

        //处理步骤   查询可用的司机
        List<Map<String, Object>> driverList = driverService.listMaps(new QueryWrapper<TDriverDto>()
            .select("id")
            .eq("status","1")
        );

        if(driverList==null || driverList.size()==0){
            log.info("==执行高峰算法== 当前没有可用的司机......");
            return ;
        }

        //查询高峰线路信息
        Map<String,Object> condition = Maps.newHashMap();
        condition.put("lineState","1");//
        List<Map<String,Object>> routePeakLineList = routePeakService.queryRouteLineList(condition);
        if(null ==routePeakLineList || routePeakLineList.size()==0){
            log.info("==执行高峰算法== 当前没有可用的高峰线路......");
            return ;
        }

        //保存线路ids
        List<String> lineIds = Lists.newArrayListWithCapacity(routePeakLineList.size());
        for (Map<String, Object> routePeak : routePeakLineList) {
            lineIds.add(StringUtil.trim(routePeak.get("line_id")));
        }

        //根据线路ids查询司机信息
        List<Map<String, Object>> lineDriverBindLists = lineDriverBindService.listMaps(new QueryWrapper<TLineDriverBindDto>()
                .select("driver_id","line_id")
                .in("line_id", lineIds)
        );

        List<String> driverIds = Lists.newArrayList();
        for (Map<String, Object> lineDriverBind : lineDriverBindLists) {
            String driver_id = StringUtil.trim(lineDriverBind.get("driver_id"));
            for (Map<String, Object> dirver : driverList) {
                if(driver_id.equals(StringUtil.trim(dirver.get("id")))){
                    driverIds.add(driver_id);
                }
            }
        }
        if(driverIds.size()==0){
            log.info("==执行高峰算法== 当前没有可用的司机......");
            return ;
        }

        //查询司机当天对应的车辆信息
        List<Map<String, Object>> driverCarBindLists = driverCarBindService.listMaps(new QueryWrapper<TDriverCarBindDto>()
                .select("car_id", "driver_id")
                .in("driver_id",driverIds)
                .eq("bind_status","1")
        );

        if(driverCarBindLists == null || driverCarBindLists.size() == 0){
            log.info("==执行高峰算法== 当前司机没有绑定的车辆......");
            return ;
        }
        //封装线路所有运营的司机及车辆信息
        for (Map<String, Object> driverCarBind : driverCarBindLists) {
            String driverId = StringUtil.trim(driverCarBind.get("driver_id"));
            for (Map<String, Object> lineDriverBind : lineDriverBindLists) {
                String driver_id = StringUtil.trim(lineDriverBind.get("driver_id"));
                if(driverId.equals(driver_id)){
                    driverCarBind.put("line_id",StringUtil.trim(lineDriverBind.get("line_id")));
                }
            }
        }

        //查询车辆现在所属位置
        Map<String,Object> respMap = carPositionService.calculateVehicleDirectionAngle();
        List<Map<String,Object>> vehicleList = (List<Map<String, Object>>) respMap.get("vehicleList");
        if(vehicleList!= null && vehicleList.size()>0){

            //查询车辆的行驶线路

            //将车辆当前位置与线路进行绑定
            for (int i = vehicleList.size()-1 ;i>= 0; i--) {
                Map<String, Object> vehicle = vehicleList.get(i);
                String vehicleID = StringUtil.trim(vehicle.get("vehicleID"));
                vehicle.put("index",0);
                //查询司机当天对应的车辆信息
                String line_id = null;
                for (Map<String, Object> driverCarBind : driverCarBindLists) {
                    String car_id = StringUtil.trim(driverCarBind.get("car_id"));
                    if(vehicleID.equals(car_id)){
                        line_id = StringUtil.trim(driverCarBind.get("line_id"));
                        vehicle.put("lineID",line_id);
                        vehicle.put("driver_id",StringUtil.trim(driverCarBind.get("driver_id")));
                        break;
                    }
                }
                if(line_id!=null){
                    String route_id = null;
                    for (Map<String, Object> route : routePeakLineList) {
                        String lineId = StringUtil.trim(route.get("line_id"));
                        String driverId = StringUtil.trim(route.get("driver_user_id"));
                        String driver_id = StringUtil.trim(vehicle.get("driver_id"));
                        if(driver_id.equals(driverId)){
//                        if(lineId.equals(line_id)){
                            route_id = StringUtil.trim(route.get("route_id"));
                            vehicle.put("route_id",route_id);
                            break;
                        }
                    }

                    //查询当前车辆已经停靠的站点
                    TRouteStationDto station = routeStationService.getOne(new QueryWrapper<TRouteStationDto>()
                            .eq("route_id",route_id)
//                            .isNull("on_num")
                            .orderByDesc("create_date")
                            .last("limit 1")
                    );
                    if(station!=null){
                        String stationId = station.getStationId();
                        TViaDto one = viaService.getOne(new QueryWrapper<TViaDto>()
                                .eq("line_id", line_id)
                                .eq("station_id", stationId)
                        );
                        if(one!=null){
                            vehicle.put("index",one.getIndex());
                        }
                    }
                }else{
                    vehicleList.remove(i);
                }
            }
        }

        //将查询结转换为json,放到json中
        Map<String, JsonValueProcessor> processors = new HashMap<String, JsonValueProcessor>();
        processors.put("java.util.Date", new UtilDateProcessor("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
        System.out.println("高峰请求数据："+JSONUtil.toJsonArray(vehicleList));

        JSONArray jsonObject = HttpClientUtil.httpPost(SysParamConstant.PEAK_URL, JSONUtil.toJsonArray(vehicleList));

        System.out.println("高峰返回数据："+StringUtil.trim(jsonObject));
        if(!StringUtil.isEmpty(StringUtil.trim(jsonObject))){
            parsePeakData(jsonObject.toString(),vehicleList);
        }
    }


    /**
     *解析高峰算法
     * @param jsonStr
     */
    private void parsePeakData(String jsonStr,List<Map<String,Object>> vehicleList) throws Exception {
        if(StringUtil.isEmpty(jsonStr)){
            log.error("高峰算法调度返回结果为空");
            return ;
        }
        List<Map<Object, Object>> maps = JsonUtil.parseJSON2List(jsonStr);
        if(maps.size()==0){
            log.error("高峰算法调度返回结果为空");
            return;
        }

        //保存线路集合
        List<String> lineIds = Lists.newArrayList();
        //保存车辆集合
        List<String> vehicleIDs = Lists.newArrayList();
        for (Map<Object, Object> map : maps) {
            String vehicleId = StringUtil.trim(map.get("vehicleId"));
            vehicleIDs.add(vehicleId);
            for (Map<String, Object> vehicle : vehicleList) {
                String vehicleId1 = StringUtil.trim(vehicle.get("vehicleID"));
                if(vehicleId.equals(vehicleId1)){
                    String lineID = StringUtil.trim(vehicle.get("lineID"));
                    map.put("lineID",lineID);
                    lineIds.add(lineID);
                    break;
                }
            }
        }

        //查询车辆信息
        List<Map<String, Object>> carMapLists = carService.listMaps(new QueryWrapper<TCarDto>()
                .select("id", "car_no")
                .in("id", vehicleIDs)
        );

        for (Map<Object, Object> map : maps) {
            String vehicleId = StringUtil.trim(map.get("vehicleId"));
            for (Map<String, Object> carMapList : carMapLists) {
                String id = StringUtil.trim(carMapList.get("id"));
                if(vehicleId.equals(id)){
                    map.put("car_no",carMapList.get("car_no"));
                    break;
                }
            }
        }

        //查询高峰线路的途经站点表
        List<Map<String, Object>> lineViaList = viaService.listMaps(new QueryWrapper<TViaDto>()
                .select("id via_id","line_id","index","station_id","type")
                .in("line_id", lineIds)
        );

        //查询缓存的站点信息
        String stations = redisUtil.get(RedisConstant.SYS_STATIONS_CACHE_KEY);
        List<Map<Object, Object>> stationList = JsonUtil.parseJSON2List(stations);

        for (Map<Object, Object> map : maps) {
            //车辆id
            String vehicleID = StringUtil.trim(map.get("vehicleId"));
            String lineID = StringUtil.trim(map.get("lineID"));
            //线路id
            List<Map<String,Object>> viaList = new ArrayList<>();
            for (Map<String, Object> lineVia : lineViaList) {
                if(lineID.equals(lineVia.get("line_id"))){
                    viaList.add(lineVia);
                }
            }
            viaList.sort((a,b) -> StringUtil.getAsInt(StringUtil.trim(a.get("index"))) - StringUtil.getAsInt(StringUtil.trim(b.get("index"))));
            //添加站点信息
            for (Map<String, Object> via : viaList) {
                String station_id = StringUtil.trim(via.get("station_id"));
                for (Map<Object, Object> station : stationList) {
                    String id = StringUtil.trim(station.get("id"));
                    if(station_id.equals(id)){
                        via.put("site_name",StringUtil.trim(station.get("site_name")));
                        via.put("longitude",StringUtil.trim(station.get("longitude")));
                        via.put("latitude",StringUtil.trim(station.get("latitude")));
                        via.put("type",StringUtil.trim(station.get("type")));
                    }
                }
            }
            //将车辆线路信息推送给司机端导航
            redisUtil.set("maas_peak_station"+vehicleID,JsonUtil.list2Json(viaList), (long) (60*20));
            map.put("viaStations",viaList);

            String lineCacheKey = redisUtil.get("maas_arrival_time_of_vehicles" + lineID);
            List<Map<Object, Object>> linesList  = null;
            if(StringUtil.isEmpty(lineCacheKey)){
                linesList = Lists.newArrayList();
                linesList.add(map);
            }else{
                linesList = JsonUtil.parseJSON2List(lineCacheKey);
                boolean isAdd = true;
                for (Map<Object, Object> line : linesList) {
                    if(StringUtil.trim(line.get("vehicleId")).equals(vehicleID)){
                        isAdd = false;
                        if(map.get("stationArivalTime") != null){
                            line.put("stationArivalTime",map.get("stationArivalTime"));
                        }
                        break;
                    }
                }
                if(isAdd){
                    linesList.add(map);
                }
            }
            redisUtil.set("maas_arrival_time_of_vehicles"+lineID,JsonUtil.toJson(linesList), (long) (60*20));
        }

        System.out.println(maps);
    }



    /**
     *解析高峰算法（copy）
     * @param jsonStr
     */
    private void parsePeakData_copy(String jsonStr,List<Map<String,Object>> vehicleList) throws Exception {
        if(StringUtil.isEmpty(jsonStr)){
            log.error("高峰算法调度返回结果为空");
            return ;
        }
        List<Map<Object, Object>> maps = JsonUtil.parseJSON2List(jsonStr);
        if(maps.size()==0){
            log.error("高峰算法调度返回结果为空");
            return;
        }
        Date nowTime = new Date();
        //保存线路集合
        List<String> lineIds = Lists.newArrayList();
        //保存车辆集合
        List<String> vehicleIDs = Lists.newArrayList();
        for (Map<Object, Object> map : maps) {
            String vehicleId = StringUtil.trim(map.get("vehicleId"));
            vehicleIDs.add(vehicleId);
            int arrivaltime= (int) Math.ceil(StringUtil.getDouble(StringUtil.trim(map.get("arrivaltime"))));
            for (Map<String, Object> vehicle : vehicleList) {
                String vehicleId1 = StringUtil.trim(vehicle.get("vehicleID"));
                if(vehicleId.equals(vehicleId1)){
                    vehicle.put("arrivaltime",arrivaltime);
                }
            }
        }

        //查询车辆信息
        List<Map<String, Object>> carMapLists = carService.listMaps(new QueryWrapper<TCarDto>()
                .select("id", "car_no")
                .in("id", vehicleIDs)
        );

        for (Map<String, Object> vehicle : vehicleList) {
            String vehicleId = StringUtil.trim(vehicle.get("vehicleID"));
            lineIds.add(StringUtil.trim(vehicle.get("lineID")));
            for (Map<String, Object> carMapList : carMapLists) {
                String id = StringUtil.trim(carMapList.get("id"));
                if(vehicleId.equals(id)){
                    vehicle.put("car_no",carMapList.get("car_no"));
                    break;
                }
            }
        }



        //查询高峰线路的途经站点表
        List<Map<String, Object>> lineViaList = viaService.listMaps(new QueryWrapper<TViaDto>()
                .select("id via_id","line_id","index","station_id","type")
                .in("line_id", lineIds)
        );

        //保存车辆对应的行驶路线
        Map<String,Object> lineViaMap = Maps.newHashMap();

        //查询缓存的站点信息
        String stations = redisUtil.get(RedisConstant.SYS_STATIONS_CACHE_KEY);
        List<Map<Object, Object>> stationList = JsonUtil.parseJSON2List(stations);


        for (Map<String, Object> vehicle : vehicleList) {
            //车辆id
            String vehicleID = StringUtil.trim(vehicle.get("vehicleID"));
            String lineID = StringUtil.trim(vehicle.get("lineID"));
            if(!redisUtil.exists("maas_peak_station"+vehicleID)){
                //线路id
                List<Map<String,Object>> viaList = new ArrayList<>();
                for (Map<String, Object> lineVia : lineViaList) {
                    if(lineID.equals(lineVia.get("line_id"))){
                        viaList.add(lineVia);
                    }
                }
                viaList.sort((a,b) -> StringUtil.getAsInt(StringUtil.trim(a.get("index"))) - StringUtil.getAsInt(StringUtil.trim(b.get("index"))));
                //添加站点信息
                for (Map<String, Object> via : viaList) {
                    String station_id = StringUtil.trim(via.get("station_id"));
                    for (Map<Object, Object> station : stationList) {
                        String id = StringUtil.trim(station.get("id"));
                        if(station_id.equals(id)){
                            via.put("site_name",StringUtil.trim(station.get("site_name")));
                            via.put("longitude",StringUtil.trim(station.get("longitude")));
                            via.put("latitude",StringUtil.trim(station.get("latitude")));
                            via.put("type",StringUtil.trim(station.get("type")));
                        }
                    }
                }
                //将车辆线路信息推送给司机端导航
                redisUtil.set("maas_peak_station"+vehicleID,JsonUtil.list2Json(viaList), (long) (60*20));
            }
            //查询缓存是否存在，存在则添加进去
            String orderLineCache = redisUtil.get(RedisConstant.MAAS_PEAK_USER_ORDER_LINE_VALUE + lineID);
            List<Map<Object, Object>> userLineInfoList = null;
            Map<Object,Object> userLineInfo = Maps.newHashMap();
            userLineInfo.put("arrivaltime",DateUtils.addMinutes(nowTime, (Integer) vehicle.get("arrivaltime")).getTime());
            userLineInfo.put("vehicleID",vehicleID);
            //保存车辆当前的位置，下一次
            userLineInfo.put("index",StringUtil.getAsInt(StringUtil.trim(vehicle.get("index")))+1);
            userLineInfo.put("car_no",vehicle.get("car_no"));
            if(StringUtil.isEmpty(orderLineCache)){
                userLineInfoList = Lists.newArrayList();
                userLineInfoList.add(userLineInfo);
            }else{
                userLineInfoList = JsonUtil.parseJSON2List(orderLineCache);
                boolean isnotFind = true;
                for (Map<Object, Object> userLine : userLineInfoList) {
                    String vehicleID1 = StringUtil.trim(userLine.get("vehicleID"));
                    if(vehicleID.equals(vehicleID1)){
                        isnotFind = false;
                        userLine.putAll(userLineInfo);
                        break;
                    }
                }
                if(isnotFind){
                    userLineInfoList.add(userLineInfo);
                }
            }


            //保存用户高峰订单线路最近的车辆信息（高峰下单显示车辆最近到达时间）
            redisUtil.set(RedisConstant.MAAS_PEAK_USER_ORDER_LINE_VALUE+lineID,JSONUtil.toJsonArray(userLineInfoList) , (long) (60*5));

//            lineViaMap.put(vehicleID,viaList);
        }

//        kafkaTemplate.send(MessageConstant.BUSS_ROUTE_NAVIGATION_TOPIC_MESSAGE, JsonUtil.toJson(lineViaMap));

        System.out.println("jsonStr:"+jsonStr);
    }



    /**
     * 将当天高峰订单设置为失效
     */
    @Scheduled(cron = "0 15 2 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void updatePeakOrder(){
        QueryWrapper<TOrderDto> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("order_status","1");
//        queryWrapper.eq("route_type","1");
       List<TOrderDto> orderDtoList =orderService.list(queryWrapper);
       if (orderDtoList.size()>0){
           orderDtoList.forEach(order->order.setOrderStatus("4"));
           orderService.updateBatchById(orderDtoList);
       }
    }

    /**
     * 将车辆位置写入文件
     */
    @Scheduled(cron = " 0 50 23 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void saveTxt() throws IOException, ParseException {
        SimpleDateFormat dfName = new SimpleDateFormat("yyyy-MM-dd");
        String name = dfName.format(new Date()).replace("-","/");
        //创建文件夹
        File file = new File("/var"+name);
        if(!file.exists()){
            file.mkdirs();
        }
        //分成288个文件夹
        SimpleDateFormat sdf= new SimpleDateFormat("HH:mm");
        String str="00:00";
        List<Map<String,String>> mapList = new ArrayList<>();
        for (int i = 1; i < 289; i++) {
            //文件名及类型
            String fileName = String.format("%03d", i)+".txt";
            File fileTxt = new File(file, fileName);
            fileTxt.createNewFile();
            Date  dt = sdf.parse(str);
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.MINUTE,5);
            Date dt1=rightNow.getTime();
            Map<String,String> map = new HashMap(289);
            map.put(String.format("%03d", i),dt.getTime()+","+ dt1.getTime());
            mapList.add(map);
            String dt2=dt1.toString().substring(11,16);
            str=dt2;
        }
        //查询车辆位置信息
        QueryWrapper<TCarPositionDto> queryWrapper =new QueryWrapper<>();
        queryWrapper.apply("to_char(create_date,'yyyy-MM-dd')={0}",dfName.format(new Date()));
        queryWrapper.orderByAsc("create_date");
       List<TCarPositionDto> tCarPositionDtoList = carPositionService.list(queryWrapper);
       if (tCarPositionDtoList.size()<1){
           return ;
       }
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd,HHmmss");
       //列表比较，插入哪个文件夹
        for (TCarPositionDto tCarPositionDto : tCarPositionDtoList) {
            Long   cerateDate = sdf.parse(sdf.format(new Date(tCarPositionDto.getCreateDate().getTime()))).getTime();
            for (int i = 0; i<mapList.size(); i++) {
               Map<String,String> map = mapList.get(i);
             String date =  map.get(String.format("%03d", i+1));
            String[] strings= date.split(",");
            Long startTime =Long.valueOf(strings[0]);
            Long endTime =Long.valueOf(strings[1]);
             if (startTime<cerateDate&&cerateDate<endTime){
                 //写入内容

                 String content = df.format(tCarPositionDto.getCreateDate()).split(",")[0]+"，"+ df.format(tCarPositionDto.getCreateDate()).split(",")[1]+"，"+carService.getById(tCarPositionDto.getCarId()).getCarNo()+ "，" + tCarPositionDto.getLongitude()+"，"+tCarPositionDto.getLatitude();
                 BufferedWriter out = null;
                 //设置文件名
                 String fileName = String.format("%03d", i+1)+".txt";
                 try {
                     out = new BufferedWriter(new OutputStreamWriter(
                             new FileOutputStream(file+"\\"+fileName, true)));
                     out.write(content+"\r\n");
                 } catch (Exception e) {
                     e.printStackTrace();
                 } finally {
                     try {
                         out.close();
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                 }
             }
            }
        }
    }

}
