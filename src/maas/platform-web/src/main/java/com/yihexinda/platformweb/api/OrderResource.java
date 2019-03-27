package com.yihexinda.platformweb.api;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.yihexinda.auth.domain.User;
import com.yihexinda.core.Execption.BussException;
import com.yihexinda.core.dto.Json;
import com.yihexinda.core.dto.Page;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.data.dto.CencusDto;
import com.yihexinda.data.dto.OperateLogDto;
import com.yihexinda.data.dto.OrderDetailsDto;
import com.yihexinda.data.dto.OrderDto;
import com.yihexinda.data.dto.OrderPageQueryDto;
import com.yihexinda.data.dto.OrderQueryDto;
import com.yihexinda.data.dto.RemarkDto;
import com.yihexinda.data.enums.ManageStatusEnum;
import com.yihexinda.data.enums.OperateTypeEnum;
import com.yihexinda.platformweb.client.DataOrderClient;
import com.yihexinda.platformweb.security.SecurityHelper;
import com.yihexinda.platformweb.service.OperateLogService;
import com.yihexinda.platformweb.service.OrderService;
import com.yihexinda.platformweb.service.RemarkService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by jiangchangyuan on 2018-10-19.
 */
@Api(description = "订单接口")
@RestController
@RequestMapping("/api/order")
@Slf4j
public class OrderResource {
    @Autowired
    private DataOrderClient orderClient;
    @Autowired
    private OperateLogService operateLogService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private RemarkService remarkService;

    @Autowired
    private SecurityHelper securityHelper;

    /**
     * 分页查询订单列表
     *
     * @param pageQueryDto
     * @return
     */
    @ApiOperation(value = "订单查询列表", httpMethod = "POST")
    @PostMapping("/list")
    @ResponseBody
    public Json<Page<OrderDto>> list(@RequestBody OrderPageQueryDto pageQueryDto) {
        Json json = new Json();
        try {
            if (pageQueryDto.getManageStatus() != null) {
                if (ManageStatusEnum.已超时.equals(pageQueryDto.getManageStatus())) {
                    pageQueryDto.setOvertime(new Date());
                    pageQueryDto.setManageStatus(null);
                }
            }
            json.setSuccess(true);
            json.setObj(orderClient.findOrderPage(pageQueryDto));
        } catch (Exception e) {
            json.setSuccess(false);
            json.setMsg(e.getMessage());
            log.error("list {}", e);
        }
        return json;
    }

