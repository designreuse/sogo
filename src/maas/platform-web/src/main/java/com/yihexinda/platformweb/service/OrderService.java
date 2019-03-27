package com.yihexinda.platformweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yihexinda.core.dto.Json;
import com.yihexinda.data.dto.OrderDto;
import com.yihexinda.platformweb.client.DataOrderClient;

/**
 * @author Jack
 * @date 2018/10/26.
 */
@Service
public class OrderService {
    @Autowired
    private DataOrderClient orderClient;


//    public void withdrawOrder(String orderId){
//        OrderDto orderDto = new OrderDto();
//        orderDto.setId(orderId);
//        orderClient.withdrawOrder(orderDto);
//
//    }

    /**
     * 手动派单
     *
     * @param orderDto
     * @return
     */
//    public Json distributeOrder(OrderDto orderDto) {
//        Json json = orderClient.distributeOrder(orderDto.getId(), orderDto.getOutTicketMember());
//        return json;
//    }

    /**
     * 派单确认
     *
     * @param orderId
     * @return
     */
    public Json comfirmDispatchOrder(String orderId, Long userId) {
        Json json = orderClient.comfirmDispatchOrder(orderId, userId);
        return json;
    }

    /**
     * 订单抽回
     *
     * @param orderId
     * @return
     */
    public Json withdrawOrder(String orderId, String userName) {
        Json json = orderClient.withdrawOrder(orderId, userName);
        return json;
    }

}
