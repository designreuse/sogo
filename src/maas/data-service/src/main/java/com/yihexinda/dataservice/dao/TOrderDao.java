package com.yihexinda.dataservice.dao;

import com.yihexinda.data.dto.TOrderDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单信息表 Mapper 接口
 * </p>
 *
 * @author pengfeng
 * @since 2018-11-28
 */
public interface TOrderDao extends BaseMapper<TOrderDto> {

    /**
     * 查询历史订单列表
     * @return 订单列表
     */
    List<Map> getOrderList(Map map);

    /**
     *  查询订单详情
     * @param id 订单id
     * @return 订单详情
     */
    Map getOrder(String id);

    /**
     * 查询用户订单列表
     * @param map 用户id 必填(查询参数)
     * @return 订单列表
     */
    List<Map> getOrderUser(Map<String,Object> condition);


    /**
     * 查询用户距离当前时间最近一条订单（用户端生成二维码）
     * @param userId 用户id
     * @return 订单列表
     */
    TOrderDto getLatelyOrder(String userId);


    /**
     * 批量修改订单信息
     * @param condition
     */
    int batchUpdateByCondition(Map<String, Object> condition);
}
