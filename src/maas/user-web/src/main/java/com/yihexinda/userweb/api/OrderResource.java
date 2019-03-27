package com.yihexinda.userweb.api;


import com.yihexinda.core.utils.RequestUtil;
import com.yihexinda.core.utils.StringUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TOrderDto;
import com.yihexinda.userweb.client.OrderClient;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

/**
 * @author pengfeng
 * @date 2018/12/3
 */
@Api(description = "订单接口")
@RestController()
@RequestMapping("/api/user")
@Slf4j
public class OrderResource {

    @Autowired
    private OrderClient orderClient;

    /**
     * pengfeng
     * 查询订单详情
     *
     * @param id 订单id
     * @return 订单详情
     */
    @ApiOperation(value = "查询订单详情", httpMethod = "POST")
    @RequestMapping(value = "/getOrder/{id}")
    public ResultVo getOrder(@PathVariable String id) {
        return orderClient.getOrder(id);
    }


    /**
     * 显示评价
     * @param data 订单id
     * @return
     */
    @ApiOperation(value = "显示评价", httpMethod = "POST")
    @PostMapping(value = "/showEvaluate")
    public ResultVo showEvaluate(@RequestBody Map<String, Object> data) {
        String orderId = StringUtil.trim(data.get("orderId"));
        if(StringUtil.isEmpty(orderId)){
            return ResultVo.error(ResultVo.Status.REQUIRED_PARAMETER_ERROR);
        }
        return orderClient.showEvaluate(data);
    }

    /**
     * 判断订单是否过期（可查看行程详情）
     * @param paramData
     * @return
     */
    @ApiOperation(value = "判断订单是否过期", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "订单号", required = true, paramType = "string"),
            @ApiImplicitParam(name = "token", value = "token", required = true, paramType = "string")
    })
    @PostMapping("/checkExpOrder")
    public ResultVo checkExpOrder(@ApiIgnore @RequestBody Map<String, Object> paramData) {
        return orderClient.setExpOrder(paramData);
    }

    /**
     * pengfeng
     * 下单接口
     *
     * @param tOrderDto 订单信息
     * @return
     */
    @ApiOperation(value = "下单接口", httpMethod = "POST")
    @RequestMapping(value = "/addOrder")
    public ResultVo addOrder(@RequestBody TOrderDto tOrderDto) {
        return orderClient.addOrder(tOrderDto);
    }

    /**
     * pengfeng
     * 修改订单
     *
     * @param tOrderDto 订单信息
     * @return
     */
    @ApiOperation(value = "修改订单信息", httpMethod = "PUT")
    @RequestMapping(value = "/updateOrder")
    public ResultVo updateOrder(@RequestBody TOrderDto tOrderDto) {
        return orderClient.updateOrder(tOrderDto);
    }

    /**
     * 查询用户行程
     *
     * @param condition 用户id
     * @return 行程id
     */

    @ApiOperation(value = "查询用户行程", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "当前页码", required = true, paramType = "String"),
            @ApiImplicitParam(name = "pageSize", value = "每页多少条", required = true, paramType = "String"),
            @ApiImplicitParam(name = "token", value = "用户登录标识", required = true, paramType = "String")
    })
    @RequestMapping(value = "/getOrderUser",method = RequestMethod.POST)
    public ResultVo getOrderUser(@RequestBody Map<String, Object> condition) {
        condition.put("userId",RequestUtil.analysisToken(String.valueOf(condition.get("token"))).getUserId());
        return orderClient.getOrderUser(condition);
    }



    /**
     * 取消订单
     * @param paramData
     * @return
     */
    @ApiOperation(value = "取消订单", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderNo", value = "订单号", required = true, paramType = "string"),
            @ApiImplicitParam(name = "token", value = "token", required = true, paramType = "string")
    })
    @PostMapping("/cancelOrder")
    public ResultVo cancelOrder(@ApiIgnore @RequestBody Map<String, Object> paramData) {
        return orderClient.cancelOrder(paramData);
    }

    /**
     * 取消订单前加载订单退款过期时间
     * @param condition
     * @return
     */
    @ApiOperation(value = "取消订单前加载订单退款过期时间", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderNo", value = "订单号", required = true, paramType = "string"),
            @ApiImplicitParam(name = "token", value = "用户登录标识", required = true, paramType = "string"),
            @ApiImplicitParam(name = "status", value = "是否取消订单(0确定,1不取消)", required = true, paramType = "string")
    })
    @PostMapping("/loadRefundExpirationTime")
    public ResultVo loadRefundExpirationTime( @RequestBody Map<String, Object> condition) {
        condition.put("userId",RequestUtil.analysisToken(String.valueOf(condition.get("token"))).getUserId());
        return orderClient.loadRefundExpirationTime(condition);
    }


    /**
     * wenbn
     * 查询高峰最近车辆
     *
     * @param condition
     * @return
     */
    @ApiOperation(value = "查询高峰最近车辆", httpMethod = "POST")
    @PostMapping(value = "/queryPeakOrderInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "订单ID", required = true, paramType = "string"),
            @ApiImplicitParam(name = "token", value = "用户登录标识", required = true, paramType = "string")
    })
    public ResultVo queryPeakOrderInfo(@ApiIgnore @RequestBody Map<String,Object> condition) {
        return orderClient.queryPeakOrderInfo(condition);
    }

    /**
     * wenbn
     * 显示车辆线路信息
     * @param condition
     * @return
     */
    @ApiOperation(value = "显示车辆线路信息", httpMethod = "POST")
    @PostMapping(value = "/queryPeakLineInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "订单ID", required = true, paramType = "string"),
            @ApiImplicitParam(name = "token", value = "用户登录标识", required = true, paramType = "string")
    })
    public ResultVo queryPeakLineInfo(@ApiIgnore @RequestBody Map<String,Object> condition) {
        return orderClient.queryPeakLineInfo(condition);
    }
}
