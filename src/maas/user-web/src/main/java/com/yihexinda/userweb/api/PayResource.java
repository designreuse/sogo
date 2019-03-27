package com.yihexinda.userweb.api;

import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.userweb.client.OrderClient;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

/**
 * @author wenbn
 * @version 1.0
 * @date 2018/12/7 0007
 */
@Api(description = "支付接口")
@RestController()
@RequestMapping("/api/pay")
public class PayResource {
    @Autowired
    private OrderClient orderClient;

    /**
     *生成预支付信息
     */
    @PostMapping("/loadPayPre")
    @ApiOperation(value = "生成预支付信息", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderNo", value = "订单号", required = true, paramType = "string"),
            @ApiImplicitParam(name = "openid", value = "openid", required = true, paramType = "string"),
            @ApiImplicitParam(name = "token", value = "token", required = true, paramType = "string")
    })
    public ResultVo loadAppPayPre(@ApiIgnore @RequestBody Map<String,Object> map) throws Exception{
        ResultVo vo = null;
        //获取订单号
        String orderNo = StringUtil.trim(map.get("orderNo"));
        if(StringUtil.isEmpty(StringUtil.trim(orderNo))){
            return ResultVo.error(ResultVo.Status.REQUIRED_PARAMETER_ERROR);
        }
        return orderClient.loadAppPayPre(map);
    }

    /**
     *查询微信支付结果
     */
    @PostMapping("/queryWechatPayResult")
    @ApiOperation(value = "查询微信支付结果", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderNo", value = "订单号", required = true, paramType = "string"),
            @ApiImplicitParam(name = "token", value = "token", required = true, paramType = "string")
    })
    public ResultVo queryWechatPayResult(@ApiIgnore @RequestBody Map<String,Object> paramData) throws Exception {

        String orderNo = StringUtil.trim(paramData.get("orderNo"));
        if(StringUtil.isEmpty(StringUtil.trim(orderNo))){
            return ResultVo.error(ResultVo.Status.REQUIRED_PARAMETER_ERROR);
        }
        return orderClient.queryWechatPayResult(paramData);
    }


    /**
     * 申请退款
     */
    @PostMapping("/applyRefund")
    @ApiOperation(value = "申请退款", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderNo", value = "订单号", required = true, paramType = "string"),
            @ApiImplicitParam(name = "token", value = "token", required = true, paramType = "string")
    })
    public ResultVo applyRefund(@ApiIgnore @RequestBody Map<String,Object> paramData) throws Exception {
        String orderNo = StringUtil.trim(paramData.get("orderNo"));
        if(StringUtil.isEmpty(StringUtil.trim(orderNo))){
            return ResultVo.error(ResultVo.Status.REQUIRED_PARAMETER_ERROR);
        }
        paramData.put("refund_desc","买家申请退款");
        return orderClient.applyRefund(paramData);
    }

}
