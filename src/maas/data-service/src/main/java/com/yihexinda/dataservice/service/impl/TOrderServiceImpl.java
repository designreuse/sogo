package com.yihexinda.dataservice.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yihexinda.core.constants.Constants;
import com.yihexinda.core.utils.DateUtils;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.data.dto.TOrderDto;
import com.yihexinda.dataservice.dao.TOrderDao;
import com.yihexinda.dataservice.service.TOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单信息表 服务实现类
 * </p>
 *
 * @author pengfeng
 * @since 2018-11-28
 */
@Service
public class TOrderServiceImpl extends ServiceImpl<TOrderDao, TOrderDto> implements TOrderService {

    @Resource
    private  TOrderDao tOrderDao;

    @Override
    public List<Map> getOrderList(Map map) {
        return tOrderDao.getOrderList(map);
    }

    @Override
    public Map getOrder(String id) {
        return tOrderDao.getOrder(id);
    }

    @Override
    public List<Map> getOrderUser(String userId) {
        Map<String,Object> condition = Maps.newHashMap();
        condition.put("userId",userId);
        return getOrderUser(condition);
    }

    @Override
    public List<Map> getOrderUser(Map<String,Object> condition){
        return tOrderDao.getOrderUser(condition);
    }

    @Override
    public TOrderDto getLatelyOrder(String userId) {
        return tOrderDao.getLatelyOrder(userId);
    }

    /**
     * 查询车上订单（专为算法提供）
     * @return
     */
    @Override
    public Map<String, Object> queryTakeOrder(List<String> vehicleIds) {
        Map<String,Object> resultMap = Maps.newHashMap();
        //保存站点Ids
        List<String> stationIds = Lists.newArrayList();

        List<Map<String, Object>> takeOrderList = this.listMaps(new QueryWrapper<TOrderDto>()
                        .select( "id","car_id","passengers_num people","trip_time starttime","start_station_id",
                                "end_station_id","user_id","create_date ordertime","order_status state","ride_start_date boardtime")
                    .eq("order_status", '2')
                    .in("car_id", vehicleIds)
//                        .apply("to_char(trip_time,'yyyy-MM-dd')={0}",DateUtils.formatDate(new Date(), Constants.DATE_PATTERN))
//                        .between("create_date",date,DateUtils.addMinutes(date,20));
        );
        for (Map<String, Object> takeOrder : takeOrderList) {
            Date ordertime = new Date(((Date) takeOrder.get("ordertime")).getTime());
            takeOrder.put("boardtime",takeOrder.get("boardtime") == null ? null : new Date(((Date)takeOrder.get("boardtime")).getTime()));//保存扫码上车时间
            takeOrder.put("orderID",takeOrder.get("id"));
            takeOrder.put("ordertime",ordertime);
            takeOrder.put("starttime",takeOrder.get("starttime")==null ? ordertime : new Date(((Date)takeOrder.get("starttime")).getTime()));
            takeOrder.put("userID",takeOrder.get("user_id"));
            takeOrder.put("userID",takeOrder.get("user_id"));
            takeOrder.remove("id");
            takeOrder.remove("user_id");
            stationIds.add(StringUtil.trim(takeOrder.get("start_station_id")));
            stationIds.add(StringUtil.trim(takeOrder.get("end_station_id")));
        }
        //保存站点信息
        resultMap.put("stationIds",stationIds);
        resultMap.put("takeOrderList",takeOrderList);
        return resultMap;

    }