    /**
     * 查询订单操作日志
     *
     * @param orderId
     * @return
     */
    @ApiOperation(value = "订单日志查询", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单ID", dataType = "String", paramType = "path", required = true)
    })
    @GetMapping("/operateLog/{orderId}")
    @ResponseBody
    public Json<List<OperateLogDto>> operateLog(@PathVariable String orderId) {
        Json json = new Json();
        try {
            json.setSuccess(true);
            json.setObj(operateLogService.findByAble("Order", orderId));
        } catch (Exception e) {
            json.setSuccess(false);
            json.setMsg("系统内部错误");
            log.error("operateLog {}", e);
        }
        return json;
    }


    /**
     * 查询订单备注记录
     *
     * @param orderId 订单id
     * @return
     */
    @ApiOperation(value = "订单备注记录查询", httpMethod = "GET")
    @RequestMapping(value = "/remark/{orderId}")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "orderId", value = "订单id", dataType = "String",
                    paramType = "path", required = true)
    )
    public Json<List<RemarkDto>> findRemark(@PathVariable String orderId) {
        Json json = new Json();
        try {
            json.setSuccess(true);
            json.setObj(remarkService.findRemark("Order", orderId));
        } catch (Exception e) {
            json.setSuccess(false);
            json.setMsg("系统内部错误");
            log.error("operateLog {}", e);
        }
        return json;
    }

    /**
     * 方法:待抢单列表
     *
     * @param orderPageQueryDto
     * @return
     */
    @ApiOperation(value = "待抢单列表", httpMethod = "POST")
    @PostMapping("/robbingOrderPage")
    @ResponseBody
    public Json<Page<OrderDto>> robbingOrderPage(@RequestBody OrderPageQueryDto orderPageQueryDto) {
        Json json = new Json<>();
        Page<OrderDto> page = new Page<>();
        try {
            page = orderClient.robbingOrderPage(orderPageQueryDto);
            json.setSuccess(Boolean.TRUE);
            json.setObj(page);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            json.setSuccess(Boolean.FALSE);
            json.setMsg("订单查询错误！");
        }
        return json;
    }

    /**
     * 方法：待派单接口
     *
     * @param orderPageQueryDto
     * @return
     */
    @ApiOperation(value = "待派单列表", httpMethod = "GET")
    @PostMapping("/dispatchOrderPage")
    @ResponseBody
    public Json<Page<OrderDto>> dispatchOrderPage(@RequestBody OrderPageQueryDto orderPageQueryDto) {
        Json json = new Json<>();
        Page<OrderDto> page = new Page<>();
        try {
            page = orderClient.dispatchOrderPage(orderPageQueryDto);
            json.setSuccess(Boolean.TRUE);
            json.setObj(page);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            json.setSuccess(Boolean.FALSE);
            json.setMsg("订单查询错误！");
        }
        return json;
    }

    @ApiOperation(value = "手动派单操作接口", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "outTicketMember", value = "被选中用户username", dataType = "String", paramType = "JSON", required = true),
            @ApiImplicitParam(name = "id", value = "订单id", dataType = "String", paramType = "JSON", required = true)
    }
    )
    @PostMapping("/distributeOrder")
    public Json distributeOrder(@RequestBody OrderDto order) {
        Json json = orderService.distributeOrder(order);
        if (json.isSuccess()) {
            operateLogService.addOperateLog(securityHelper.getCurrentUser().getId(), "Order", order.getId(), OperateTypeEnum.派单, "派单成功");
        }
        return json;
    }

    @ApiOperation(value = "派单确认操作接口", httpMethod = "GET")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "orderId", value = "订单id", dataType = "String",
                    paramType = "path", required = true)
    )
    @GetMapping("/comfirmDispatchOrder/{orderId}")
    public Json comfirmDispatchOrder(@PathVariable("orderId") String orderId) {
        Json json = orderService.comfirmDispatchOrder(orderId, securityHelper.getCurrentUser().getId());
        if (json.isSuccess()) {
            operateLogService.addOperateLog(securityHelper.getCurrentUser().getId(), "Order", orderId, OperateTypeEnum.派单确认, "派单确认成功");
        }
        return json;
    }

    @ApiOperation(value = "添加订单备注", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ableId", value = "订单id", dataType = "String", paramType = "JSON", required = true),
            @ApiImplicitParam(name = "remarkInfo", value = "备注内容", dataType = "String", paramType = "JSON", required = true),
    })
    @PostMapping("/addRemark")
    public Json addRemark(@RequestBody RemarkDto remarkDto) {
        Json json = new Json();
        try {
            remarkService.addRemark(remarkDto);
            json.setSuccess(true);
            json.setMsg("添加成功");
        } catch (BussException e) {
            log.warn("addRemark{}", e);
            json.setSuccess(false);
            json.setMsg(e.getExceptionInfo());
        } catch (Exception e) {
            log.error("addRemark{}", e);
            json.setSuccess(false);
            json.setMsg("系统内部错误");
        }
        return json;
    }

    /**
     * 查询异常订单详情接口
     *
     * @param externalOrderNo 外部订单编号
     * @return
     */
    @ApiOperation(value = "查询订单详情", httpMethod = "GET")
    @GetMapping("/findOrderDetails/{externalOrderNo}")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "externalOrderNo", value = "外部订单编号", dataType = "String",
                    paramType = "path", required = true)
    )
    public Json<OrderDetailsDto> findOrderDetails(@PathVariable String externalOrderNo) {
        return orderClient.findOrderDetails(externalOrderNo);
    }

    /**
     * 订单手动抽回
     *
     * @param
     * @return
     */
    @ApiOperation(value = "订单手动抽回", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单ID", dataType = "String", paramType = "path", required = true)
    })
    @GetMapping("/withdrawOrder/{orderId}")
    public Json withdrawOrder(@PathVariable String orderId) {
        Json json = new Json();
        if (StringUtil.isEmpty(orderId)) {
            json.setMsg("订单ID不能为空！");
            json.setSuccess(Boolean.FALSE);
        } else {
            User currentUser = securityHelper.getCurrentUser();
            json = orderService.withdrawOrder(orderId, currentUser.getUsername());
        }

        return json;
    }

    @ApiOperation(value = "订单分类统计", httpMethod = "POST")
    @PostMapping("/countOrderTypeNum")
    @ResponseBody
    public Json<CencusDto> countOrderTypeNum(@RequestBody OrderQueryDto orderQueryDto) {
        Json<CencusDto> json = orderClient.countOrderTypeNum(orderQueryDto);
        return json;
    }
}
