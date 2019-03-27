package com.yihexinda.nodeweb.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
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

    @RequestMapping(method = RequestMethod.POST, value = "/queryOrderPage")
    @ResponseBody
    Page<OrderDto> queryOrderPage(OrderPageQueryDto orderPageQueryDto);


    @RequestMapping(method = RequestMethod.POST, value = "/findOrderById")
    @ResponseBody
    OrderDto findOrderById(OrderQueryDto queryDto);

    /**
     * 异常订单详情展示接口
     *
     * @param ddbh
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/findOrderDetails")
    @ResponseBody
    Json<OrderDetailsDto> findOrderDetails(@RequestParam("ddbh") String ddbh);

    /**
     * 订单退回
     *
     * @param orderDto
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/sendBackOrder")
    @ResponseBody
    Json sendBackOrder(@RequestBody  OrderDto orderDto);

    /**
     * 抢订单确认接口
     *
     * @param orderId
     * @param userId
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/affirmGrabSingleOrder")
    @ResponseBody
    Json affirmGrabSingleOrder(@RequestParam("orderId") String orderId,@RequestParam("userId")Integer userId);

    /**
     * 检查订单是否可抢
     *
     * @param queryDto
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/checkOrderCanBeGrabbed")
    @ResponseBody
    Json<Boolean> checkOrderCanBeGrabbed(OrderQueryDto queryDto);

    /**
     * 更新订单的暂存状态,用户所有订单中只能出现一条已暂存
     *
     * @param orderId
     * @return
     */
    @RequestMapping(value = "/toggleOrderTempLockStatus",method = RequestMethod.POST)
    @ResponseBody
    Json<OrderDto> toggleOrderTempLockStatus(@RequestParam("orderId") String orderId);

    /**
     * 派单确认接口
     * @param orderId 订单id
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/comfirmDispatchOrder")
    @ResponseBody
    Json comfirmDispatchOrder(@RequestParam("orderId") String orderId,@RequestParam("userId")Long userId);

    /**
     * 订单类型数据统计
     * @param orderQueryDto
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/countOrderTypeNum")
    @ResponseBody
    Json<CencusDto> countOrderTypeNum(OrderQueryDto  orderQueryDto);

    /**
     * 我的订单
     * @param dto
     * @return
     */
    @RequestMapping(method = RequestMethod.POST,value = "/selectOrderDto")
    Page<OrderDto> selectOrderDto(OrderPageQueryDto dto);


}
