package com.yihexinda.userweb.api;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.netflix.discovery.converters.Auto;
import com.yihexinda.core.constants.RedisConstant;
import com.yihexinda.core.constants.ResultConstant;
import com.yihexinda.core.utils.*;
import com.yihexinda.data.dto.TStationDto;
import com.yihexinda.userweb.client.SysParamClient;
import com.yihexinda.userweb.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yihexinda.core.constants.Constants;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TOrderDto;
import com.yihexinda.userweb.client.OrderClient;
import com.yihexinda.userweb.client.TicketManageClient;
import com.yihexinda.userweb.utils.SnowflakeIdWorker;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 购票管理，管理预约出行及即实出行
 * @author wenbn
 * @version 1.0
 * @date 2018/12/1 0001
 */
@Api(description = "C端购票管理")
@RestController()
@RequestMapping("/api/ticket")
public class TicketManageResource {

    @Autowired
    private TicketManageClient ticketManageClient;
    @Autowired
    private OrderClient orderClient;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SysParamClient sysParamClient;


    /**
     * 检查预约数据
     * @return
     */
    @PostMapping("/check/appoTime")
    @ApiOperation(value = "检查预约数据", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appoTime", value = "预约时间" , required = true,dataType = "String"),
            @ApiImplicitParam(name = "ticketNumber", value = "预约人数" , required = true,dataType = "int"),
    })
    public ResultVo checkAppointmentTime(@RequestBody Map<String,Object> condition){
        //预约时间
        String appoTime = StringUtil.trim(condition.get("appoTime"));
        //预约人数
        int ticketNumber = StringUtil.getAsInt(StringUtil.trim(condition.get("ticketNumber")),1);
        //如果预约时间为空，则为实时出行
        if(StringUtil.isEmpty(appoTime)){
            return ticketNumber == 1 ?  ResultVo.success(): ResultVo.error(ResultConstant.SYS_APPOINTMENT_FAILURE,"预约出行");
        }
        if(ticketNumber>5){
            return ResultVo.error(ResultVo.Status.REQUIRED_PARAMETER_ERROR);
        }
        return ticketManageClient.checkAppointment(condition);
    }

    /**
     * 添加购票入口
     * @param data
     * @return
     */
    @PostMapping("/buy")
    @ApiOperation(value = "购票", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startStationId", value = "起始站" , required = true,dataType = "String"),
            @ApiImplicitParam(name = "endStationId", value = "终点站" , required = true,dataType = "String"),
            @ApiImplicitParam(name = "appoTime", value = "预约时间" , required = true,dataType = "Date"),
            @ApiImplicitParam(name = "token", value = "token" , required = true,dataType = "String"),
            @ApiImplicitParam(name = "lineId", value = "高峰线路id" , required = false,dataType = "String"),
            @ApiImplicitParam(name = "ticketNumber", value = "预约人数" , required = true,dataType = "int")
    })
    @Transactional
    public ResultVo buyTicket(@ApiIgnore @RequestBody Map<String,Object> data) throws Exception {
        //保存高峰线路ID
        String lineId = StringUtil.trim(data.get("lineId"));
        //        //起始站
        String startStationId = StringUtil.trim(data.get("startStationId"));
        //终点站
        String endStationId = StringUtil.trim(data.get("endStationId"));

        String viaStationKey = redisUtil.get(RedisConstant.SYS_PEAK_VIA_STATIONS_CACHE_KEY);
        List<Map<Object, Object>> maps = JsonUtil.parseJSON2List(viaStationKey);
        List<String> lineIds = Lists.newArrayList();

        for (Map<Object, Object> map : maps) {
            String station_id = StringUtil.trim(map.get("station_id"));
            if(startStationId.equals(station_id)){
                String line_id = StringUtil.trim(map.get("line_id"));
                Double index = StringUtil.getDouble(StringUtil.trim(map.get("index")));
                lineIds.add(line_id);
            }
        }
        for (Map<Object, Object> map : maps) {
            String station_id = StringUtil.trim(map.get("station_id"));
            if(endStationId.equals(station_id)){
                String line_id = StringUtil.trim(map.get("line_id"));
                if(lineIds.contains(line_id)){
                    lineId = line_id;
                    break;
                }
            }
        }

        //判断起始站点是否为空
        if(StringUtil.isEmpty(startStationId)|| StringUtil.isEmpty(endStationId)){
            return ResultVo.error(ResultVo.Status.REQUIRED_PARAMETER_ERROR);
        }
        if(startStationId.equals(endStationId)){
            return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE,"起始站和终点站不能相同");
        }
        //预约时间
        String appoTime = StringUtil.trim(data.get("appoTime"));
        //获取token
        String token = StringUtil.trim(data.get("token"));
        //解析token数据
        String userId = RequestUtil.analysisToken(token).getUserId();

        //订单信息表
        TOrderDto order =  new TOrderDto();
        order.setCreateId(userId);
        order.setUserId(userId);
        if(!StringUtil.isEmpty(appoTime)){
            //检测预约
            ResultVo resultVo = ticketManageClient.checkAppointment(data);
            if(resultVo.getResult() != 0 ){
                return resultVo;
            }
            order.setTripTime(DateUtils.parseDate(appoTime, Constants.DATE_FULL_TIME_PATTERN));
        }
        //预约人数
        int ticketNumber = StringUtil.getAsInt(StringUtil.trim(data.get("ticketNumber")),1);

        if(ticketNumber == 0 || ticketNumber>5){
            return ResultVo.error(ResultVo.Status.REQUIRED_PARAMETER_ERROR);
        }
        order.setOrderNo(CreateUtils.createOrderCode("ON_"));
        order.setTradeNo(StringUtil.trim(new SnowflakeIdWorker(2,1).nextId()));
        order.setPassengersNum(ticketNumber);
        String ticketType ="0";
        String routeType ="0";

        //判断类型 0：平峰即时  1平峰预约 2:高峰
        Date nowTime = new Date();