    /**
     * 查询上次计算时间内已完成的订单（专为算法提供 以秒为单位）
     * 暂未控制精准时间
     * @return
     */
    @Override
    public Map<String, Object> queryCompleteOrder(Date nowDate,Date addDate) {
        //返回的结果集
        Map<String,Object> resultMap = Maps.newHashMap();
        //保存站点Ids
        List<String> stationIds = Lists.newArrayList();

        List<Map<String, Object>> completeOrderList = this.listMaps(new QueryWrapper<TOrderDto>()
                        .select(
                                "id","passengers_num","trip_time","start_station_id",
                                "end_station_id","user_id","create_date","order_status","ride_start_date boardtime")
                        .eq("order_status", '3')//订单状态为已完成
//                        .apply("to_char(create_date,'yyyy-MM-dd')={0}", DateUtils.formatDate(new Date(), Constants.DATE_PATTERN))
//                    .between("trip_time",nowDate,addDate) //记录上一次计算的时间
                    .eq("route_type","0")
                    .between("update_date",nowDate,addDate) //记录上一次计算的时间
        );

        if(completeOrderList!=null &&completeOrderList.size()>0){
            for (Map<String, Object> complete : completeOrderList) {
                complete.put("orderID",complete.get("id"));
                complete.put("ordertime",new Date(((Date)complete.get("create_date")).getTime()));
                complete.put("boardtime",complete.get("boardtime") == null ? null : new Date(((Date)complete.get("boardtime")).getTime()));//保存扫码上车时间
                complete.put("people",complete.get("passengers_num"));
//                complete.put("starttime",new Date(((Date)complete.get("trip_time")).getTime()));
                complete.put("starttime", complete.get("trip_time") == null ? new Date(((Date)complete.get("create_date")).getTime()) : new Date(((Date)complete.get("trip_time")).getTime()));
                complete.put("userID",complete.get("user_id"));
                complete.put("state",complete.get("order_status"));
                complete.remove("id");
                complete.remove("create_date");
                complete.remove("passengers_num");
                complete.remove("trip_time");
                complete.remove("user_id");
                complete.remove("order_status");
                stationIds.add(StringUtil.trim(complete.get("start_station_id")));
                stationIds.add(StringUtil.trim(complete.get("end_station_id")));
            }
        }
        //已完成订单
        resultMap.put("completeOrderList",completeOrderList);
        //站点ids
        resultMap.put("stationIds",stationIds);
        return resultMap;
    }

