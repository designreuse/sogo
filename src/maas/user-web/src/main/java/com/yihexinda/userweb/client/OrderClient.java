package com.yihexinda.userweb.client;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yihexinda.core.param.QRCodeParam;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TOrderDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

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
     * 根据订单id获取详情
     * @param id 订单id
     * @return  订单详情
     */
    @RequestMapping(method = RequestMethod.POST, value = "/getOrder/{id}")
    ResultVo getOrder(@PathVariable("id") String id);

    /**
     * 显示评价
     * @param data 订单id
     * @return
     */
    @PostMapping(value = "/showEvaluate")
    ResultVo showEvaluate(@RequestBody Map<String, Object> data);

    /**
     * 设置订单过期
     * @param paramData
     * @return
     */
    @PostMapping("/setExpOrder")
    ResultVo setExpOrder(@RequestBody Map<String, Object> paramData);

    /**
     * pengFeng
     * 下单接口
     * @param tOrderDto 下单信息
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/addOrder")
    ResultVo addOrder(@RequestBody TOrderDto tOrderDto);

    /**
     *  修改订单信息
     * @param tOrderDto 订单信息
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/updateOrder")
    ResultVo updateOrder(@RequestBody TOrderDto tOrderDto);

    /**
     *  查询用户行程
     * @param condition 用户id
     * @return 用户行程信息
     */
    @RequestMapping(method = RequestMethod.POST, value = "getOrderUser")
    ResultVo getOrderUser(@RequestBody Map<String, Object> condition);


    /**
     *  查询未分配的订单信息
     * @param condition
     * @return ResultVo
     */
    @PostMapping(value = "/getUnAllotCarOrderList")
    ResultVo getUnAllotCarOrderList(@RequestBody Map<String,Object> condition);

    /**
     * 生成预支付信息
     * @param map
     * @return
     */
    @PostMapping("loadPayPre")
    ResultVo loadAppPayPre(@RequestBody Map<String, Object> map);

    /**
     *查询微信支付结果
     * @param paramData
     * @return
     */
    @PostMapping("queryWechatPayResult")
    ResultVo queryWechatPayResult(Map<String, Object> paramData);

    /**
     * 根据订单id生成二维码数据
     */
    @PostMapping("/getOrderQRCode")
    ResultVo getOrderQRCode(@RequestBody Map<String, Object> paramData);



    /**
     * 自动过期
     * @param condition
     * @return
     */
    @PostMapping(value = "/setOrderExpire")
    ResultVo setOrderExpire(@RequestBody Map<String,Object> condition);


    /**
     * 查询用户最近订单
     * @param data userId
     * @return  ResultVo
     */
    @PostMapping("getLatelyOrder")
    ResultVo getLatelyOrder(@RequestBody Map<String, Object> data);


    /**
     * 取消订单
     * @param paramData
     * @return
     */
    @PostMapping("/cancelOrder")
    ResultVo cancelOrder(@RequestBody Map<String, Object> paramData);

    /**
     * 加载订单退款过期时间
     * @param paramData
     * @return
     */
    @PostMapping("/loadRefundExpirationTime")
    ResultVo loadRefundExpirationTime(@RequestBody Map<String, Object> paramData);

    /**
     * 检查二维码是否被扫
     * @param param
     * @return
     */
    @PostMapping(value = "/checkQrCallBack")
    ResultVo checkQrCallBack(@RequestBody QRCodeParam param);

    /**
     * 申请微信退款
     * @param paramData
     * @return
     */
    @PostMapping(value = "/applyRefund")
    ResultVo applyRefund(@RequestBody Map<String, Object> paramData);

    /**
     * 系统自动取消实时出行订单
     * @return
     */
    @PostMapping(value = "/sysCancalOrder")
    ResultVo sysCancalOrder();

    /**
     * 查询高峰最近车辆
     * @param condition
     * @return
     */
    @PostMapping(value = "/queryPeakOrderInfo")
    ResultVo queryPeakOrderInfo(@RequestBody Map<String, Object> condition);

    /**
     * 首页获取用户最近使用的订单
     * @param condition
     * @return
     */
    @PostMapping(value = "/queryRecentOrderOrder")
    ResultVo queryRecentOrderOrder(@RequestBody Map<String, Object> condition);

    /**
     * wenbn
     * 显示车辆线路信息
     * @param condition
     * @return
     */
    @PostMapping(value = "/queryPeakLineInfo")
    ResultVo queryPeakLineInfo(@RequestBody Map<String, Object> condition);
    /**
     * wenbn
     * 保存流水
     * @param condition
     * @return
     */
    @PostMapping(value = "/savePaySerial")
    ResultVo savePaySerial(@RequestBody Map<String, Object> condition);

    @PostMapping(value = "/setRouteOrderExpire")
    ResultVo setRouteOrderExpire(@RequestBody Map<String, Object> condition);

}