//        nowTime = DateUtils.parseDate("2018-12-26 07:45:00",DateUtils.PATTERN_yyyy_MM_dd_HH_mm_ss);
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


        Date expDate = null;
        //预约出行，过期时间则加1小时
        int oneHour= 60 * 60 * 1000;
        if (currentTime.after(stratTime) && currentTime.before(endTime)){
            //高峰时段预约平峰出行
            if(!StringUtil.isEmpty(appoTime)){
                order.setOrderType("1");
                if(ticketNumber > 1){
                    order.setTicketType("1");
                }
                expDate = new Date(DateUtils.parseDate(appoTime,DateUtils.PATTERN_yyyy_MM_dd_HH_mm_ss).getTime()+oneHour);
            }else{
                //高峰出现，购票人数只能1人
                if(ticketNumber != 1){
                    return ResultVo.error(-1,ResultConstant.SYS_PEAK_PURCHASE_LIMITONE_ERROR_VALUE);
                }
                routeType = "1";
                order.setOrderType("0");
                order.setLineId(lineId);
                //设置过期时间
                expDate = DateUtils.getDayEnd();
            }
            order.setExpDate(expDate);
        }else {
            if(!StringUtil.isEmpty(appoTime)){
                order.setOrderType("1");
                if(ticketNumber > 1){
                    order.setTicketType("1");
                }
                expDate = new Date(DateUtils.parseDate(appoTime,DateUtils.PATTERN_yyyy_MM_dd_HH_mm_ss).getTime()+oneHour);
            }else{
                order.setOrderType("0");
                if(ticketNumber > 1){
                    ticketType = "1";
                }
                expDate = new Date(nowTime.getTime()+oneHour);
            }
            order.setExpDate(expDate);
        }
        //检测是否还有未出行的票
        Map<String,Object> condition = Maps.newHashMap();
        condition.put("userId",userId);
        condition.put("routeType",routeType);
        ResultVo checkOrderVo = ticketManageClient.checkExitsNotTravelPeak(condition);
        if(checkOrderVo.getResult() != 0 ){
            return checkOrderVo;
        }
        //用车状态( 0 单票  1多票 )
        order.setTicketType(ticketType);
        //行程类型( 0 平峰 1高峰 )
        order.setRouteType(routeType);
        //订单状态( 0 创建订单 1待使用 2进行中 3已完成 4已失效 )
        order.setOrderStatus("0");
        //待支付
        order.setIsPay("0");
        order.setCreateDate(nowTime);
        order.setIsRefund("0");
        order.setStartStationId(startStationId);
        order.setIsCancel("0");
        order.setEndStationId(endStationId);

        //计算票价
        BigDecimal num = new BigDecimal(ticketNumber);
        if(routeType.equals("1")){
            String peakPrice = redisUtil.get(RedisConstant.SYS_PEAK_PRICE);
            order.setOrderAmount(new BigDecimal(peakPrice).multiply(num));
        }else{
            String offpeakPrice = redisUtil.get(RedisConstant.SYS_OFFPEAK_PRICE);
            order.setOrderAmount(new BigDecimal(offpeakPrice).multiply(num));
        }
        //计算站点距离
        String stations = redisUtil.get(RedisConstant.SYS_STATIONS_CACHE_KEY);
        if(!StringUtil.isEmpty(stations)){
            List<Map<Object,Object>> stationLists = JsonUtil.parseJSON2List(stations);
            for (int i = stationLists.size()-1;i>=0;i--) {
                Map<Object, Object> station= stationLists.get(i);
                String id = StringUtil.trim(station.get("id"));
                if(id.equals(startStationId) || id.equals(endStationId)){
                    continue;
                }
                stationLists.remove(station);
            }
            double latitude = StringUtil.getDouble(StringUtil.trim(stationLists.get(0).get("latitude")));
            double longitude = StringUtil.getDouble(StringUtil.trim(stationLists.get(0).get("longitude")));
            double latitude1 = StringUtil.getDouble(StringUtil.trim(stationLists.get(1).get("latitude")));
            double longitude1 = StringUtil.getDouble(StringUtil.trim(stationLists.get(1).get("longitude")));
            double v = MapUtil.GetDistance(longitude, latitude, longitude1, latitude1);
            order.setSiteDis(StringUtil.trim(v));
        }
        ResultVo resultVo = orderClient.addOrder(order);
        if(resultVo.getResult() == 0){
            String orderId = (String) resultVo.getDataSet();
            Map<String,Object> resultMap = Maps.newLinkedHashMapWithExpectedSize(3);
            resultMap.put("orderId",orderId);
            resultMap.put("orderNo",order.getOrderNo());
            resultMap.put("orderAmount",order.getOrderAmount());
            redisUtil.set(RedisConstant.SYS_CREATE_ORDER_KEY_EXPIRE_PREFIX+"&"+orderId, order.getOrderNo(), (long)180);
            resultVo.setDataSet(resultMap);
        }
        return resultVo;
    }



    /**
     * 添加购票员工入口
     * @param data
     * @return
     */
    @PostMapping("/buyTicketByCompany")
    @ApiOperation(value = "购票", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startStationId", value = "起始站" , required = true,dataType = "String"),
            @ApiImplicitParam(name = "endStationId", value = "终点站" , required = true,dataType = "String"),
            @ApiImplicitParam(name = "appoTime", value = "预约时间" , required = true,dataType = "Date"),
            @ApiImplicitParam(name = "token", value = "token" , required = true,dataType = "String"),
            @ApiImplicitParam(name = "lineId", value = "高峰线路id" , required = false,dataType = "String"),
            @ApiImplicitParam(name = "ticketNumber", value = "预约人数" , required = true,dataType = "int")
    })
    @Transactional
    public ResultVo buyTicketByCompany(@ApiIgnore @RequestBody Map<String,Object> data) throws Exception {
        //保存高峰线路ID
        String lineId = StringUtil.trim(data.get("lineId"));
        //        //起始站
        String startStationId = StringUtil.trim(data.get("startStationId"));
        //终点站
        String endStationId = StringUtil.trim(data.get("endStationId"));

        String viaStationKey = redisUtil.get(RedisConstant.SYS_PEAK_VIA_STATIONS_CACHE_KEY);
        List<Map<Object, Object>> maps = JsonUtil.parseJSON2List(viaStationKey);
        List<String> lineIds = Lists.newArrayList();

        for (Map<Object, Object> map : maps) {
            String station_id = StringUtil.trim(map.get("station_id"));
            if(startStationId.equals(station_id)){
                String line_id = StringUtil.trim(map.get("line_id"));
                Double index = StringUtil.getDouble(StringUtil.trim(map.get("index")));
                lineIds.add(line_id);
            }
        }
        for (Map<Object, Object> map : maps) {
            String station_id = StringUtil.trim(map.get("station_id"));
            if(endStationId.equals(station_id)){
                String line_id = StringUtil.trim(map.get("line_id"));
                if(lineIds.contains(line_id)){
                    lineId = line_id;
                    break;
                }
            }
        }

        //判断起始站点是否为空
        if (StringUtil.isEmpty(startStationId) || StringUtil.isEmpty(endStationId)) {
            return ResultVo.error(ResultVo.Status.REQUIRED_PARAMETER_ERROR);
        }
        if (startStationId.equals(endStationId)) {
            return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, "起始站和终点站不能相同");
        }
        //预约时间
        String appoTime = StringUtil.trim(data.get("appoTime"));
        //获取token
        String token = StringUtil.trim(data.get("token"));
        //解析token数据
        String userId = RequestUtil.analysisToken(token).getUserId();

        //订单信息表
        TOrderDto order =  new TOrderDto();
        order.setCreateId(userId);
        order.setUserId(userId);
        if(!StringUtil.isEmpty(appoTime)){
            //检测预约
            ResultVo resultVo = ticketManageClient.checkAppointment(data);
            if(resultVo.getResult() != 0 ){
                return resultVo;
            }
            order.setTripTime(DateUtils.parseDate(appoTime, Constants.DATE_FULL_TIME_PATTERN));
        }
        //预约人数
        int ticketNumber = StringUtil.getAsInt(StringUtil.trim(data.get("ticketNumber")),1);

        if(ticketNumber == 0 || ticketNumber>5){
            return ResultVo.error(ResultVo.Status.REQUIRED_PARAMETER_ERROR);
        }
        order.setOrderNo(CreateUtils.createOrderCode("ON_"));
        order.setTradeNo(StringUtil.trim(new SnowflakeIdWorker(2,1).nextId()));
        order.setPassengersNum(ticketNumber);
        String ticketType ="0";
        String routeType ="0";

        //判断类型 0：平峰即时  1平峰预约 2:高峰
        Date nowTime = new Date();
