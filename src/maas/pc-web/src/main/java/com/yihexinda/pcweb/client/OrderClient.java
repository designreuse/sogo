package com.yihexinda.pcweb.client;

import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TSysUserDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author pengfeng
 * @date 2018/11/29
 */
@FeignClient(value = "data-service")
@RequestMapping("/order/client")
public interface OrderClient {

    /**
     * pengFeng
     * 查询订单列表
     * @param  condition 参数map
     * @return 订单列表
     */
    @RequestMapping(method = RequestMethod.POST, value = "/getOrderList")
    ResultVo getOrderList(@RequestBody Map<String, Object> condition);

    /**
     * 首页查询订单数
     * @return 首页查询订单数
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getPcIndex")
    ResultVo getPcIndex();

    /**
     * pengFeng
     * 查询订单列表（用于导出Excel）
     * @return 订单列表
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getOrderExcel")
    List<Map> getOrderExcel();


    /**
     * pengFeng
     * 查询用户订单列表
     * @param  condition 参数map
     * @return 用户订单列表
     */
    @RequestMapping(method = RequestMethod.POST, value = "/getOrderUser")
    ResultVo getOrderUser(@RequestBody Map<String, Object> condition);


    /**
     * pengFeng
     * 查询用户订单列表
     * @param  condition 参数map
     * @return 用户订单列表
     */
    @RequestMapping(method = RequestMethod.POST, value = "/addOrder")
    ResultVo addOrder(@RequestBody Map<String, Object> condition);




}
