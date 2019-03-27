package com.yihexinda.userweb.client;

import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TOrderDto;
import com.yihexinda.data.dto.TOrderRefundsDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author pengfeng
 * @date 2018/11/29
 */
@FeignClient(value = "data-service")
@RequestMapping("/orderRefund/client")
public interface OrderRefundClient {

    /**
     * pengFeng
     * 新增退款信息
     * @param tOrderRefundsDto 退款信息
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/addOrderRefund")
    ResultVo addOrderRefund(@RequestBody TOrderRefundsDto tOrderRefundsDto);

    /**
     *  更改退款状态
     * @param tOrderRefundsDto 退款信息
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/updateOrderRefund")
    ResultVo updateOrderRefund(@RequestBody TOrderRefundsDto tOrderRefundsDto);


}