//        nowTime = DateUtils.parseDate("2018-12-26 07:45:00",DateUtils.PATTERN_yyyy_MM_dd_HH_mm_ss);
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


        Date expDate = null;
        //预约出行，过期时间则加1小时
        int oneHour= 60 * 60 * 1000;
        if (currentTime.after(stratTime) && currentTime.before(endTime)){
            //高峰时段预约平峰出行
            if(!StringUtil.isEmpty(appoTime)){
                order.setOrderType("1");
                if(ticketNumber > 1){
                    order.setTicketType("1");
                }
                expDate = new Date(DateUtils.parseDate(appoTime,DateUtils.PATTERN_yyyy_MM_dd_HH_mm_ss).getTime()+oneHour);
            }else{
                //高峰出现，购票人数只能1人
                if(ticketNumber != 1){
                    return ResultVo.error(-1,ResultConstant.SYS_PEAK_PURCHASE_LIMITONE_ERROR_VALUE);
                }
                routeType = "1";
                order.setOrderType("0");
                order.setLineId(lineId);
                //设置过期时间
                expDate = DateUtils.getDayEnd();
            }
            order.setExpDate(expDate);
        }else {
            if(!StringUtil.isEmpty(appoTime)){
                order.setOrderType("1");
                if(ticketNumber > 1){
                    order.setTicketType("1");
                }
                expDate = new Date(DateUtils.parseDate(appoTime,DateUtils.PATTERN_yyyy_MM_dd_HH_mm_ss).getTime()+oneHour);
            }else{
                order.setOrderType("0");
                if(ticketNumber > 1){
                    ticketType = "1";
                }
                expDate = new Date(nowTime.getTime()+oneHour);
            }
            order.setExpDate(expDate);
        }
        //检测是否还有未出行的票
        Map<String,Object> condition = Maps.newHashMap();
        condition.put("userId",userId);
        condition.put("routeType",routeType);
        ResultVo checkOrderVo = ticketManageClient.checkExitsNotTravelPeak(condition);
        if(checkOrderVo.getResult() != 0 ){
            return checkOrderVo;
        }
        //用车状态( 0 单票  1多票 )
        order.setTicketType(ticketType);
        //行程类型( 0 平峰 1高峰 )
        order.setRouteType(routeType);
        //订单状态( 0 创建订单 1待使用 2进行中 3已完成 4已失效 )
        //order.setOrderStatus("0");
        order.setOrderStatus("1");
        //待支付
        //order.setIsPay("0");
        order.setCreateDate(nowTime);
        order.setIsRefund("0");
        order.setStartStationId(startStationId);
        order.setIsCancel("0");
        order.setEndStationId(endStationId);
        //跳过支付过程
        order.setIsPay("1");
        Date updateDate = new Date();
        //设置支付时间
        order.setPayDate(updateDate);
        order.setUpdateDate(updateDate);

        //计算票价
        BigDecimal num = new BigDecimal(ticketNumber);

        //String compPrice = redisUtil.get(RedisConstant.SYS_COMPANY_PRICE);
        String compPrice = "0";
        order.setOrderAmount(new BigDecimal(compPrice).multiply(num));

        //计算站点距离
        String stations = redisUtil.get(RedisConstant.SYS_STATIONS_CACHE_KEY);
        if(!StringUtil.isEmpty(stations)){
            List<Map<Object,Object>> stationLists = JsonUtil.parseJSON2List(stations);
            for (int i = stationLists.size()-1;i>=0;i--) {
                Map<Object, Object> station= stationLists.get(i);
                String id = StringUtil.trim(station.get("id"));
                if(id.equals(startStationId) || id.equals(endStationId)){
                    continue;
                }
                stationLists.remove(station);
            }
            double latitude = StringUtil.getDouble(StringUtil.trim(stationLists.get(0).get("latitude")));
            double longitude = StringUtil.getDouble(StringUtil.trim(stationLists.get(0).get("longitude")));
            double latitude1 = StringUtil.getDouble(StringUtil.trim(stationLists.get(1).get("latitude")));
            double longitude1 = StringUtil.getDouble(StringUtil.trim(stationLists.get(1).get("longitude")));
            double v = MapUtil.GetDistance(longitude, latitude, longitude1, latitude1);
            order.setSiteDis(StringUtil.trim(v));
        }
        ResultVo resultVo = orderClient.addOrder(order);
        if(resultVo.getResult() == 0){
            String orderId = (String) resultVo.getDataSet();
            Map<String,Object> resultMap = Maps.newLinkedHashMapWithExpectedSize(3);
            resultMap.put("orderId",orderId);
            resultMap.put("orderNo",order.getOrderNo());
            resultMap.put("orderAmount",order.getOrderAmount());
            redisUtil.set(RedisConstant.SYS_CREATE_ORDER_KEY_EXPIRE_PREFIX+"&"+orderId, order.getOrderNo(), (long)180);
            resultVo.setDataSet(resultMap);
        }
        //添加订单流水
        condition.put("orderNo",order.getOrderNo());
        orderClient.savePaySerial(condition);

        return resultVo;

    }
    /**
     * 添加购票入口(外部人员 原逻辑）
     * @param data
     * @return
     */
    @PostMapping("/buyTicketByOther")
    @ApiOperation(value = "购票", httpMethod = "POST")
    @ApiImplicitParams({
                @ApiImplicitParam(name = "startStationId", value = "起始站" , required = true,dataType = "String"),
                @ApiImplicitParam(name = "endStationId", value = "终点站" , required = true,dataType = "String"),
                @ApiImplicitParam(name = "appoTime", value = "预约时间" , required = true,dataType = "Date"),
                @ApiImplicitParam(name = "token", value = "token" , required = true,dataType = "String"),
                @ApiImplicitParam(name = "lineId", value = "高峰线路id" , required = false,dataType = "String"),
                @ApiImplicitParam(name = "ticketNumber", value = "预约人数" , required = true,dataType = "int")
    })
    @Transactional
    public ResultVo buyTicketByOther(@ApiIgnore @RequestBody Map<String,Object> data) throws Exception {
        //保存高峰线路ID
        String lineId = StringUtil.trim(data.get("lineId"));
        //        //起始站
        String startStationId = StringUtil.trim(data.get("startStationId"));
        //终点站
        String endStationId = StringUtil.trim(data.get("endStationId"));
        String viaStationKey = redisUtil.get(RedisConstant.SYS_PEAK_VIA_STATIONS_CACHE_KEY);
        List<Map<Object, Object>> maps = JsonUtil.parseJSON2List(viaStationKey);
        List<String> lineIds = Lists.newArrayList();
        for (Map<Object, Object> map : maps) {
            String station_id = StringUtil.trim(map.get("station_id"));
            if(startStationId.equals(station_id)){
                String line_id = StringUtil.trim(map.get("line_id"));
                Double index = StringUtil.getDouble(StringUtil.trim(map.get("index")));
                lineIds.add(line_id);
            }
        }
        for (Map<Object, Object> map : maps) {
            String station_id = StringUtil.trim(map.get("station_id"));
            if(endStationId.equals(station_id)){
                String line_id = StringUtil.trim(map.get("line_id"));
                if(lineIds.contains(line_id)){
                    lineId = line_id;
                    break;
                }
            }
        }
        //判断起始站点是否为空
        if(StringUtil.isEmpty(startStationId)|| StringUtil.isEmpty(endStationId)){
            return ResultVo.error(ResultVo.Status.REQUIRED_PARAMETER_ERROR);
        }
        if(startStationId.equals(endStationId)){
            return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE,"起始站和终点站不能相同");
        }
        //预约时间
        String appoTime = StringUtil.trim(data.get("appoTime"));
        //获取token
        String token = StringUtil.trim(data.get("token"));
        //解析token数据
        String userId = RequestUtil.analysisToken(token).getUserId();

        //订单信息表
        TOrderDto order =  new TOrderDto();
        order.setCreateId(userId);
        order.setUserId(userId);
        if(!StringUtil.isEmpty(appoTime)){
            //检测预约
            ResultVo resultVo = ticketManageClient.checkAppointment(data);
            if(resultVo.getResult() != 0 ){
                return resultVo;
            }
            order.setTripTime(DateUtils.parseDate(appoTime, Constants.DATE_FULL_TIME_PATTERN));
        }
        //预约人数
        int ticketNumber = StringUtil.getAsInt(StringUtil.trim(data.get("ticketNumber")),1);

        if(ticketNumber == 0 || ticketNumber>5){
            return ResultVo.error(ResultVo.Status.REQUIRED_PARAMETER_ERROR);
        }
        order.setOrderNo(CreateUtils.createOrderCode("ON_"));
        order.setTradeNo(StringUtil.trim(new SnowflakeIdWorker(2,1).nextId()));
        order.setPassengersNum(ticketNumber);
        String ticketType ="0";
        String routeType ="0";

        //判断类型 0：平峰即时  1平峰预约 2:高峰
        Date nowTime = new Date();
