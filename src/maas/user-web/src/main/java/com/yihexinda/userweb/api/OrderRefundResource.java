package com.yihexinda.userweb.api;


import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TOrderDto;
import com.yihexinda.data.dto.TOrderRefundsDto;
import com.yihexinda.userweb.client.OrderClient;
import com.yihexinda.userweb.client.OrderRefundClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author pengfeng
 * @date 2018/12/3
 */
@Api(description = "订单接口")
@RestController()
@RequestMapping("api/user")
@Slf4j
public class OrderRefundResource {

    @Autowired
    private OrderRefundClient orderRefundClient;


    /**
     * pengfeng
     * 新增退款
     * @param tOrderRefundsDto 退款信息
     * @return
     */
    @ApiOperation(value = "新增退款信息", httpMethod = "POST")
    @RequestMapping(value = "/addOrderRefund")
    public ResultVo addOrderRefund(@RequestBody TOrderRefundsDto tOrderRefundsDto) {
        return orderRefundClient.addOrderRefund(tOrderRefundsDto);
    }

    /**
     * pengfeng
     * 修改退款状态
     * @param tOrderRefundsDto 退款信息
     * @return
     */
    @ApiOperation(value = "修改退款状态", httpMethod = "PUT")
    @RequestMapping(value = "/updateOrderRefund")
    public ResultVo updateOrderRefund(@RequestBody TOrderRefundsDto tOrderRefundsDto) {
        return orderRefundClient.updateOrderRefund(tOrderRefundsDto);
    }

}