    /**
     * 整合算法数据
     * @param stationList 站点信息
     * @param unTakeOrderList 未上车订单
     * @param completeOrderList 已完成订单
     * @param vehicleList 车辆信息
     * @relturn
     */
    @Override
    public Map<String, Object> structureMaasCommonData(List<Map<String, Object>> stationList, List<Map<String, Object>> unTakeOrderList, List<Map<String, Object>> completeOrderList, List<Map<String, Object>> takeOrderList, List<Map<String,Object>> vehicleList) {
        //返回的结果集
        Map<String,Object> resultMap = Maps.newHashMap();
        Map<String, Object> curLocation = null;
        for (Map<String, Object> station : stationList) {
            String id = StringUtil.trim(station.get("id"));
            for (Map<String, Object> unTake : unTakeOrderList) {
                if(StringUtil.trim(unTake.get("start_station_id")).equals(id)){
                    unTake.remove("start_station_id");
                    curLocation = Maps.newHashMap();
                    curLocation.put("id",station.get("id"));
                    curLocation.put("lati",station.get("latitude"));
                    curLocation.put("lng",station.get("longitude"));
                    unTake.put("orignal",curLocation);
                }
                if(StringUtil.trim(unTake.get("end_station_id")).equals(id)){
                    unTake.remove("end_station_id");
                    curLocation = Maps.newHashMap();
                    curLocation.put("id",station.get("id"));
                    curLocation.put("lati",station.get("latitude"));
                    curLocation.put("lng",station.get("longitude"));
                    unTake.put("dest",curLocation);
                }
            }
            for (Map<String, Object> complete : completeOrderList) {
                if(StringUtil.trim(complete.get("start_station_id")).equals(id)){
                    complete.remove("start_station_id");
                    curLocation = Maps.newHashMap();
                    curLocation.put("id",station.get("id"));
                    curLocation.put("lati",station.get("latitude"));
                    curLocation.put("lng",station.get("longitude"));
                    complete.put("orignal",curLocation);
                }
                if(StringUtil.trim(complete.get("end_station_id")).equals(id)){
                    complete.remove("end_station_id");
                    curLocation = Maps.newHashMap();
                    curLocation.put("id",station.get("id"));
                    curLocation.put("lati",station.get("latitude"));
                    curLocation.put("lng",station.get("longitude"));
                    complete.put("dest",curLocation);
                }
            }
            for (Map<String, Object> takeOrder : takeOrderList) {
                if(StringUtil.trim(takeOrder.get("start_station_id")).equals(id)){
                    takeOrder.remove("start_station_id");
                    curLocation = Maps.newHashMap();
                    curLocation.put("id",station.get("id"));
                    curLocation.put("lati",station.get("latitude"));
                    curLocation.put("lng",station.get("longitude"));
                    takeOrder.put("orignal",curLocation);
                }
                if(StringUtil.trim(takeOrder.get("end_station_id")).equals(id)){
                    takeOrder.remove("end_station_id");
                    curLocation = Maps.newHashMap();
                    curLocation.put("id",station.get("id"));
                    curLocation.put("lati",station.get("latitude"));
                    curLocation.put("lng",station.get("longitude"));
                    takeOrder.put("dest",curLocation);
                }
            }

        }

        List<Map<String,Object>> resultUnTakeOrder = new ArrayList<>();
        if(unTakeOrderList!= null && unTakeOrderList.size()>0){
            List<Map<String,Object>> unTakeOrder0 = Lists.newArrayList();
            List<Map<String,Object>> unTakeOrder1 = Lists.newArrayList();
            List<Map<String,Object>> unTakeOrder2 = Lists.newArrayList();
            List<Map<String,Object>> unTakeOrder3 = Lists.newArrayList();
            Map<String,Object> TakeOrder0 = Maps.newHashMap();
            Map<String,Object> TakeOrder1 = Maps.newHashMap();
            Map<String,Object> TakeOrder2 = Maps.newHashMap();
            Map<String,Object> TakeOrder3 = Maps.newHashMap();

            //计算优先级
            long curTime = new Date().getTime();
            long min5 = 1000*60*5;
            long min15 = 1000*60*15;
            long min20 = 1000*60*20;
            for (Map<String, Object> unTake : unTakeOrderList) {
                Date starttime = (Date) unTake.get("starttime");
                Date ordertime = (Date) unTake.get("ordertime");
                unTake.put("starttime",starttime);
                unTake.put("ordertime",ordertime);
                long tripTime = starttime.getTime();
                long diff = tripTime-curTime;
                if(diff< min5){
                    unTakeOrder0.add(unTake);
                }else if(min5 <= diff && diff<min15){
                    unTakeOrder1.add(unTake);
                }else if(min15 <= diff && diff< min20){
                    long create_date = ordertime.getTime();
                    if(create_date-curTime >= 1000*60*60*2 ){
                        unTakeOrder2.add(unTake);
                    }else{
                        unTakeOrder3.add(unTake);
                    }
                }
            }
            TakeOrder0.put("priority",0);
            TakeOrder0.put("untakeOrderList",unTakeOrder0);
            TakeOrder1.put("priority",1);
            TakeOrder1.put("untakeOrderList",unTakeOrder1);
            TakeOrder2.put("priority",2);
            TakeOrder2.put("untakeOrderList",unTakeOrder2);
            TakeOrder3.put("priority",3);
            TakeOrder3.put("untakeOrderList",unTakeOrder3);
            resultUnTakeOrder.add(TakeOrder0);
            resultUnTakeOrder.add(TakeOrder1);
            resultUnTakeOrder.add(TakeOrder2);
            resultUnTakeOrder.add(TakeOrder3);

        }

        resultMap.put("completeOrder",completeOrderList);
        resultMap.put("unTakeOrder",resultUnTakeOrder);

        List<Map<String,Object>> resultTakeOrders = Lists.newArrayList();
        Map<String,Object> takeOrderMap = null;
        List<Map<String,Object>> takeOrders = null;
        for (Map<String,Object> vehicle : vehicleList) {
            String carId = StringUtil.trim(vehicle.get("vehicleID"));
            int holdPeople = 0;
            takeOrders = Lists.newArrayList();
            takeOrderMap = Maps.newHashMap();
            boolean isFind = false;
            for (Map<String, Object> takeOrder : takeOrderList) {
                if(StringUtil.trim(takeOrder.get("car_id")).equals(carId)){
                    isFind = true;
                    holdPeople += StringUtil.getAsInt(StringUtil.trim(takeOrder.get("people")),0);
                    takeOrders.add(takeOrder);
                }
            }
            if(isFind){
                takeOrderMap.put("vehicleId",carId);
                takeOrderMap.put("takeOrderList",takeOrders);
                resultTakeOrders.add(takeOrderMap);
            }
            vehicle.put("holdPeople",holdPeople);
        }

        resultMap.put("vehicleList",vehicleList);
        resultMap.put("takeOrder",resultTakeOrders);//车上订单

        return resultMap;

    }

    /**
     * 批量修改订单信息
     * @param condition
     */
    @Override
    public int batchUpdateByCondition(Map<String, Object> condition) {
        return this.tOrderDao.batchUpdateByCondition(condition);
    }
}
