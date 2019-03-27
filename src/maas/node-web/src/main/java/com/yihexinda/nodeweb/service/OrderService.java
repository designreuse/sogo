package com.yihexinda.nodeweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.yihexinda.core.Execption.BussException;
import com.yihexinda.core.dto.Json;
import com.yihexinda.data.dto.OrderDto;
import com.yihexinda.data.dto.OrderQueryDto;
import com.yihexinda.nodeweb.client.DataOrderClient;

/**
 * @author Jack
 * @date 2018/10/26.
 */
@Service
public class OrderService {
    @Autowired
    private DataOrderClient dataOrderClient;

    public OrderDto findOrderById(String orderId) {
        OrderQueryDto queryDto = new OrderQueryDto();
        queryDto.setId(orderId);
        return dataOrderClient.findOrderById(queryDto);
    }

    public void confirmGrabbedOrder(String orderId, Integer userId) {
        Json json = dataOrderClient.affirmGrabSingleOrder(orderId, userId);
        if (!json.isSuccess()) {
            throw new BussException(json.getMsg());
        }
    }

    public boolean checkOrderCanBeGrabbed(String orderId, Integer userId) throws BussException {
        OrderQueryDto queryDto = new OrderQueryDto();
        queryDto.setId(orderId);
        queryDto.setUserId(userId.intValue());
        Json<Boolean> json = dataOrderClient.checkOrderCanBeGrabbed(queryDto);
        if (!json.isSuccess() || !json.getObj()) {
            throw new BussException(json.getMsg());
        }
        return true;
    }

    public Json sendBackOrder(@RequestBody OrderDto orderDto) {
        Json json = dataOrderClient.sendBackOrder(orderDto);
        return json;
    }

    /**
     * 派单确认
     *
     * @param orderId
     * @return
     */
    public Json comfirmDispatchOrder(String orderId, Long userId) {
        Json json = dataOrderClient.comfirmDispatchOrder(orderId, userId);
        return json;
    }
}
