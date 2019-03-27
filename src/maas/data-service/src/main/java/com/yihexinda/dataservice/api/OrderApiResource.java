package com.yihexinda.dataservice.api;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yihexinda.core.abst.AbstractPageTemplate;
import com.yihexinda.core.constants.*;
import com.yihexinda.core.param.QRCodeParam;
import com.yihexinda.core.response.CommonResponse;
import com.yihexinda.core.response.RouteListObj;
import com.yihexinda.core.response.UserVehicle;
import com.yihexinda.core.vo.PayLoadVo;
import com.yihexinda.data.dto.*;
import com.yihexinda.dataservice.dao.TRouteOffpeakDao;
import com.yihexinda.dataservice.pay.MyConfig;
import com.yihexinda.core.utils.*;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.dataservice.json.JSONUtil;
import com.yihexinda.dataservice.json.UtilDateProcessor;
import com.yihexinda.dataservice.scheduler.Scheduler;
import com.yihexinda.dataservice.service.*;
import com.yihexinda.dataservice.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import net.sf.json.processors.JsonValueProcessor;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author pengfeng
 * @version 1.0
 * @date 2018/12/1 0028
 */
@RestController
@RequestMapping("/order/client")
@Slf4j
public class OrderApiResource {

    private static final SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

    public static WXPay wxpay;