//        nowTime = DateUtils.parseDate("2018-12-26 07:45:00",DateUtils.PATTERN_yyyy_MM_dd_HH_mm_ss);
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


        Date expDate = null;
        //预约出行，过期时间则加1小时
        int oneHour= 60 * 60 * 1000;
        if (currentTime.after(stratTime) && currentTime.before(endTime)){
            //高峰时段预约平峰出行
            if(!StringUtil.isEmpty(appoTime)){
                order.setOrderType("1");
                if(ticketNumber > 1){
                    order.setTicketType("1");
                }
                expDate = new Date(DateUtils.parseDate(appoTime,DateUtils.PATTERN_yyyy_MM_dd_HH_mm_ss).getTime()+oneHour);
            }else{
                //高峰出现，购票人数只能1人
                if(ticketNumber != 1){
                    return ResultVo.error(-1,ResultConstant.SYS_PEAK_PURCHASE_LIMITONE_ERROR_VALUE);
                }
                routeType = "1";
                order.setOrderType("0");
                order.setLineId(lineId);
                //设置过期时间
                expDate = DateUtils.getDayEnd();
            }
            order.setExpDate(expDate);
        }else {
            if(!StringUtil.isEmpty(appoTime)){
                order.setOrderType("1");
                if(ticketNumber > 1){
                    order.setTicketType("1");
                }
                expDate = new Date(DateUtils.parseDate(appoTime,DateUtils.PATTERN_yyyy_MM_dd_HH_mm_ss).getTime()+oneHour);
            }else{
                order.setOrderType("0");
                if(ticketNumber > 1){
                    ticketType = "1";
                }
                expDate = new Date(nowTime.getTime()+oneHour);
            }
            order.setExpDate(expDate);
        }
        //检测是否还有未出行的票
        Map<String,Object> condition = Maps.newHashMap();
        condition.put("userId",userId);
        condition.put("routeType",routeType);
        ResultVo checkOrderVo = ticketManageClient.checkExitsNotTravelPeak(condition);
        if(checkOrderVo.getResult() != 0 ){
            return checkOrderVo;
        }
        //用车状态( 0 单票  1多票 )
        order.setTicketType(ticketType);
        //行程类型( 0 平峰 1高峰 )
        order.setRouteType(routeType);
        //订单状态( 0 创建订单 1待使用 2进行中 3已完成 4已失效 )
        order.setOrderStatus("0");
        //待支付
        order.setIsPay("0");
        order.setCreateDate(nowTime);
        order.setIsRefund("0");
        order.setStartStationId(startStationId);
        order.setIsCancel("0");
        order.setEndStationId(endStationId);

        //计算票价
        BigDecimal num = new BigDecimal(ticketNumber);

        String otherPrice = redisUtil.get(RedisConstant.SYS_OTHER_PRICE);
        order.setOrderAmount(new BigDecimal(otherPrice).multiply(num));

        //计算站点距离
        String stations = redisUtil.get(RedisConstant.SYS_STATIONS_CACHE_KEY);
        if(!StringUtil.isEmpty(stations)){
            List<Map<Object,Object>> stationLists = JsonUtil.parseJSON2List(stations);
            for (int i = stationLists.size()-1;i>=0;i--) {
                Map<Object, Object> station= stationLists.get(i);
                String id = StringUtil.trim(station.get("id"));
                if(id.equals(startStationId) || id.equals(endStationId)){
                    continue;
                }
                stationLists.remove(station);
            }
            double latitude = StringUtil.getDouble(StringUtil.trim(stationLists.get(0).get("latitude")));
            double longitude = StringUtil.getDouble(StringUtil.trim(stationLists.get(0).get("longitude")));
            double latitude1 = StringUtil.getDouble(StringUtil.trim(stationLists.get(1).get("latitude")));
            double longitude1 = StringUtil.getDouble(StringUtil.trim(stationLists.get(1).get("longitude")));
            double v = MapUtil.GetDistance(longitude, latitude, longitude1, latitude1);
            order.setSiteDis(StringUtil.trim(v));
        }
        ResultVo resultVo = orderClient.addOrder(order);
        if(resultVo.getResult() == 0){
            String orderId = (String) resultVo.getDataSet();
            Map<String,Object> resultMap = Maps.newLinkedHashMapWithExpectedSize(3);
            resultMap.put("orderId",orderId);
            resultMap.put("orderNo",order.getOrderNo());
            resultMap.put("orderAmount",order.getOrderAmount());
            redisUtil.set(RedisConstant.SYS_CREATE_ORDER_KEY_EXPIRE_PREFIX+"&"+orderId, order.getOrderNo(), (long)180);
            resultVo.setDataSet(resultMap);
        }
        return resultVo;
    }


}
