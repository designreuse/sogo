package com.yihexinda.dataservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yihexinda.data.dto.TOrderDto;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 订单信息表 服务类
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
public interface TOrderService extends IService<TOrderDto> {

    /**
     * 历史订单列表
     * @param map 查询参数
     * @return 订单列表
     */
    List<Map> getOrderList(Map map);

    /**
     *  订单详情
     * @param id 订单id
     * @return  订单详情
     */
    Map getOrder(String id);

    /**
     * 查询用户订单列表
     * @param userId 用户id
     * @return 订单列表
     */
    List<Map> getOrderUser(String userId);

    List<Map> getOrderUser(Map<String,Object> condition);

    /**
     * 查询用户距离当前时间最近一条订单（用户端生成二维码）
     * @param userId 用户id
     * @return 订单列表
     */
    TOrderDto getLatelyOrder(String userId);

    /**
     * 查询车上订单（专为算法提供）
     * @return
     */
    Map<String, Object> queryTakeOrder(List<String> vehicleIds);

    /**
     * 查询上次计算时间内已完成的订单（专为算法提供 以秒为单位）
     * @return
     */
    Map<String, Object> queryCompleteOrder(Date nowDate,Date addDate);

    /**
     * 整合算法数据
     * @param stationList 站点信息
     * @param unTakeOrderList 未上车订单
     * @param completeOrderList 已完成订单
     * @param vehicleList 车辆信息
     * @relturn
     */
    Map<String,Object> structureMaasCommonData(List<Map<String, Object>> stationList, List<Map<String, Object>> unTakeOrderList, List<Map<String, Object>> completeOrderList, List<Map<String, Object>> takeOrderList, List<Map<String,Object>> vehicleList);

    /**
     * 批量修改订单信息
     * @param condition
     */
    int batchUpdateByCondition(Map<String, Object> condition);
}
