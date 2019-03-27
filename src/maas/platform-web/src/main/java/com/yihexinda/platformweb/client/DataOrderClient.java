package com.yihexinda.platformweb.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yihexinda.core.dto.Json;
import com.yihexinda.core.dto.Page;
import com.yihexinda.data.dto.CencusDto;
import com.yihexinda.data.dto.OrderDetailsDto;
import com.yihexinda.data.dto.OrderDto;
import com.yihexinda.data.dto.OrderPageQueryDto;
import com.yihexinda.data.dto.OrderQueryDto;

/**
 * @author Jack
 * @date 2018/10/19.
 */
@FeignClient(value = "data-service")
@RequestMapping("/order")
public interface DataOrderClient {
    @RequestMapping(method = RequestMethod.POST, value = "/findOrderPage")
    @ResponseBody
    Page<OrderDto> findOrderPage(OrderPageQueryDto findOrderPageDto);

    @RequestMapping(method = RequestMethod.POST, value = "/robbingOrderPage")
    @ResponseBody
    Page<OrderDto> robbingOrderPage(OrderPageQueryDto orderPageQueryDto);

    @RequestMapping(method = RequestMethod.POST, value = "/dispatchOrderPage")
    @ResponseBody
    Page<OrderDto> dispatchOrderPage(OrderPageQueryDto orderPageQueryDto);

    /**
     * 手动派单接口
     * @param id
     * @param userName
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/distributeOrder")
    @ResponseBody
    Json distributeOrder(@RequestParam("id") String id,@RequestParam("userName") String userName);

    /**
     * 派单确认接口
     * @param orderId 订单id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/comfirmDispatchOrder")
    @ResponseBody
    Json comfirmDispatchOrder(@RequestParam("orderId") String orderId,@RequestParam("userId")Long userId);

    /**
     * 异常订单详情展示接口
     * @param ddbh
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/findOrderDetails")
    @ResponseBody
    Json<OrderDetailsDto> findOrderDetails(@RequestParam("ddbh") String ddbh);

    /**
     * 订单抽回接口
     * @param orderId
     */
    @RequestMapping(method = RequestMethod.GET, value = "/withdrawOrder")
    @ResponseBody
    Json  withdrawOrder(@RequestParam("orderId")String  orderId,@RequestParam("userName")String  userName);

    /**
     * 订单类型数据统计
     * @param orderQueryDto
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/countOrderTypeNum")
    @ResponseBody
    Json<CencusDto> countOrderTypeNum(OrderQueryDto  orderQueryDto);

}