    static {
        MyConfig config = null;
        try {
            config = new MyConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
        wxpay = new WXPay(config);
    }

    @Resource
    private TOrderService orderService;
    @Autowired
    private TPaySerialService paySerialService;
    @Autowired
    private TCarPositionService carPositionService;
    @Autowired
    private TStationService stationService;
    @Autowired
    private TOrderRefundsService orderRefundsService;
    @Autowired
    private TRouteOffpeakService routeOffpeakService;
    @Autowired
    private TRoutePeakService routePeakService;
    @Autowired
    private TDriverCarBindService driverCarBindService;
    @Autowired
    private TLinkService linkService;
    @Autowired
    private TNodeService nodeService;
    @Autowired
    private TRouteStationService routeStationService;
    @Autowired
    private TDriverService driverService;
    @Autowired
    private TCarService carService;
    @Autowired
    private TCarDeviceService carDeviceService;
    @Autowired
    private TLineDriverBindService lineDriverBindService;

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private TCompanyInfoService tCompanyInfoService;

    @Autowired
    private TUserService tUserService;

    /**
     * pengFeng
     * 获取订单信息列表
     *
     * @param condition map参数
     * @return 订单信息列表
     */
    @PostMapping("/getOrderList")
    public ResultVo getOrderList(@RequestBody Map<String, Object> condition) {
        int pageIndex = StringUtil.getAsInt(StringUtil.trim(condition.get("pageIndex")), 1);
        int pageSize = StringUtil.getAsInt(StringUtil.trim(condition.get("pageSize")), 10);
        if (!"".equals(StringUtil.trim(condition.get("startTime")))&&!"".equals(condition.get("endTime"))){
            Timestamp aStartTime = Timestamp.valueOf(StringUtil.trim(condition.get("startTime")));
            String endTime = StringUtil.trim(condition.get("endTime")).replace("00:00:00","23:59:59");
            Timestamp aEndTime = Timestamp.valueOf(endTime);
            condition.put("aStartTime", aStartTime);
            condition.put("aEndTime", aEndTime);
        }
        ResultVo resultVo = new AbstractPageTemplate<Map>() {
            @Override
            protected List<Map> executeSql() {
                List<Map> list = orderService.getOrderList(condition);
                return list;
            }
        }.preparePageTemplate(pageIndex, pageSize);
        return resultVo;
        //return ResultVo.success().setDataSet(orderService.getOrderList());
    }

    /**
     * pengFeng
     * 获取订单信息列表
     *
     * @return 订单信息列表
     */
    @GetMapping("/getOrderExcel")
    public List<Map> getOrderExcel() {
        return orderService.getOrderList(null);
        //return ResultVo.success().setDataSet(orderService.getOrderList());
    }

    /**
     * pengFeng
     * 根据id获取订单详情
     *
     * @param id 订单id
     * @return tOrderDto 订单信息
     */
    @PostMapping("/getOrder/{id}")
    public ResultVo getOrder(@PathVariable String id) {
        Map order = orderService.getOrder(id);
        if(order == null){
            log.info("订单详情......");
            return ResultVo.error(ResultVo.Status.SYS_ORDER_DATA_ERROR);
        }
        Map<String,Object> condition = Maps.newHashMap();
        //行程类型( 0 平峰 1高峰 )
        String route_type = StringUtil.trim(order.get("route_type"));
        if(route_type.equals("1")){
            //根据线路ID 显示所有车辆位置
            String line_id = StringUtil.trim(order.get("line_id"));
            List<Map<String, Object>> lineDriverBindLists = lineDriverBindService.listMaps(new QueryWrapper<TLineDriverBindDto>()
                    .select("driver_id","line_id")
                    .eq("line_id", line_id)
            );
            if(lineDriverBindLists != null && lineDriverBindLists.size()>0){
                List<String> driverIds = Lists.newArrayList();
                lineDriverBindLists.forEach(lineDriverBind -> driverIds.add(StringUtil.trim(lineDriverBind.get("driver_id"))));
                //查询可用的司机
                List<Map<String, Object>> driverList = driverService.listMaps(new QueryWrapper<TDriverDto>()
                        .select("id")
                        .eq("status","1")
                        .in("id",driverIds)
                );

                if(driverList != null && driverList.size()>0){
                    //查询司机与车辆绑定
                    //查询司机当天对应的车辆信息
                    List<Map<String, Object>> driverCarBindLists = driverCarBindService.listMaps(new QueryWrapper<TDriverCarBindDto>()
                            .select("car_id", "driver_id")
                            .in("driver_id",driverIds)
                            .eq("bind_status","1")
                    );
                    if(driverCarBindLists != null && driverCarBindLists.size()>0){
                        List<String> carIds = Lists.newArrayList();
                        driverCarBindLists.forEach(driverCarBind->carIds.add(StringUtil.trim(driverCarBind.get("car_id"))));

                        //查询车辆对应的位置
                        condition.put("carIds",carIds);
                        List<Map<String, Object>> maps = carPositionService.selectCurCarPostion(condition);
                        order.put("carPosition",maps);

                    }else{
                        log.info("司机当天没有对应的车辆信息......"+driverIds.toString());
                    }
                }else{
                    log.info("当前没有可用的司机......"+driverList.toString());
                }
            }
        }else{
            String car_id = StringUtil.trim(order.get("car_id"));
            if(!StringUtil.isEmpty(car_id)){
                condition.clear();
                condition.put("car_id",car_id);
                Map<String, Object> carPositionMap = carPositionService.selectCurCarPostionById(condition);
                List<Map<String, Object>> carPositionList = Lists.newArrayList();
                carPositionList.add(carPositionMap);
                order.put("carPosition",carPositionList);
//                if(carPositionMap!= null){
//                    order.put("longitude",carPositionMap.get("lng"));
//                    order.put("latitude",carPositionMap.get("lati"));
//                }
            }
        }

        if(StringUtil.trim(order.get("order_status")).equals("3")){
            TOrderDto newOrder = new TOrderDto();
            Date date = new Date();
            newOrder.setId(StringUtil.trim(order.get("id")));
            newOrder.setRideEndDate(date);
            orderService.updateById(newOrder);
            order.put("ride_end_date",date.getTime());
        }

        String start_station_id = StringUtil.trim(order.get("start_station_id"));
        String end_station_id = StringUtil.trim(order.get("end_station_id"));
        String stations = redisUtil.get(RedisConstant.SYS_STATIONS_CACHE_KEY);
        Map<String,Object> start_station_position = Maps.newHashMap();
        Map<String,Object> end_station_position = Maps.newHashMap();
        List<Map<Object, Object>> stationsList = JsonUtil.parseJSON2List(stations);
        for (Map<Object, Object> station : stationsList) {
            String stationId = StringUtil.trim(station.get("id"));
            if(start_station_id.equals(stationId)){
                start_station_position.put("latitude",station.get("latitude"));
                start_station_position.put("longitude",station.get("longitude"));
            }
            if(end_station_id.equals(stationId)){
                end_station_position.put("latitude",station.get("latitude"));
                end_station_position.put("longitude",station.get("longitude"));
            }
            if(start_station_position.size()>0 && end_station_position.size()>0){
                break;
            }
        }
        order.put("start_station_position",start_station_position);
        order.put("end_station_position",end_station_position);
        return ResultVo.success().setDataSet(order);
    }


    /**
     * 显示评价
     * @param data 订单id
     * @return
     */
    @PostMapping(value = "/showEvaluate")
    ResultVo showEvaluate(@RequestBody Map<String, Object> data){
        String orderId = StringUtil.trim(data.get("orderId"));
        int count = orderService.count(
                new QueryWrapper<TOrderDto>()
                        .select("id")
                        .eq("id", orderId)
                        .eq("order_status", "3")
        );
        if(count == 1){
            return ResultVo.success();
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE,"行程未结束");

    }

    /**
     * 设置订单失效
     * @param paramData
     * @return
     */
    @PostMapping("/setExpOrder")
    ResultVo setExpOrder(@RequestBody Map<String, Object> paramData){
        String id = StringUtil.trim(paramData.get("id"));
        TOrderDto query = orderService.getById(id);
        //订单已过期
        if(query.getOrderStatus().equals("1")){
            TOrderDto update = new TOrderDto();
            update.setId(id);
            update.setUpdateDate(new Date());
            update.setOrderStatus("4");
            boolean isRight = orderService.updateById(update);
        }
        return ResultVo.success();
    }


    @PostMapping(value = "/setRouteOrderExpire")
    ResultVo setRouteOrderExpire(@RequestBody Map<String, Object> condition) throws Exception {
        Date nowTime = new Date();
        String routeId = StringUtil.trim(condition.get("routeId"));
        String currStation = StringUtil.trim(condition.get("currStation"));
        List<Map<String, Object>> maps = orderService.listMaps(new QueryWrapper<TOrderDto>()
                .select("id")
                .eq("route_id", routeId)
                .eq("order_status", "1")
                .eq("start_station_id", currStation)
        );
        if(maps!= null && maps.size()>0){
            List<String> timeOutOrder = Lists.newArrayList();
            for (Map<String, Object> map : maps) {
                String id = StringUtil.trim(map.get("id"));
                timeOutOrder.add(id);
            }

            //超时订单调用算法
            log.info("超时订单调用算法 start...");
            Map<String, Object> maasCommonData = Maps.newHashMap();
            //查询车辆现在所属位置
            Map<String,Object> respMap = carPositionService.calculateVehicleDirectionAngle();
            List<Map<String,Object>> vehicleList = (List<Map<String, Object>>) respMap.get("vehicleList");
            maasCommonData.put("vehicleList",vehicleList);
            maasCommonData.put("takeOrder",null);
            maasCommonData.put("unTakeOrder",null);
            maasCommonData.put("completeOrder",null);
            maasCommonData.put("cancleOrder",null);
            maasCommonData.put("timeOutOrder",timeOutOrder);
            maasCommonData.put("finishedOrderId", null);
            //将查询结转换为json,放到json中
            Map<String, JsonValueProcessor> processors = new HashMap<String, JsonValueProcessor>();
            processors.put("java.util.Date", new UtilDateProcessor("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
            log.info("超时订单请求数据：" + JSONUtil.toJson(maasCommonData, processors));
            JSONObject jsonObject = HttpClientUtil.httpPost(SysParamConstant.COMMON_URL, JSONUtil.toJson(maasCommonData, processors));
            log.info("超时订单返回数据："+StringUtil.trim(jsonObject));
            if (!StringUtil.isEmpty(StringUtil.trim(jsonObject))) {
                log.info("更新超时订单数据 start ...");
                List<TOrderDto> updateOrderList = Lists.newArrayListWithCapacity(timeOutOrder.size());
                for (String id : timeOutOrder) {
                    TOrderDto order = new TOrderDto();
                    order.setId(id);
                    order.setOrderStatus("4");
                    order.setUpdateDate(nowTime);
                    updateOrderList.add(order);
                }
                boolean b = orderService.updateBatchById(updateOrderList);
                log.info("操作状态："+b);
                log.info("更新超时订单数据 end ...");

                CommonResponse response = (CommonResponse) JsonUtil.getInstance().fromJson(jsonObject.toString(), CommonResponse.class);
                List<RouteListObj> vehicleRouteList = response.getVehicleRouteList();
                if(response.getUserStationMap()==null && response.getUserVehicleList()==null && vehicleRouteList == null){
                    TRouteOffpeakDto byId = routeOffpeakService.getOne(new QueryWrapper<TRouteOffpeakDto>()
                            .select("id","route_state")
                            .eq("id",routeId)//行程状态未完成
                            .eq("route_state","0")
                    );
                    if(byId!=null){
                        //用户取消订单
                        byId.setRouteState("2");
                        byId.setUpdateDate(new Date());
                        boolean b1 = routeOffpeakService.updateById(byId);
                        log.info("结束司机行程状态"+ b1);

                        //结束当前司机行程
                        Map<String,List<Map<Object,Object>>> newRouteMap = Maps.newHashMap();
                        newRouteMap.put(byId.getCarId(),Lists.newArrayList());
                        kafkaTemplate.send(MessageConstant.BUSS_ROUTE_NAVIGATION_TOPIC_MESSAGE, JsonUtil.toJson(newRouteMap));
                    }
                }else {
                    String stations = redisUtil.get(RedisConstant.SYS_STATIONS_CACHE_KEY);
                    List<Map<Object, Object>> cacheStationList = JsonUtil.parseJSON2List(stations);

                    //查询行程
                    Map<String, Object> map = routeOffpeakService.getMap(new QueryWrapper<TRouteOffpeakDto>()
                            .select("id", "car_id", "midway_station", "curr_station", "all_position")
                            .eq("id", routeId)
                    );
                    String car_id = StringUtil.trim(map.get("car_id"));
                    for (RouteListObj routeListObj : vehicleRouteList) {
                        if(routeListObj.getVehicleID().equals(car_id)){
                            List<Map<Object,Object>> bussNavigationData = Lists.newArrayList();
                            ArrayList<String> stationList = routeListObj.getStationList();
                            for (String station : stationList) {
                                for (Map<Object, Object> cacheStation : cacheStationList) {
                                    String id = StringUtil.trim(cacheStation.get("id"));
                                    if(station.equals(id)){
                                        bussNavigationData.add(cacheStation);
                                        break;
                                    }
                                }
                            }

                            TRouteOffpeakDto routeOffpeakDto = new TRouteOffpeakDto();
                            routeOffpeakDto.setId(routeId);

                            List<String> midway_station = Arrays.asList(StringUtil.trim(map.get("midway_station")).split(","));
                            int curr_station = StringUtil.getAsInt(StringUtil.trim(map.get("curr_station")));
                            if(curr_station == -1){
                                routeOffpeakDto.setAllPosition(JsonUtil.object2Json(bussNavigationData));
                                routeOffpeakDto.setMidwayStation(StringUtils.join(stationList, ","));
                                //如果车辆已到站
                            }else{
                                int stationCurr = StringUtil.getAsInt(StringUtil.trim(midway_station.get(curr_station)));
                                int len = 0;
                                for (int i = 0;i<stationList.size();i++) {
                                    int station = StringUtil.getAsInt(StringUtil.trim(stationList.get(i)));
                                    if(stationCurr == station){
                                        len = i+1;
                                        break;
                                    }
                                }

                                List<Map<Object, Object>> allPositionLists = bussNavigationData.subList(len, bussNavigationData.size());
                                routeOffpeakDto.setAllPosition(JsonUtil.object2Json(allPositionLists));
                                if(allPositionLists == null || allPositionLists.size()==0){
                                    //推送导航数据
                                    Map<String,List<Map<Object,Object>>> newRouteMap = Maps.newHashMap();
                                    newRouteMap.put(car_id,allPositionLists);
                                    kafkaTemplate.send(MessageConstant.BUSS_ROUTE_NAVIGATION_TOPIC_MESSAGE, JsonUtil.toJson(newRouteMap));

                                    routeOffpeakDto.setRouteState("1");
                                }
                                String stationLists = StringUtils.join(stationList.subList(len, stationList.size()), ",");
                                if(StringUtil.isEmpty(stationLists)){
                                    routeOffpeakDto.setMidwayStation(StringUtils.join(midway_station.subList(0, curr_station + 1), ","));
                                }else{
                                    String beforeMidway = StringUtils.join(midway_station.subList(0, curr_station + 1), ",") + ",";
                                    routeOffpeakDto.setMidwayStation(beforeMidway+stationLists);
                                }
                            }
                            routeOffpeakDto.setUpdateDate(nowTime);
                            routeOffpeakService.updateById(routeOffpeakDto);
                            break;
                        }
                    }
                }
            }else{
                CommonResponse response = (CommonResponse) JsonUtil.getInstance().fromJson(jsonObject.toString(), CommonResponse.class);
                List<RouteListObj> vehicleRouteList = response.getVehicleRouteList();

                if(response.getUserStationMap()==null && response.getUserVehicleList()==null && vehicleRouteList == null){
                    TRouteOffpeakDto byId = routeOffpeakService.getOne(new QueryWrapper<TRouteOffpeakDto>()
                            .select("id","route_state")
                            .eq("id",routeId)//行程状态未完成
                            .eq("route_state","0")
                    );
                    if(byId!=null){
                        //用户取消订单
                        byId.setRouteState("2");
                        byId.setUpdateDate(new Date());
                        boolean b1 = routeOffpeakService.updateById(byId);
                        log.info("结束司机行程状态"+ b1);

                        //结束当前司机行程
                        Map<String,List<Map<Object,Object>>> newRouteMap = Maps.newHashMap();
                        newRouteMap.put(byId.getCarId(),Lists.newArrayList());
                        kafkaTemplate.send(MessageConstant.BUSS_ROUTE_NAVIGATION_TOPIC_MESSAGE, JsonUtil.toJson(newRouteMap));
                    }
                }
            }
            log.info("超时订单调用算法 end ...");
            System.out.println("超时订单ids :" +timeOutOrder.toString());
        }
        return ResultVo.success();
    }


    /**
     * pengFeng
     * 生成订单
     *
     * @param tOrderDto 订单信息
     * @return
     */
    @PostMapping("/addOrder")
    public ResultVo addOrder(@RequestBody TOrderDto tOrderDto) {
        if (orderService.save(tOrderDto)) {
            return ResultVo.success().setDataSet(tOrderDto.getId());
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
    }

    /**
     * pengFeng
     * 修改订单信息
     *
     * @param tOrderDto 订单信息
     * @return
     */
    @PutMapping("/updateOrder")
    public ResultVo updateOrder(@RequestBody TOrderDto tOrderDto) {
        if (orderService.updateById(tOrderDto)) {
            return ResultVo.success();
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
    }

    /**
     * 查询用户行程
     *
     * @param condition map参数
     * @return 行程列表
     */
    @PostMapping("/getOrderUser")
    public ResultVo getOrderUser(@RequestBody Map<String, Object> condition) {
        if (StringUtil.isEmpty(StringUtil.trim(condition.get("userId")))) {
            return ResultVo.error(ResultConstant.SYS_REQUIRED_PARAMETER_ERROR, ResultConstant.SYS_REQUIRED_PARAMETER_ERROR_VALUE);
        }
        int pageIndex = StringUtil.getAsInt(StringUtil.trim(condition.get("pageIndex")), 1);
        int pageSize = StringUtil.getAsInt(StringUtil.trim(condition.get("pageSize")), 10);
        if (!"".equals(StringUtil.trim(condition.get("startTime")))&&!"".equals(condition.get("endTime"))){
            Timestamp aStartTime = Timestamp.valueOf(StringUtil.trim(condition.get("startTime")));
            String endTime = StringUtil.trim(condition.get("endTime")).replace("00:00:00","23:59:59");
            Timestamp aEndTime = Timestamp.valueOf(endTime);
            condition.put("aStartTime", aStartTime);
            condition.put("aEndTime", aEndTime);
        }
        ResultVo resultVo = new AbstractPageTemplate<Map>() {
            @Override
            protected List<Map> executeSql() {

                List<Map> list = orderService.getOrderUser(condition);
                return list;
            }
        }.preparePageTemplate(pageIndex, pageSize);
        return resultVo;
        // return ResultVo.success().setDataSet(orderService.getOrderUser(userId));

    }


    /**
     * 查询未分配的订单信息
     *
     * @param condition
     * @return ResultVo
     */
    @PostMapping(value = "/getUnAllotCarOrderList")
    ResultVo getUnAllotCarOrderList(@RequestBody Map<String, Object> condition) {
        QueryWrapper queryWrapper = (QueryWrapper) JsonUtil.json2Ojbect(StringUtil.trim(condition.get("queryWrapper")), QueryWrapper.class);
        queryWrapper.isNull("car_id");
        List<TOrderDto> list = null;
        if (null != queryWrapper) {
            list = orderService.list(queryWrapper);
        } else
            list = orderService.list();
        return ResultVo.success().setDataSet(list);
    }

    /**
     * 生成预支付信息
     *
     * @param data
     * @return
     */
    @PostMapping("loadPayPre")
    ResultVo loadAppPayPre(@RequestBody Map<String, Object> data) throws Exception {
        //获取订单号
        String orderNo = StringUtil.trim(data.get("orderNo"));
        String openid = StringUtil.trim(data.get("openid"));
        if (StringUtil.isEmpty(StringUtil.trim(orderNo)) || StringUtil.isEmpty(StringUtil.trim(openid))) {
            return ResultVo.error(ResultVo.Status.REQUIRED_PARAMETER_ERROR);
        }
        Map<String, Object> orderDto = orderService.getMap(new QueryWrapper<TOrderDto>()
                .select("id", "order_amount", "order_status", "is_pay")
                .eq("order_no", orderNo)
        );
        if (null == orderDto) {
            return ResultVo.error(ResultVo.Status.SYS_ORDER_DATA_ERROR);
        }
        String is_pay = StringUtil.trim(orderDto.get("is_pay"));
        if (is_pay.equals("1")) {//订单已支付
            return ResultVo.error(ResultVo.Status.SYS_ORDER_HAVE_PAID_ERROR);
        }
        //修改支付流水号
        TOrderDto updateOrder = new TOrderDto();
        updateOrder.setId(StringUtil.trim(orderDto.get("id")));
        String tardeNo = "SC_" + format.format(new Date()) + new Random().nextInt(1000);
        updateOrder.setTradeNo(tardeNo);
        boolean b = orderService.updateById(updateOrder);
        if (!b) {
            return ResultVo.error(ResultVo.Status.SYS_REQUIRED_FAILURE);
        }
        BigDecimal order_amount = new BigDecimal(StringUtil.trim(orderDto.get("order_amount")));
        int sumAmout = order_amount.multiply(new BigDecimal(100)).intValue();

        HashMap<Object, Object> dataSet = Maps.newHashMap();
        dataSet.put("body", "maas出行订单");// 商品名称
        dataSet.put("out_trade_no", tardeNo);// 交易编号
        dataSet.put("sum_amout", sumAmout);
        dataSet.put("openid", openid);
        dataSet.put("notify_url", "http://mallweb.free.ngrok.cc/mall-web-consumer/pay/wechatCallBack");

        Map<Object, Object> prepayMap = (Map<Object, Object>) prePay(dataSet);
        prepayMap.put("out_trade_no", tardeNo);
        return ResultVo.success().setDataSet(prepayMap);
    }

    /**
     * 预支付接口
     *
     * @param payParams
     * @return
     */
    private Object prePay(Map<Object, Object> payParams) {
        // 微信支付
        try {
            Map<String, String> wechatPay = wechatPay(payParams);
            String return_code = StringUtil.trim(wechatPay.get("return_code"));
            String return_msg = StringUtil.trim(wechatPay.get("return_msg"));
            String code_url = StringUtil.trim(wechatPay.get("code_url"));
            String prepay_id = StringUtil.trim(wechatPay.get("prepay_id"));//预支付ID
            wechatPay.clear();
            if (return_code.equals("SUCCESS") && return_msg.equals("OK")) {
                Map<String, String> reqData = new HashMap<String, String>();
                MyConfig myConfig = new MyConfig();
                reqData.put("signType", "MD5");
                reqData.put("appId", myConfig.getAppID());
                reqData.put("package", "prepay_id=" + prepay_id);
                reqData.put("nonceStr", WXPayUtil.generateNonceStr());
                reqData.put("timeStamp", StringUtil.trim(new Date().getTime() / 1000));
                reqData.put("sign", WXPayUtil.generateSignature(reqData, myConfig.getKey()));
                return reqData;
            } else {
                wechatPay.put("state", "1");
                wechatPay.put("returnMsg", return_msg);
            }
            return wechatPay;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Maps.newHashMap();
    }

    /**
     * 微信支付
     *
     * @param requestParams
     * @return
     * @throws Exception
     */
    private static Map<String, String> wechatPay(Map<Object, Object> requestParams) throws Exception {
        Map<String, String> data = new HashMap<String, String>();
        data.put("body", StringUtil.trim(requestParams.get("body").toString()));
        data.put("out_trade_no", Md5.MD5Encode(StringUtils.trim(requestParams.get("out_trade_no").toString()), "utf-8"));
        data.put("device_info", "");
        data.put("fee_type", "CNY");
        data.put("total_fee", StringUtil.trim(requestParams.get("sum_amout")));
        data.put("spbill_create_ip", "127.0.0.1");
        data.put("notify_url", StringUtil.trim(requestParams.get("notify_url")));
        data.put("trade_type", "JSAPI");
        data.put("openid", StringUtil.trim(requestParams.get("openid")));
        try {
            Map<String, String> resp = wxpay.unifiedOrder(data);
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Maps.newHashMap();
    }


    /**
     * 查询微信支付结果
     *
     * @param paramData
     * @return
     */
    @PostMapping("queryWechatPayResult")
    @Transactional
    ResultVo queryWechatPayResult(@RequestBody Map<String, Object> paramData) {
        //订单号
        String userId = RequestUtil.analysisToken(StringUtil.trim(paramData.get("token"))).getUserId();
        String orderNo = StringUtil.trim(paramData.get("orderNo"));
        if (StringUtil.isEmpty(StringUtil.trim(orderNo))) {
            return ResultVo.error(ResultVo.Status.REQUIRED_PARAMETER_ERROR);
        }

        QueryWrapper<TOrderDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(
                "id", "trade_no", "passengers_num", "trip_time", "start_station_id",
                "end_station_id", "user_id", "create_date", "order_status",
                "is_pay", "order_amount", "route_type", "trip_time","exp_date"
        );
        queryWrapper.eq("order_no", orderNo);
        Map<String, Object> orderMap = orderService.getMap(queryWrapper);
        if (null == orderMap) return ResultVo.error(ResultVo.Status.SYS_ORDER_DATA_ERROR);
        String is_pay = StringUtil.trim(orderMap.get("is_pay"));
        //订单已支付
        if (is_pay.equals("1")) return ResultVo.success();

        synchronized (this){
            orderMap = orderService.getMap(queryWrapper);
            if (null == orderMap) return ResultVo.error(ResultVo.Status.SYS_ORDER_DATA_ERROR);
            is_pay = StringUtil.trim(orderMap.get("is_pay"));

            //订单已支付
            if (is_pay.equals("1")) return ResultVo.success();

            String trade_no = StringUtil.trim(orderMap.get("trade_no"));
            String order_amount = StringUtil.trim(orderMap.get("order_amount"));
            Map<String, String> data = new HashMap<String, String>();
            data.put("out_trade_no", Md5.MD5Encode(StringUtil.trim(trade_no), "utf-8"));
            data = query(data);
            if (StringUtil.trim(data.get("state")).equals("0")) {
                TOrderDto order = new TOrderDto();
                String orderId = StringUtil.trim(orderMap.get("id"));
                order.setId(orderId);
                order.setIsPay("1");
                order.setOrderStatus("1");
                Date updateDate = new Date();
                order.setUpdateDate(updateDate);
                //设置支付时间
                order.setPayDate(updateDate);
                boolean success = orderService.updateById(order);
                if (success) {
                    //支付成功后  设置过期时间
                    long time = updateDate.getTime();
                    Date exp_date = (Date) orderMap.get("exp_date");
                    redisUtil.set("orderExp="+orderId,orderId,(exp_date.getTime()-time)/1000);


//                    //判断是否平峰，调用对应的算法接口
//                    //行程类型( 0 平峰 1高峰 )
//                    String routeType = StringUtil.trim(orderMap.get("route_type"));
//                    //预约出行的时间
//                    String tripTime = StringUtil.trim(orderMap.get("trip_time"));
//                    if (routeType.equals("0")) {
//                        //判断预约出行还是实时出行
//                        if (StringUtil.isEmpty(tripTime)) {
//                            //调用平峰算法
//                            try {
//                                callMaasCommon(orderMap);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }else{
////                            try {
////                                callMaasCommon(orderMap);
////                            } catch (Exception e) {
////                                e.printStackTrace();
////                            }
//                        }
//                    } else {
//                        //调用高峰算法
//
//                    }

                    TPaySerialDto paySerialDto = new TPaySerialDto();
                    paySerialDto.setOrderId(orderId);
                    try {
                        paySerialDto.setPayAmount(StringUtil.getDouble(StringUtil.trim(order_amount)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    paySerialDto.setCreateDate(updateDate);
                    paySerialDto.setPayStatus("1");
                    paySerialDto.setUserId(userId);
                    success = paySerialService.save(paySerialDto);
                    //支付成功
                    if (success)
                        return ResultVo.error(ResultConstant.SYS_REQUIRED_SUCCESS, ResultConstant.SYS_ORDER_HAVE_PAID_ERROR_VALUE);
                }
            }else{
                return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE,data.get("returnMsg"));
            }
        }
        return ResultVo.error(ResultVo.Status.SYS_ORDER_UNPAID_ERROR);
    }

    /**
     * 取消订单调用平峰算法接口
     * @param tOrderDto
     */
    private void callCancelMaasCommon(TOrderDto tOrderDto) throws Exception {
        Map<String, Object> orderMap = Maps.newHashMap();
        //保存站点Ids
        List<String> stationIds = Lists.newArrayList();
        //过滤不必要的参数
        Date create_date = tOrderDto.getCreateDate();
        orderMap.put("orderID", tOrderDto.getId());
        orderMap.put("ordertime",create_date);
        orderMap.put("people", tOrderDto.getPassengersNum());
        orderMap.put("starttime", DateUtils.addSeconds(create_date,3));
        orderMap.put("state", "4");
        orderMap.put("userID", tOrderDto.getUserId());
        orderMap.put("boardtime", null);//保存上车时间

        String startStationId = tOrderDto.getStartStationId();
        String endStationId = tOrderDto.getEndStationId();
        stationIds.add(startStationId);
        stationIds.add(endStationId);
        //查询站点坐标
        List<Map<String, Object>> stationList = stationService.queryStationByIds(stationIds);
        for (Map<String, Object> station : stationList) {
            String id = StringUtil.trim(station.get("id"));
            if(startStationId.equals(id)){
                Map<String, Object> curLocation = Maps.newHashMap();
                curLocation.put("id",station.get("id"));
                curLocation.put("lati",station.get("latitude"));
                curLocation.put("lng",station.get("longitude"));
                orderMap.put("orignal",curLocation);
            }
            if(endStationId.equals(id)){
                Map<String, Object> curLocation = Maps.newHashMap();
                curLocation.put("id",station.get("id"));
                curLocation.put("lati",station.get("latitude"));
                curLocation.put("lng",station.get("longitude"));
                orderMap.put("dest",curLocation);
            }
        }

        //判断是否有订单，如果有则处理请求数据
        Map<String,Object> maasCommonData = Maps.newHashMap();
        Date date = new Date();
        QueryWrapper queryWrapper =new QueryWrapper();
        queryWrapper.select(
                "id","passengers_num","trip_time","start_station_id",
                "end_station_id","user_id","create_date","order_status");
        queryWrapper.isNull("car_id");
        queryWrapper.eq("order_type","1");//预约出行
        queryWrapper.eq("is_pay","1");//已支付
        queryWrapper.eq("order_status","1");//待使用

        //保存未上车订单列表
        List<Map<String,Object>> unTakeOrderList = orderService.listMaps(queryWrapper);
        //保存站点Ids
        stationIds.clear();
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
        List<Map<String, Object>> stationList1 = stationService.queryStationByIds(stationIds);

        //整合算法数据
        maasCommonData = orderService.structureMaasCommonData(stationList1,unTakeOrderList,completeOrderList,takeOrderList,vehicleList);

        //保存取消的订单
        Map<String,List<Map<String,Object>>> cancleOrder = Maps.newHashMap();
        List<Map<String,Object>> orderListMap = Lists.newArrayList();
        orderListMap.add(orderMap);
        cancleOrder.put(tOrderDto.getCarId(),orderListMap);
        maasCommonData.put("cancleOrder",cancleOrder);
        maasCommonData.put("finishedOrderId", null);

        //将查询结转换为json,放到json中
        Map<String, JsonValueProcessor> processors = new HashMap<String, JsonValueProcessor>();
        processors.put("java.util.Date", new UtilDateProcessor("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
        System.out.println("取消订单调用算法请求数据："+JSONUtil.toJson(maasCommonData, processors));
        JSONObject jsonObject = HttpClientUtil.httpPost(SysParamConstant.COMMON_URL, JSONUtil.toJson(maasCommonData, processors));

        System.out.println("返回数据："+StringUtil.trim(jsonObject));
        if(!StringUtil.isEmpty(StringUtil.trim(jsonObject))){
            CommonResponse response = (CommonResponse) JsonUtil.getInstance().fromJson(StringUtil.trim(jsonObject), CommonResponse.class);
            List<RouteListObj> vehicleRouteList = response.getVehicleRouteList();
            if(response.getUserStationMap()==null && response.getUserVehicleList()==null && vehicleRouteList == null){
                String routeId = tOrderDto.getRouteId();
                if(!StringUtil.isEmpty(routeId)){
                    TRouteOffpeakDto byId = routeOffpeakService.getOne(new QueryWrapper<TRouteOffpeakDto>()
                            .select("id","route_state")
                            .eq("id",routeId)//行程状态未完成
                            .eq("route_state","0")
                    );
                    if(byId!=null){
                        //用户取消订单
                        byId.setRouteState("2");
                        byId.setUpdateDate(new Date());
                        boolean b = routeOffpeakService.updateById(byId);
                        log.info("结束司机行程状态"+ b);

                        //结束当前司机行程
                        Map<String,List<Map<Object,Object>>> newRouteMap = Maps.newHashMap();
                        newRouteMap.put(tOrderDto.getCarId(),Lists.newArrayList());
                        kafkaTemplate.send(MessageConstant.BUSS_ROUTE_NAVIGATION_TOPIC_MESSAGE, JsonUtil.toJson(newRouteMap));
                        log.info("重新规划导航发送导航数据:"+JsonUtil.toJson(newRouteMap));
                    }
                }
            }else{
                boolean isCancelRoute = true;
                if(null != vehicleRouteList && vehicleRouteList.size()>0){
                    for (RouteListObj routeListObj : vehicleRouteList) {
                        if(routeListObj.getVehicleID().equals(tOrderDto.getCarId())){
                            isCancelRoute = false;
                        }
                    }
                }
                if(isCancelRoute){
                    String routeId = tOrderDto.getRouteId();
                    TRouteOffpeakDto byId = routeOffpeakService.getOne(new QueryWrapper<TRouteOffpeakDto>()
                            .select("id","route_state")
                            .eq("id",routeId)//行程状态未完成
                            .eq("route_state","0")
                    );
                    if(byId!=null){
                        //用户取消订单
                        byId.setRouteState("2");
                        byId.setUpdateDate(new Date());
                        boolean b = routeOffpeakService.updateById(byId);
                        log.info("结束司机行程状态"+ b);

                        //结束当前司机行程
                        Map<String,List<Map<Object,Object>>> newRouteMap = Maps.newHashMap();
                        newRouteMap.put(tOrderDto.getCarId(),Lists.newArrayList());
                        kafkaTemplate.send(MessageConstant.BUSS_ROUTE_NAVIGATION_TOPIC_MESSAGE, JsonUtil.toJson(newRouteMap));
                        log.info("重新规划导航发送导航数据:"+JsonUtil.toJson(newRouteMap));
                    }
                }
                parseCommonData(jsonObject.toString());
            }
        }else{
            //当前结束司机当前行程
            String routeId = tOrderDto.getRouteId();
            if(!StringUtil.isEmpty(routeId)){
                TRouteOffpeakDto byId = routeOffpeakService.getOne(new QueryWrapper<TRouteOffpeakDto>()
                        .select("id","route_state")
                    .eq("id",routeId)//行程状态未完成
                    .eq("route_state","0")
                );
                if(byId!=null){
                    //用户取消订单
                    byId.setRouteState("2");
                    byId.setUpdateDate(new Date());
                    boolean b = routeOffpeakService.updateById(byId);
                    log.info("结束司机行程状态"+ b);

                    //结束当前司机行程
                    Map<String,List<Map<Object,Object>>> newRouteMap = Maps.newHashMap();
                    newRouteMap.put(tOrderDto.getCarId(),Lists.newArrayList());
                    kafkaTemplate.send(MessageConstant.BUSS_ROUTE_NAVIGATION_TOPIC_MESSAGE, JsonUtil.toJson(newRouteMap));
                    log.info("重新规划导航发送导航数据:"+JsonUtil.toJson(newRouteMap));
                }
            }
        }
    }


    /**
     * 平峰算法调用接口
     *
     * @param orderMap
     */
    private void callMaasCommon(Map<String, Object> orderMap) throws Exception {

        //保存站点Ids
        List<String> stationIds = Lists.newArrayList();

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
        orderMap.remove("trip_time");

        stationIds.add(StringUtil.trim(orderMap.get("start_station_id")));
        stationIds.add(StringUtil.trim(orderMap.get("end_station_id")));


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
        Date date = new Date();
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


        List<Map<String, Object>> unTakeOrderList = Lists.newArrayList();
        unTakeOrderList.add(orderMap);

        //整合算法数据
        Map<String, Object> maasCommonData = orderService.structureMaasCommonData(stationList, unTakeOrderList, completeOrderList, takeOrderList, vehicleList);
        //保存取消的订单
        maasCommonData.put("cancleOrder",Maps.newHashMap());
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
                        list = strings.subList(0,asInt+1);
                    }
                    list.addAll(stationList);
                    routeOffpeakDto.setMidwayStation(StringUtils.join(list, ","));
                    routeOffpeakDto.setUpdateDate(date);
                    routeOffpeakDto.setAllPosition(JsonUtil.object2Json(bussNavigationData));
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
            log.info("重新规划导航发送导航数据:"+JsonUtil.toJson(newRouteMap));
        }
    }

    private Map<String, String> query(Map<String, String> data) {
        try {
            Map<String, String> resp = wxpay.orderQuery(data);
            String return_code = StringUtil.trim(resp.get("return_code"));
            String return_msg = StringUtil.trim(resp.get("return_msg"));
            String trade_state = StringUtil.trim(resp.get("trade_state"));
            String out_trade_no = StringUtil.trim(resp.get("out_trade_no"));
            String trade_state_desc = StringUtil.trim(resp.get("trade_state_desc"));
            data.clear();
            if (return_code.equals("SUCCESS") && return_msg.equals("OK")) {
                if (trade_state.equals("SUCCESS")) {
                    data.put("state", "0");// 支付成功
                    data.put("out_trade_no", out_trade_no);// 支付成功
                } else {
                    data.put("state", "1");
                    data.put("returnMsg", trade_state_desc);
                }
            } else {
                data.put("state", "1");
                data.put("returnMsg", trade_state_desc);
            }
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        data.put("state", "1");
        data.put("returnMsg", "查询失败");
        return data;
    }

    /**
     * 根据订单id生成二维码数据
     *
     * @param paramData
     * @return
     */
    @PostMapping("/getOrderQRCode")
    public ResultVo getOrderQRCode(@RequestBody Map<String, Object> paramData) {
        if(paramData== null){
            return ResultVo.error(ResultVo.Status.REQUIRED_PARAMETER_ERROR);
        }
        //获取订单id
        String id = StringUtil.trim(paramData.get("id"));
        String token = StringUtil.trim(paramData.get("token"));
        //根据id查询订单
        TOrderDto orderDto = orderService.getById(id);
        //判断能否查到数据
        if (orderDto == null) {
            return ResultVo.error(ResultVo.Status.SYS_ORDER_DATA_ERROR);
        }
        //判断订单状态 （0 已失效 1待使用 2进行中 3已完成 )
        String orderStatus = orderDto.getOrderStatus();
        if(orderStatus.equals("0")) {
            return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, "订单未支付");
        }else if (orderDto.getOrderStatus().equals("1")) {
            Date expDate = orderDto.getExpDate();
            long time = new Date().getTime();
            if(time > expDate.getTime()){
                return ResultVo.error(ResultVo.Status.SYS_QRCODE_EXPIRED_ERROR);
            }
            QRCodeParam QRCodeParam = new QRCodeParam();
            QRCodeParam.setOid(id);
            QRCodeParam.setToken(RequestUtil.analysisToken(token).getUserId());
            //时间转换成那种固定格式的。
            QRCodeParam.setExpTime(DateUtils.formatDate(expDate,DateUtils.PATTERN_yyyy_MM_dd_HH_mm_ss));
            //作为刷新二维码，生成动态字符串
            QRCodeParam.setNowTime(StringUtil.trim(time));
            return ResultVo.success().setDataSet(QRCodeParam);
        }else if(orderStatus.equals("2") || orderStatus.equals("3") ){
            return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE,ResultConstant.SYS_QCCODE_HAS_USERED_ERROR_VALUE);
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE,ResultConstant.SYS_ORDER_HAS_EXPIRED_ERROR_VALUE);
//        return ResultVo.error(ResultVo.Status.SYS_ORDER_DATA_ERROR);
    }


    /**
     * 自动过期
     *
     * @param condition
     * @return
     */
    @PostMapping(value = "/setOrderExpire")
    ResultVo setOrderExpire(@RequestBody Map<String, Object> condition) {
        String id = StringUtil.trim(condition.get("id"));
        TOrderDto queryOrder = orderService.getById(id);
        if (queryOrder != null) {
            //订单状态( 0 创建订单 1待使用 2进行中 3已完成 4已失效 )
            String orderStatus = StringUtil.trim(queryOrder.getOrderStatus());
            String type = StringUtil.trim(condition.get("type"));
            //在指定时间内没有完成支付，则取消订单
            if(type.equals("0")) {
                if (orderStatus.equals("0")) {
                    if (executeOrderExpire(id))
                        return ResultVo.success();
                }
            }else{
               // 在指定时间内没有乘车，则取消订单
                if (orderStatus.equals("1")) {
                    if (executeOrderExpire(id))
                        return ResultVo.success();
                }
//                if (executeOrderExpire(id))
//                    return ResultVo.success();
            }
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
    }

    private boolean executeOrderExpire(String id) {
        TOrderDto tOrderDto = new TOrderDto();
        tOrderDto.setId(id);
        //设置订单过期
        tOrderDto.setOrderStatus("4");
        tOrderDto.setUpdateDate(new Date());
        if (orderService.updateById(tOrderDto)) {
            return true;
        }
        return false;
    }

    /**
     * 根据用户id 查询用户距离当前时间最近一条
     *
     * @param data userId
     * @return ResultVo
     */
    @PostMapping("/getLatelyOrder")
    public ResultVo getLatelyOrder(@RequestBody Map<String, Object> data) {
        TOrderDto userId = orderService.getLatelyOrder(String.valueOf(data.get("userId")));
        return ResultVo.success().setDataSet(userId);
    }


    /**
     * 加载订单退款过期时间
     *
     * @param paramData
     * @return
     */
    @PostMapping("/loadRefundExpirationTime")
    public ResultVo loadRefundExpirationTime(@RequestBody Map<String, Object> paramData) {
        String orderNo = StringUtil.trim(paramData.get("orderNo"));
        String userId = StringUtil.trim(paramData.get("userId"));
        if (StringUtil.isEmpty(StringUtil.trim(orderNo)) || StringUtil.isEmpty(StringUtil.trim(userId))) {
            return ResultVo.error(ResultVo.Status.REQUIRED_PARAMETER_ERROR);
        }
        Date nowtime = new Date();
        //根据用户id查询所有当前用订单
        QueryWrapper queryWrapperUser = new QueryWrapper();
        queryWrapperUser.eq("user_id", userId);
        //0：未取消 ，1：用户取消，2系统取消
        queryWrapperUser.eq("is_cancel", "1");
        queryWrapperUser.apply("to_char(create_date,'yyyy-MM-dd')={0}", DateUtils.formatDate(nowtime, Constants.DATE_PATTERN));
        int orderCount = orderService.count(queryWrapperUser);
        if (orderCount > 5) {
            return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, "用户每天最多只能取消五次行程");
        }

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_no", orderNo);
        TOrderDto tOrderDto = orderService.getOne(queryWrapper);
        if (tOrderDto != null) {
            //判断订单状态 订单状态( 0 创建订单 1待使用 2进行中 3已完成 4已失效 )
            if (tOrderDto.getOrderStatus().equals("1")) {
                //判断高峰还是平峰 行程类型( 0 平峰 1高峰 )
                if (tOrderDto.getRouteType().equals("0")) {
                    //判断是即时还是预约
                    if (tOrderDto.getOrderType().equals("0")) {
                        //即时+3分钟
                        Date tripTime = tOrderDto.getTripTime();
                        tripTime = tripTime == null ? tOrderDto.getCreateDate() : tripTime;
                        long tripTimeLong = tripTime.getTime() - 3 * 60 * 1000;
                        if (tripTimeLong < new Date().getTime()) {
                            //设置是否退款。'是否退款 (0:否 1：是)'
                            tOrderDto.setIsRefund("1");
                        } else {
                            //设置是否退款。'是否退款 (0:否 1：是)'
                            tOrderDto.setIsRefund("0");
                        }
                        //取消点订单返回前台数据
                        return OrderDataVo(tOrderDto, tripTime.getTime() + 3 * 60 * 1000);
                    } else {
                        //预约+10分钟
                        long tripTime = tOrderDto.getTripTime().getTime() - 10 * 60 * 1000;
                        if (tripTime < new Date().getTime()) {
                            //设置是否退款。'是否退款 (0:否 1：是)'
                            tOrderDto.setIsRefund("1");
                        } else {
                            //设置是否退款。'是否退款 (0:否 1：是)'
                            tOrderDto.setIsRefund("0");
                        }
                        //取消点订单返回前台数据
                        return OrderDataVo(tOrderDto, tripTime);
                    }
                } else {
                    //高峰 24小时内有效
                    long tripTime = new Date(DateUtils.getDayEnd().getTime()).getTime();
                    //设置是否退款。'是否退款 (0:否 1：是)'
                    tOrderDto.setIsRefund("1");
                    return OrderDataVo(tOrderDto, tripTime);
                }
            }
        }
        return ResultVo.error(ResultVo.Status.REQUIRED_PARAMETER_ERROR);
    }

    /**
     * 取消点订单返回前台数据
     *
     * @param tOrderDto
     * @param tripTime
     * @return
     */
    private ResultVo OrderDataVo(TOrderDto tOrderDto, long tripTime) {

        boolean success = orderService.updateById(tOrderDto);
        if (success) {
            Map<String, String> orderMap = new HashMap<>();
            orderMap.put("isRefund", tOrderDto.getIsRefund());
            orderMap.put("tripTime", DateUtils.formatDate(new Date(tripTime), DateUtils.PATTERN_HH_mm_ss));
            return ResultVo.success().setDataSet(orderMap);
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);

    }

    /**
     * 取消订单
     *
     * @param paramData
     * @return
     */
    @PostMapping("/cancelOrder")
    public ResultVo cancelOrder(@RequestBody Map<String, Object> paramData) {
        System.out.println("用户调用取消订单的方法");
        Date nowtime = new Date();
        String orderNo = StringUtil.trim(paramData.get("orderNo"));
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_no", orderNo);
        TOrderDto tOrderDto = (TOrderDto) orderService.getObj(queryWrapper);
        if (tOrderDto != null) {
            //判断订单状态 订单状态( 0 创建订单 1待使用 2进行中 3已完成 4已失效 )
            if (tOrderDto.getOrderStatus().equals("1")) {
                //行程类型( 0 平峰 1高峰 )
                String routeType = tOrderDto.getRouteType();
                Date tripTime = tOrderDto.getTripTime();
                boolean isRefund = false;
                if (routeType.equals("0")) {
                    //平峰实时出行
                    if (tripTime == null) {
                        Date updateDate = tOrderDto.getUpdateDate();
                        if (updateDate.getTime() + 1000 * 60 * 3 > nowtime.getTime()) {
                            isRefund = true;
                        }
                    } else {
                        //如果出发前10分钟取消订单时间退款
                        long time = nowtime.getTime();
                        long time1 = tripTime.getTime();
                        if (time < time1 - 1000 * 60 * 10 ) {
                            isRefund = true;
                        }
                    }
                } else {
                    //判断下单是否是同一天，同一天则可以退款
                    Date createDate = tOrderDto.getCreateDate();
                    isRefund = DateUtils.isSameDay(nowtime, createDate);
                }
                //重新设置订单状态
                tOrderDto.setOrderStatus("4");
                tOrderDto.setIsCancel("1");
                //设置是否退款。'是否退款 (0:否 1：是)'
                if (isRefund) {
                    ResultVo vo = applyRefund(paramData);
                    if(vo.getResult() != 0){
                        return vo;
                    }
                    tOrderDto.setIsRefund("2");
                }
                boolean success = orderService.updateById(tOrderDto);
                if (success) {
                    //调用取消订单的平峰算法
                    try {
                        callCancelMaasCommon(tOrderDto);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return ResultVo.success();
                }
            }else{
                //订单已过期
                return ResultVo.error(ResultVo.Status.SYS_ORDER_DATA_ERROR);
            }
        }
        return ResultVo.error(ResultVo.Status.SYS_REQUIRED_FAILURE);
    }

    /**
     * 检查二维码回调事件
     * 判断二维码是否被扫  或者已过期
     * @param param
     * @return
     */
    @PostMapping(value = "/checkQrCallBack")
    @Transactional(rollbackFor = Exception.class)
    ResultVo checkQrCallBack(@RequestBody QRCodeParam param) {
        String id = param.getOid();
        TOrderDto queryOrder = orderService.getById(id);
        if(queryOrder == null){
            //订单数据异常
            return ResultVo.error(ResultVo.Status.SYS_ORDER_DATA_ERROR);
        }
        //订单状态( 0 创建订单 1待使用 2进行中 3已完成 4已失效 )
        String orderStatus = queryOrder.getOrderStatus();
        Date nowTime = new Date();
        ResultVo vo = null;
        switch (orderStatus) {
            case "0":
                vo = ResultVo.error(ResultConstant.SYS_ORDER_UNPAID_ERROR, ResultConstant.SYS_ORDER_UNPAID_ERROR_VALUE);
                break;
            case "1":
                //行程类型( 0 平峰 1高峰  )
                String routeType = queryOrder.getRouteType();
                String deviceCode = param.getDeviceCode();
                Map<String, Object> map = carDeviceService.getMap(new QueryWrapper<TCarDeviceDto>()
                        .select("id", "car_id", "device_code")
                        .eq("device_status", "1")
                        .eq("device_code", deviceCode)
                );
                if(map == null){
                    return ResultVo.error(-1,"车辆设备未注册");
                }
                //修改订单状态
                TOrderDto udpateOrder = new TOrderDto();
                String car_id = StringUtil.trim(map.get("car_id"));
                if(routeType.equals("0")){
                    //判断当前车设备  是否与系统配置一样，不一样则提示车辆信息错误
                    String carId = queryOrder.getCarId();
                    if(!car_id.equals(carId)){
                        return ResultVo.error(-1,"车票与车辆未匹配");
                    }
                }else if(routeType.equals("1")){
                    //高峰则修改订单车辆信息
                    String keyDate = DateUtils.formatDate(nowTime, DateUtils.PATTERN_yyyy_MM_dd);
                    //将车辆与司机绑定的信息缓存
                    String driverId = redisUtil.get(RedisConstant.SYS_TODAY_CAR_DRIVER_BIND_VALUE + keyDate + car_id);
                    udpateOrder.setCarId(car_id);
                    udpateOrder.setDriverId(driverId);
                    //查询平峰行程
                    Map<String, Object> routePeakMap = routePeakService.getMap(new QueryWrapper<TRoutePeakDto>()
                            .select("id")
                            .eq("car_id",car_id)
                            .apply("to_char(create_date,'yyyy-MM-dd')={0}",DateUtils.formatDate(nowTime, Constants.DATE_PATTERN))
                            .eq("route_state","0")
                    );
                    if(routePeakMap == null){
                        log.info("当前司机没有行程.....");
                        return ResultVo.error(-1,"当前司机没有行程");
                    }
                    String routeId = StringUtil.trim(routePeakMap.get("id"));
                    udpateOrder.setRouteId(routeId);
                    queryOrder.setRouteId(routeId);
                }
                udpateOrder.setId(id);
                //2进行中
                udpateOrder.setOrderStatus("2");
                udpateOrder.setUpdateId(param.getToken());
                udpateOrder.setUpdateDate(nowTime);
                udpateOrder.setRideStartDate(nowTime);
                boolean update = orderService.updateById(udpateOrder);

                if(update){
                    //添加上车记录，修改上车人数
                    TRouteStationDto routeStation = routeStationService.getOne(new QueryWrapper<TRouteStationDto>()
                        .eq("route_id",queryOrder.getRouteId())
//                        .eq("driver_id",queryOrder.getDriverId())
                        .apply("to_char(create_date,'yyyy-MM-dd')={0}",DateUtils.formatDate(nowTime, Constants.DATE_PATTERN))
                        .orderByDesc("create_date")
                            .last("limit 1")
                    );
                    //如果没有记录则添加上车人数
                    if(routeStation != null){
                        routeStation.setOnNum(StringUtil.getAsInt(StringUtil.trim(routeStation.getOnNum()),0)+1);
                        routeStationService.updateById(routeStation);
                    }else{
                        routeStation = new TRouteStationDto();
                        routeStation.setCreateDate(nowTime);
                        routeStation.setRouteType(routeType);
                        routeStation.setDriverId(queryOrder.getDriverId());
                        routeStation.setRouteId(queryOrder.getRouteId());
                        routeStation.setStationId(queryOrder.getStartStationId());
                        routeStation.setOnNum(queryOrder.getPassengersNum());
                        routeStationService.save(routeStation);
                    }
                    return ResultVo.success() ;
                }
                vo = ResultVo.error(ResultConstant.SYS_ORDER_HAS_EXPIRED_ERROR, ResultConstant.SYS_ORDER_HAS_EXPIRED_ERROR_VALUE);
                break;
            case "4":
                vo = ResultVo.error(ResultConstant.SYS_ORDER_HAS_EXPIRED_ERROR, ResultConstant.SYS_ORDER_HAS_EXPIRED_ERROR_VALUE);
                break;
            default:
                vo = ResultVo.error(ResultConstant.SYS_QCCODE_HAS_USERED_ERROR, ResultConstant.SYS_QCCODE_HAS_USERED_ERROR_VALUE);
                break;
        }
        return vo;
    }


    /**
     * 申请微信退款
     *
     * @param paramData
     * @return
     */
    @PostMapping(value = "/applyRefund")
    @Transactional
    ResultVo applyRefund(@RequestBody Map<String, Object> paramData) {
        //获取订单号
        String orderNo = StringUtil.trim(paramData.get("orderNo"));
        if (StringUtil.isEmpty(StringUtil.trim(orderNo))) {
            return ResultVo.error(ResultVo.Status.REQUIRED_PARAMETER_ERROR);
        }
        Map<String, Object> orderMap = orderService.getMap(new QueryWrapper<TOrderDto>()
                .select("id", "order_amount","user_id", "order_status", "is_pay", "trade_no")
                .eq("order_no", orderNo)
        );
        if (null == orderMap) {
            return ResultVo.error(ResultVo.Status.SYS_ORDER_DATA_ERROR);
        }
        String is_pay = StringUtil.trim(orderMap.get("is_pay"));
        String order_status = StringUtil.trim(orderMap.get("order_status"));
        if (is_pay.equals("0")) {//订单未支付
            return ResultVo.error(ResultVo.Status.SYS_ORDER_HAVE_PAID_ERROR);
        }
        if (!order_status.equals("1")) {//订单状态 1待使用
            return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, "只有待使用的才能申请退款");
        }


       /* //查询用户电话号，根据电话号在员工表内查询是否存在
        String userId=StringUtil.trim(orderMap.get("user_id"));
        TUserDto user=tUserService.getById(userId);
        //判断是否为内部(无需调用微信退款）/外部人员(需调用微信退款）
        TCompanyInfoDto companyInfoDto=tCompanyInfoService.getOne(new QueryWrapper<TCompanyInfoDto>()
                    .select("id","user_name","dept_name","phone")
                    .eq("phone", user.getPhone())
        );
        //companyInfoDto!=null*/
        Map<String, String> refundMap=new HashMap<>();

        if("0".equals(StringUtil.trim(orderMap.get("order_amount")))){
             //不调用微信退款接口
            refundMap.put("state", "0");
            //添加退款记录
            String orderId =StringUtil.trim(orderMap.get("id"));
            TOrderRefundsDto refund = new TOrderRefundsDto();
            refund.setRefundNo("mf_"+orderId);
            refund.setRefundStatus("1");
            refund.setOrderId(orderId);
            refund.setCreateDate(new Date());
            refund.setRefundAmount(new BigDecimal(0));
            boolean save = orderRefundsService.save(refund);

        }else{
             refundMap = createRefundInfo(orderMap);
        }


        if (!StringUtil.trim(refundMap.get("state")).equals("0")) {
            return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, refundMap.get("err_code_des"));
        }
        //修改订单状态为退款完成
        TOrderDto orderDto = new TOrderDto();
        orderDto.setId(StringUtil.trim(orderMap.get("id")));
        orderDto.setOrderStatus("4");
        orderDto.setIsRefund("1");
        boolean b = orderService.updateById(orderDto);
        System.out.println(" == " + b);
        return ResultVo.success();
    }

    /**
     * 生成退款信息
     *
     * @param refundParams
     * @return
     */
    private Map<String, String> createRefundInfo(Map<String, Object> refundParams) {
        Map<String, String> respRefund = Maps.newHashMap();
        // 微信支付
        try {
            Map<String, String> reqData = new HashMap<String, String>();
            MyConfig myConfig = new MyConfig();
            reqData.put("appid", myConfig.getAppID());
            reqData.put("mch_id", myConfig.getMchID());
            reqData.put("nonce_str", WXPayUtil.generateNonceStr());
            reqData.put("sign_type", "MD5");
            reqData.put("out_trade_no", Md5.MD5Encode(StringUtils.trim(refundParams.get("trade_no").toString()), "utf-8"));
            String order_amount = StringUtil.trim(refundParams.get("order_amount"));
            String orderAmount = StringUtil.trim(new BigDecimal(order_amount).multiply(new BigDecimal(100)).intValue());
            reqData.put("total_fee",orderAmount);
            reqData.put("refund_fee", orderAmount);
            reqData.put("refund_fee_type", "CNY");
            reqData.put("refund_desc", StringUtil.trim(refundParams.get("refund_desc")));
            reqData.put("out_refund_no", CreateUtils.createOrderCode("ORN_"));
            reqData.put("sign", WXPayUtil.generateSignature(reqData, myConfig.getKey()));
            respRefund = wxpay.refund(reqData);
            String return_code = StringUtil.trim(respRefund.get("return_code"));
            String return_msg = StringUtil.trim(respRefund.get("return_msg"));
            //请求成功
            if (return_code.equals("SUCCESS") && return_msg.equals("OK") && respRefund.get("result_code").equals("SUCCESS")) {
                TOrderRefundsDto refund = new TOrderRefundsDto();
                //添加退款记录
                String orderId = StringUtil.trim(refundParams.get("id"));
                refund.setRefundNo(respRefund.get("out_refund_no"));
                refund.setRefundStatus("1");
                refund.setOrderId(orderId);
                refund.setCreateDate(new Date());
                refund.setRefundAmount(new BigDecimal(respRefund.get("refund_fee")).divide(new BigDecimal(100)));
                boolean save = orderRefundsService.save(refund);
                //修改订单状态为退款完成
//                TOrderDto orderDto = new TOrderDto();
//                orderDto.setId(orderId);
//                orderDto.setIsRefund("2");
//                boolean b = orderService.updateById(orderDto);
//                System.out.println(b);
                respRefund.put("state", "0");
            } else {
                respRefund.put("state", "1");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respRefund;
    }


    /**
     * 获取司机行程列表
     *
     * @param data
     * @return
     */
    @PostMapping("/getDriverRouteList")
    public ResultVo getDriverRouteList(@RequestBody Map<String, Object> data) {

        if (StringUtil.isEmpty(StringUtil.trim(data.get("driverId")))) {
            return ResultVo.error(ResultConstant.SYS_REQUIRED_PARAMETER_ERROR, ResultConstant.SYS_REQUIRED_PARAMETER_ERROR_VALUE);
        }
        int pageIndex = StringUtil.getAsInt(StringUtil.trim(data.get("pageIndex")), 1);
        int pageSize = StringUtil.getAsInt(StringUtil.trim(data.get("pageSize")), 10);

        ResultVo resultVo = new AbstractPageTemplate<Map>() {
            @Override
            protected List<Map> executeSql() {

                List<Map> list = orderService.getOrderUser(StringUtil.trim(data.get("driverId")));
                return list;
            }
        }.preparePageTemplate(pageIndex, pageSize);
        return resultVo;
    }


    /**
     * 系统自动取消出行订单
     * @return
     */
    @PostMapping(value = "/sysCancalOrder")
    @Transactional
    ResultVo sysCancalOrder(){
        log.info("start sysCancalOrder scheduled!");
        Map<String,Object> condition = Maps.newHashMap();
        Date date = new Date();
        //查询平峰即时出行的订单数据
        QueryWrapper queryWrapper =new QueryWrapper();
        queryWrapper.select(
                "id","trade_no","trip_time","user_id","create_date","order_status","order_amount");
        queryWrapper.isNull("car_id");
        queryWrapper.eq("order_type","0");//实现出行
        queryWrapper.eq("is_pay","1");//已支付
        queryWrapper.eq("order_status","1");//待使用
        queryWrapper.eq("route_type","0");// 0 平峰 1高峰
        queryWrapper.apply("to_char(create_date,'yyyy-MM-dd')={0}",DateUtils.formatDate(date, Constants.DATE_PATTERN));
        queryWrapper.lt("create_date",DateUtils.addMinutes(date,-3));

        //保存订单列表
        List<Map<String,Object>> orderList = orderService.listMaps(queryWrapper);
        if(orderList != null && orderList.size()>0){
            List<String> ids = Lists.newArrayList();
            for (Map<String, Object> order : orderList) {
                ids.add(StringUtil.trim(order.get("id")));
            }
            condition.put("order_status","4");
            condition.put("is_cancel","2");
            condition.put("is_refund","2");
            condition.put("update_date",date);
            condition.put("ids",ids);
            int i = orderService.batchUpdateByCondition(condition);
            if(i>0){
                for (Map<String, Object> order : orderList) {
                    createRefundInfo(order);
                }
            }
        }

        queryWrapper =new QueryWrapper();
        queryWrapper.isNull("car_id");
        queryWrapper.eq("order_type","1");//预约出行
        queryWrapper.eq("is_pay","1");//已支付
        queryWrapper.eq("order_status","1");//待使用
        queryWrapper.eq("route_type","0");// 0 平峰 1高峰
        queryWrapper.apply("to_char(create_date,'yyyy-MM-dd')={0}",DateUtils.formatDate(date, Constants.DATE_PATTERN));
        queryWrapper.lt("trip_time",date);
        queryWrapper.select("id","trade_no","trip_time","user_id","create_date","order_status","order_amount");
        //保存订单列表
        List<Map<String,Object>> orderList1 = orderService.listMaps(queryWrapper);
        if(orderList1 != null && orderList1.size()>0){
            List<String> ids = Lists.newArrayList();
            for (Map<String, Object> order : orderList1) {
                ids.add(StringUtil.trim(order.get("id")));
            }
            condition.clear();
            condition.put("order_status","4");
            condition.put("is_cancel","2");
            condition.put("is_refund","2");
            condition.put("update_date",date);
            condition.put("ids",ids);
            int i = orderService.batchUpdateByCondition(condition);
            if(i>0){
                for (Map<String, Object> order : orderList1) {
                    createRefundInfo(order);
                }
            }
        }
        log.info("end sysCancalOrder scheduled!");
        return ResultVo.success();
    }


    /**
     * 查询高峰最近车辆
     * @param condition
     * @return
     */
    @PostMapping(value = "/queryPeakOrderInfo")
    ResultVo queryPeakOrderInfo(@RequestBody Map<String, Object> condition) throws Exception {
        QueryWrapper queryWrapper =new QueryWrapper();
        queryWrapper.select("id","start_station_id","line_id");
        queryWrapper.eq("id",condition.get("id"));//订单id
        queryWrapper.eq("is_pay","1");//已支付
        queryWrapper.eq("order_status","1");//待使用
//        queryWrapper.apply("to_char(create_date,'yyyy-MM-dd')={0}",DateUtils.formatDate(new Date(), Constants.DATE_PATTERN));

        //保存订单列表
        Map<String,Object> orderMap = orderService.getMap(queryWrapper);
        if(orderMap == null ){
            return ResultVo.error(ResultVo.Status.SYS_ORDER_DATA_ERROR);
        }
        //获取车辆位置
        String lineVehiclesCache = redisUtil.get("maas_arrival_time_of_vehicles"+StringUtil.trim(orderMap.get("line_id")));
        if(StringUtil.isEmpty(lineVehiclesCache)){
            log.info("暂无用户高峰订单线路最近的车辆信息");
            return ResultVo.error(ResultConstant.PLEASE_TRY_AGAIN_LATER,ResultConstant.PLEASE_TRY_AGAIN_LATER_VALUE);
        }

        String start_station_id = StringUtil.trim(orderMap.get("start_station_id"));
        List<Map<Object, Object>> linesList  = JsonUtil.parseJSON2List(lineVehiclesCache);

        Date date = new Date();
        Map<String,Object> result  = Maps.newHashMap();
        if(linesList.size() == 1){
            Map<String,Object> stationArivalTime = (Map<String, Object>) linesList.get(0).get("stationArivalTime");
            int arrivaltime= (int) Math.ceil(StringUtil.getDouble(StringUtil.trim(stationArivalTime.get(start_station_id))));
            result.put("arrivaltime",DateUtils.addMinutes(date, arrivaltime).getTime());
            result.put("vehicleID",linesList.get(0).get("vehicleId"));
            result.put("car_no",linesList.get(0).get("car_no"));
            return ResultVo.success().setDataSet(result);
        }

        Double min = 100d;
        Map<Object, Object> line = Maps.newHashMap();
        for (Map<Object, Object> lines : linesList) {
            Map<String,Object> stationArivalTime = (Map<String, Object>) lines.get("stationArivalTime");
            if(stationArivalTime == null ){
                continue;
            }
            Double arrivaltime = StringUtil.getDouble(StringUtil.trim(stationArivalTime.get(start_station_id)),120d);
//            for (Map<String, Object> station : stationArivalTime) {
//                for (Map.Entry<String, Object> entry : station.entrySet()) {
//                    if(entry.getKey().equals(start_station_id)){
////                        result.put("arrivaltime",entry.getValue());
//                        arrivaltime = (Double) entry.getValue();
//                    }
//                }
//                break;
//            }
            if(min > arrivaltime){
                min = arrivaltime;
                line = lines;
            }
        }
        result.put("vehicleID",line.get("vehicleId"));
        result.put("car_no",line.get("car_no"));
        if(min!= 100d){
            int arrivaltime= (int) Math.ceil(min);
            result.put("arrivaltime",DateUtils.addMinutes(date, arrivaltime).getTime());
        }
        return ResultVo.success().setDataSet(result);

    }



    /**
     * 查询高峰最近车辆(copy)
     * @param condition
     * @return
     */
    @PostMapping(value = "/queryPeakOrderInfo_copy")
    ResultVo queryPeakOrderInfo_copy(@RequestBody Map<String, Object> condition) throws Exception {
        QueryWrapper queryWrapper =new QueryWrapper();
        queryWrapper.select("id","start_station_id","line_id");
        queryWrapper.eq("id",condition.get("id"));//订单id
        queryWrapper.eq("is_pay","1");//已支付
        queryWrapper.eq("order_status","1");//待使用
//        queryWrapper.apply("to_char(create_date,'yyyy-MM-dd')={0}",DateUtils.formatDate(new Date(), Constants.DATE_PATTERN));

        //保存订单列表
        Map<String,Object> orderMap = orderService.getMap(queryWrapper);
        if(orderMap == null ){
            return ResultVo.error(ResultVo.Status.SYS_ORDER_DATA_ERROR);
        }
        //获取车辆位置
        String orderLineCache = redisUtil.get(RedisConstant.MAAS_PEAK_USER_ORDER_LINE_VALUE + orderMap.get("line_id"));
        if(StringUtil.isEmpty(orderLineCache)){
            log.info("暂无用户高峰订单线路最近的车辆信息");
            return ResultVo.error(ResultConstant.PLEASE_TRY_AGAIN_LATER,ResultConstant.PLEASE_TRY_AGAIN_LATER_VALUE);
        }
        List<Map<Object, Object>> userLineInfoList = JsonUtil.parseJSON2List(orderLineCache);
        if(userLineInfoList== null || userLineInfoList.size()==0){
            return ResultVo.error(ResultConstant.PLEASE_TRY_AGAIN_LATER,ResultConstant.PLEASE_TRY_AGAIN_LATER_VALUE);
        }
        //根据用户上车的站点去查询车辆已经到了哪个站点
        String viaStationKey = redisUtil.get(RedisConstant.SYS_PEAK_VIA_STATIONS_CACHE_KEY);
        List<Map<Object, Object>> maps = JsonUtil.parseJSON2List(viaStationKey);
        String line_id = StringUtil.trim(orderMap.get("line_id"));

        for (int i = maps.size() - 1 ;i>= 0 ;i--) {
            Map<Object, Object> map= maps.get(i);
            if(!StringUtil.trim(map.get("line_id")).equals(line_id) ||  !map.get("type").equals(1.0d)){
                maps.remove(map);
            }else{
                Double index = StringUtil.getDouble(StringUtil.trim(map.get("index")));
                map.put("index",index.intValue());
            }
        }

        //过滤掉其他线上车辆的停靠信息
        maps.sort((a,b) -> StringUtil.getAsInt(StringUtil.trim(a.get("index"))) - StringUtil.getAsInt(StringUtil.trim(b.get("index"))));
        //获取起点
        String start_station_id = StringUtil.trim(orderMap.get("start_station_id"));
        //获取一条线上的所有车辆信息
        ResultVo success = ResultVo.success();
        for (Map<Object, Object> userLine : userLineInfoList) {
            //获取停靠站点信息
            int index = (int)StringUtil.getDouble(StringUtil.trim(userLine.get("index")));

            for (int i = 0 ;i < maps.size() ;i++) {
                Map<Object, Object> map  = maps.get(i);
                if(i == maps.size()-1){
                    break;
                }
                int index1 = (int)StringUtil.getDouble(StringUtil.trim(map.get("index")));
                int index2 = (int)StringUtil.getDouble(StringUtil.trim(maps.get(i+1).get("index")));
                if(index1 <= index && index <= index2){
                    if(StringUtil.trim(maps.get(i+1).get("station_id")).equals(start_station_id)){
                        success.setDataSet(userLine);
                        return success;
                    }
                }
            }
        }
        return ResultVo.error(ResultConstant.PLEASE_TRY_AGAIN_LATER,ResultConstant.PLEASE_TRY_AGAIN_LATER_VALUE);
    }

    /**
     * 首页获取用户最近使用的订单
     * @param condition
     * @return
     */
    @PostMapping(value = "/queryRecentOrderOrder")
    ResultVo queryRecentOrderOrder(@RequestBody Map<String, Object> condition){
        if(condition == null || condition.size() == 0){
            return ResultVo.error(ResultVo.Status.REQUIRED_PARAMETER_ERROR);
        }
        Map<String,Object> resultMap = Maps.newHashMap();
        String route_type = StringUtil.trim(condition.get("route_type"));
        QueryWrapper<TOrderDto> tOrderDtoQueryWrapper = new QueryWrapper<>();
                tOrderDtoQueryWrapper.select("id","start_station_id","end_station_id","line_id","order_status");
                tOrderDtoQueryWrapper.eq("user_id",condition.get("userId"));
                tOrderDtoQueryWrapper.eq("route_type",condition.get("route_type"));
                tOrderDtoQueryWrapper.orderByDesc("create_date");
                tOrderDtoQueryWrapper.last("limit 1");
        if(route_type.equals("1")){
            String SYS_PEAK_LINES = redisUtil.get("SYS_PEAK_LINES");
            if(!StringUtil.isEmpty(SYS_PEAK_LINES)){
                tOrderDtoQueryWrapper.in("line_id",Arrays.asList(SYS_PEAK_LINES.split(",")));
            }
        }
        Map<String,Object> orderMap = orderService.getMap(tOrderDtoQueryWrapper);
        resultMap.put("order",orderMap);
        //查询当前可用的司机
        List<Map<String, Object>> driverList = driverService.listMaps(new QueryWrapper<TDriverDto>()
                .select("id")
                .eq("status","1")
        );
        if(driverList==null || driverList.size()==0){
            log.info("当前没有可用的司机......");
            resultMap.put("car",null);
            return ResultVo.success().setDataSet(resultMap);
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
            resultMap.put("car",null);
            return ResultVo.success().setDataSet(resultMap);
        }
        List<String> carIds = Lists.newArrayList();
        driverCarBindLists.forEach(driverCar->carIds.add(StringUtil.trim(driverCar.get("car_id"))));
        condition.clear();
        condition.put("carIds",carIds);
        List<Map<String, Object>> maps = carPositionService.selectCurCarPostion(condition);
        resultMap.put("car",maps);
        return ResultVo.success().setDataSet(resultMap);
    }


    /**
     * wenbn
     * 显示车辆线路信息
     * @param condition
     * @return
     */
    @PostMapping(value = "/queryPeakLineInfo")
    ResultVo queryPeakLineInfo(@RequestBody Map<String, Object> condition){
        ResultVo resultVo = ResultVo.success();
        Map<String,Object> resultMap = Maps.newHashMap();
        String id = StringUtil.trim(condition.get("id"));
        if(!StringUtil.isEmpty(id)){
            Map<String,Object> orderMap = orderService.getMap(new QueryWrapper<TOrderDto>()
                    .select("id","route_id","route_type","start_station_id","end_station_id","line_id","order_position")
                    .eq("id",id)
//                    .eq("order_status","2")
            );
            //订单数据异常
            if(orderMap == null){
                return ResultVo.error(ResultVo.Status.REQUIRED_PARAMETER_ERROR);
            }
            List<Map<Object, Object>> maps = null;
            if(orderMap.get("route_id") == null){
//                resultVo.setMessage("暂无数据");
                String line_id = StringUtil.trim(orderMap.get("line_id"));
                String viaStationKey = redisUtil.get(RedisConstant.SYS_PEAK_VIA_STATIONS_CACHE_KEY);
                maps = JsonUtil.parseJSON2List(viaStationKey);
                for (int i = maps.size() - 1 ;i>= 0 ;i--) {
                    Map<Object, Object> map= maps.get(i);
                    if(!StringUtil.trim(map.get("line_id")).equals(line_id)){
                        maps.remove(map);
                        continue;
                    }
                    Double index = null;
                    try {
                        index = StringUtil.getDouble(StringUtil.trim(map.get("index")));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    map.put("index",index.intValue());
                }
                //过滤掉其他线上车辆的停靠信息
                maps.sort((a,b) -> StringUtil.getAsInt(StringUtil.trim(a.get("index"))) - StringUtil.getAsInt(StringUtil.trim(b.get("index"))));
                //查询所有的站点信息
                String stations = redisUtil.get(RedisConstant.SYS_STATIONS_CACHE_KEY);
                if (!StringUtil.isEmpty(stations)) {
                    List<Map<Object, Object>> stationsList = JsonUtil.parseJSON2List(stations);
                    List<Map<Object, Object>> newStations = Lists.newArrayList();
                    for (Map<Object, Object> map : maps) {
                        String station_id = StringUtil.trim(map.get("station_id"));
                        Double type = 0d;
                        try {
                            type = StringUtil.getDouble(StringUtil.trim(map.get("type")));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        for (Map<Object, Object> station : stationsList) {
                            String stationId = StringUtil.trim(station.get("id"));
                            if(station_id.equals(stationId)){
                                map.put("latitude",station.get("latitude"));
                                map.put("longitude",station.get("longitude"));
                            }
                        }
                        if(type == 1d){
                            newStations.add(map);
                        }
                    }
                    resultMap.put("station",newStations);
                    resultMap.put("position",maps);
                }
                resultVo.setDataSet(resultMap);
                return resultVo;
            }
            //判断订单类型 行程类型( 0 平峰 1高峰 )
            String route_type = StringUtil.trim(orderMap.get("route_type"));

            if(route_type.equals("0")){
                //
                String route_id = StringUtil.trim(orderMap.get("route_id"));
                Map<String, Object> routeOffpeakMap = routeOffpeakService.getMap(new QueryWrapper<TRouteOffpeakDto>()
                        .select("id", "midway_station", "curr_station", "all_position","route_state", "car_id")
                        .eq("id", route_id)
                        .eq("route_state", "0")
                );
                if(routeOffpeakMap == null){
                    resultVo.setMessage("暂无数据");
                    return resultVo;
                }
                String allPosition = StringUtil.trim(orderMap.get("order_position"));
                List<Map<Object, Object>> allPositions = JsonUtil.parseJSON2List(allPosition);
                String midway_station = StringUtil.trim(routeOffpeakMap.get("midway_station"));
                //查询所有的站点信息
                String stations = redisUtil.get(RedisConstant.SYS_STATIONS_CACHE_KEY);
                if (!StringUtil.isEmpty(stations)) {
                    List<Map<Object, Object>> stationsList = JsonUtil.parseJSON2List(stations);
                    maps = Lists.newArrayList();
                    int isFind = 0;
                    Map<Object, Object> startStation = Maps.newHashMap();
                    Map<Object, Object> endStation = Maps.newHashMap();
                    for (Map<Object, Object> station : stationsList) {
                        String id1 = StringUtil.trim(station.get("id"));
                        if(StringUtil.trim(orderMap.get("start_station_id")).equals(id1)){
                            Map<Object, Object> objectObjectMap = allPositions.get(0);
                            objectObjectMap.clear();
                            objectObjectMap.putAll(station);
//                            allPositions.add(0, station);
                            startStation.putAll(station);
                            isFind++;
                        }
                        if(StringUtil.trim(orderMap.get("end_station_id")).equals(id1)){
                            endStation.putAll(station);
//                            maps.add(station);
                            allPositions.add(station);
                            isFind++;
                        }
                        if(isFind == 2){
                            maps.add(startStation);
                            maps.add(endStation);
                            break;
                        }
                    }
                }
                resultMap.put("station",maps);
                resultMap.put("position",allPositions);
            }else{
                //
                String line_id = StringUtil.trim(orderMap.get("line_id"));
                String viaStationKey = redisUtil.get(RedisConstant.SYS_PEAK_VIA_STATIONS_CACHE_KEY);
                maps = JsonUtil.parseJSON2List(viaStationKey);
                for (int i = maps.size() - 1 ;i>= 0 ;i--) {
                    Map<Object, Object> map= maps.get(i);
                    if(!StringUtil.trim(map.get("line_id")).equals(line_id)){
                        maps.remove(map);
                    }else{
                        Double index = 0d;
                        try {
                            index = StringUtil.getDouble(StringUtil.trim(map.get("index")));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        map.put("index",index.intValue());
                    }
                }
                //过滤掉其他线上车辆的停靠信息
                maps.sort((a,b) -> StringUtil.getAsInt(StringUtil.trim(a.get("index"))) - StringUtil.getAsInt(StringUtil.trim(b.get("index"))));
                //查询所有的站点信息
                String stations = redisUtil.get(RedisConstant.SYS_STATIONS_CACHE_KEY);
                if (!StringUtil.isEmpty(stations)) {
                    List<Map<Object, Object>> stationsList = JsonUtil.parseJSON2List(stations);
                    List<Map<Object, Object>> newStations = Lists.newArrayList();
                    for (Map<Object, Object> map : maps) {
                        String station_id = StringUtil.trim(map.get("station_id"));
                        String type = StringUtil.trim(map.get("type"));
                        for (Map<Object, Object> station : stationsList) {
                            String stationId = StringUtil.trim(station.get("id"));
                            if(station_id.equals(stationId)){
                                map.put("latitude",station.get("latitude"));
                                map.put("longitude",station.get("longitude"));
                            }
                        }
                        if(type.equals("1")){
                            newStations.add(map);
                        }
                    }
                    resultMap.put("station",newStations);
                    resultMap.put("position",maps);
                }
            }
        }
        resultVo.setDataSet(resultMap);
        return resultVo;
    }

    /**
     * PC首页 查询昨日订单，上周订单，本周订单，与计算增长比（本周/上周）
     *
     * @return ResultVo
     */
    @GetMapping("/getPcIndex")
    public ResultVo getPcIndex() {
            SimpleDateFormat format  = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DATE, - 7);
            calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
            String lastWeekStart = format.format(calendar.getTime())+" 00:00:00";
            System.out.println(lastWeekStart);
            Timestamp lastStartTime = Timestamp.valueOf(lastWeekStart);
            Calendar tCalendar = calendar;
            tCalendar.setFirstDayOfWeek(Calendar.MONDAY);
            tCalendar.set(Calendar.DAY_OF_WEEK, tCalendar.getFirstDayOfWeek() + 6);
            String lastWeekEnd = format.format(tCalendar.getTime())+" 23:59:59";
            Timestamp lastEndTime = Timestamp.valueOf(lastWeekEnd);
            QueryWrapper<TOrderDto> queryWrapper =new QueryWrapper<>();
            queryWrapper.between("create_date",lastStartTime,lastEndTime);
            Integer lastWeekTotal =orderService.list(queryWrapper).size();
        Calendar day = Calendar.getInstance();
        day.setTime(new Date());
        day.add(Calendar.DATE, - 1);
        Date dayTime = day.getTime();
        String yesterday = format.format(dayTime);
       QueryWrapper<TOrderDto> orderDtoQueryWrapper =new QueryWrapper<>();
       orderDtoQueryWrapper.apply("to_char(create_date,'yyyy-MM-dd')={0}",yesterday);
       Integer dayTotal = orderService.list(orderDtoQueryWrapper).size();

        Calendar dayCalendar = Calendar.getInstance();
        dayCalendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
        String weekStart = format.format(dayCalendar.getTime())+" 00:00:00";
        Timestamp startTime = Timestamp.valueOf(weekStart);
        Timestamp endTime =  Timestamp.valueOf(format.format(dayTime)+" 23:59:59");
        QueryWrapper<TOrderDto> weekQueryWrapper = new QueryWrapper<>();
        weekQueryWrapper.between("create_date",startTime,endTime);
        Integer weekTotal = orderService.list(weekQueryWrapper).size();
        BigDecimal weekTotalSize = new BigDecimal(weekTotal);
        if (lastWeekTotal!=0) {
            weekTotalSize = weekTotalSize.divide(new BigDecimal(lastWeekTotal), 2, RoundingMode.HALF_UP);
        }
        Map map =new HashMap(3);
        map.put("dayTotal",dayTotal);
        map.put("weekTotal",weekTotal);
        if (lastWeekTotal!=0) {
            map.put("increase",weekTotalSize);
        }else {
            map.put("increase","");
        }

        return ResultVo.success().setDataSet(map);
    }
    /**
     * 耿东雪
     * 保存流水
     * @param condition
     * @return
     */
    @PostMapping(value = "/savePaySerial")
    ResultVo savePaySerial(@RequestBody Map<String, Object> condition){

        QueryWrapper<TOrderDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(
                    "id", "trade_no", "passengers_num", "trip_time", "start_station_id",
                    "end_station_id", "user_id", "create_date", "order_status",
                    "is_pay", "order_amount", "route_type", "trip_time","exp_date"
        );
        queryWrapper.eq("order_no", condition.get("orderNo"));
        Map<String, Object> orderMap = orderService.getMap(queryWrapper);
        String orderId = StringUtil.trim(orderMap.get("id"));
        String order_amount = StringUtil.trim(orderMap.get("order_amount"));
        Date updateDate = new Date();
        long time = updateDate.getTime();
        Date exp_date = (Date) orderMap.get("exp_date");
        redisUtil.set("orderExp=" + orderId, orderId, (exp_date.getTime() - time) / 1000);

        TPaySerialDto paySerialDto = new TPaySerialDto();
        paySerialDto.setOrderId(orderId);
        try {
            paySerialDto.setPayAmount(StringUtil.getDouble(StringUtil.trim(order_amount)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        paySerialDto.setCreateDate(updateDate);
        paySerialDto.setPayStatus("1");
        paySerialDto.setUserId("1");
        if(paySerialService.save(paySerialDto)){
           return ResultVo.error(ResultConstant.SYS_REQUIRED_SUCCESS, ResultConstant.SYS_ORDER_HAVE_PAID_ERROR_VALUE);
        }else{
            return ResultVo.error(ResultConstant.HTTP_STATUS_BAD_REQUEST, ResultConstant.HTTP_STATUS_BAD_REQUEST_VALUE);
        }

    }


}