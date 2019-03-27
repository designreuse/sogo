package com.yihexinda.dataservice.api;


import com.yihexinda.core.constants.ResultConstant;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TOrderDto;
import com.yihexinda.data.dto.TOrderRefundsDto;
import com.yihexinda.dataservice.service.TOrderRefundsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;


/**
 * @author pengfeng
 * @version 1.0
 * @date 2018/12/1 0028
 */
@RestController
@RequestMapping("/orderRefund/client")
@Slf4j
public class OrderRefundApiResource {


    @Resource
    private TOrderRefundsService tOrderRefundsService;

    /**
     * pengFeng
     * 获取退款记录列表
     *
     * @return 退款记录列表
     */
    @GetMapping("/getOrderRefundList")
    public ResultVo getOrderRefundList() {
        return ResultVo.success().setDataSet(tOrderRefundsService.list());
    }

    /**
     * pengFeng
     *
     * @param id 退款id、
     * @return 退款详情
     */
    @GetMapping("/OrderRefund/{id}")
    public ResultVo getOrderRefund(@PathVariable String id) {
        return ResultVo.success().setDataSet(tOrderRefundsService.getById(id));
    }

    /**
     * pengFeng
     * 新增退款记录表
     *
     * @param tOrderRefundsDto 退款信息
     * @return
     */
    @PostMapping("/addOrderRefund")
    public ResultVo addOrderRefund(@RequestBody TOrderRefundsDto tOrderRefundsDto) {
        if (null == tOrderRefundsDto.getOrderId() || "".equals(tOrderRefundsDto.getOrderId())) {
            return ResultVo.error(ResultConstant.SYS_REQUIRED_PARAMETER_ERROR, ResultConstant.SYS_REQUIRED_PARAMETER_ERROR_VALUE);
        }
        if (tOrderRefundsService.save(tOrderRefundsDto)) {
            return ResultVo.success();
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
    }

    /**
     * pengFeng
     * 更改退款状态
     *
     * @param TOrderRefundsDto 订单信息
     * @return
     */
    @PutMapping("/updateOrderRefund")
    public ResultVo updateOrderRefund(@RequestBody TOrderRefundsDto TOrderRefundsDto) {
        if (null == TOrderRefundsDto.getRefundNo() || "".equals(TOrderRefundsDto.getRefundNo()) || null == TOrderRefundsDto.getRefundStatus() || "".equals(TOrderRefundsDto.getRefundStatus())) {
            return ResultVo.error(ResultConstant.SYS_REQUIRED_PARAMETER_ERROR, ResultConstant.SYS_REQUIRED_PARAMETER_ERROR_VALUE);
        }
        TOrderRefundsDto.setUpdateDate(new Date());
        if (tOrderRefundsService.updateById(TOrderRefundsDto)) {
            return ResultVo.success();
        }
        return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
    }


}
