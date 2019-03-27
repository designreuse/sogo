package com.yihexinda.nodeweb.api;

import static com.yihexinda.data.constants.Constants.KAFKA_TOPIC_GRABBING_ORDER_KEY;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.yihexinda.auth.constant.AuthoritiesConstants;
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
import com.yihexinda.data.dto.UserDto;
import com.yihexinda.data.enums.ManageStatusEnum;
import com.yihexinda.data.enums.MessageTypeEnum;
import com.yihexinda.data.enums.OperateTypeEnum;
import com.yihexinda.data.enums.OrderTypeEnum;
import com.yihexinda.data.enums.UserOnlineStatusEnum;
import com.yihexinda.nodeweb.client.DataOrderClient;
import com.yihexinda.nodeweb.client.DataUserClient;
import com.yihexinda.nodeweb.security.SecurityHelper;
import com.yihexinda.nodeweb.security.SecurityUtils;
import com.yihexinda.nodeweb.service.MessageService;
import com.yihexinda.nodeweb.service.OperateLogService;
import com.yihexinda.nodeweb.service.OrderService;
import com.yihexinda.nodeweb.service.RemarkService;

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
    private KafkaTemplate<String, OrderDto> kafkaTemplate;

    @Autowired
    private SecurityHelper securityHelper;

    @Autowired
    private OperateLogService operateLogService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private RemarkService remarkService;
    @Autowired
    private DataUserClient userClient;


//    /**
//     * 分页查询订单列表
//     *
//     * @param findOrderPageDto
//     * @return
//     */
//    @ApiOperation(value = "订单查询列表", httpMethod = "POST")
//    @RequestMapping("/list")
//    @ResponseBody
//    public Page<OrderDto> list(@RequestBody OrderPageQueryDto findOrderPageDto) {
//        return orderClient.findOrderPage(findOrderPageDto);
//    }

    /**
     * 检查用户是否有权限抢单
     *
     * @param orderType
     */
    private void checkOrderProcessingAuthority(OrderTypeEnum orderType) {
        if ((orderType.equals(OrderTypeEnum.出票) && !SecurityUtils.hasAuthority(AuthoritiesConstants.NODE_GRAB_ORDER_TYPE_CHUPIAO))
                || (orderType.equals(OrderTypeEnum.退票) && !SecurityUtils.hasAuthority(AuthoritiesConstants.NODE_GRAB_ORDER_TYPE_TUIPIAO))
                || (orderType.equals(OrderTypeEnum.改期) && !SecurityUtils.hasAuthority(AuthoritiesConstants.NODE_GRAB_ORDER_TYPE_GAIQI))) {
            throw new BussException("您没有抢" + orderType.name() + "单的权限");
        }
    }

    /**
     * 方法:添加请求到抢单队列
     *
     * @param orderId
     * @return
     */
    @ApiOperation(value = "抢单", httpMethod = "GET")
    @GetMapping("/grabOrder/{orderId}")
    @ResponseBody
    public Json grabOrder(@PathVariable String orderId) {
        Json json = new Json();
        try {
            if (StringUtil.isEmpty(orderId)) {
                json.setMsg("订单ID不能为空！");
                json.setSuccess(false);
            } else {
                //获取用户信息
                User currentUser = securityHelper.getCurrentUser();
                OrderDto order = orderService.findOrderById(orderId);
                if (order == null) {
                    throw new BussException("该订单不存在");
                }
                checkOrderProcessingAuthority(order.getOrderType());
                orderService.checkOrderCanBeGrabbed(orderId, currentUser.getId().intValue());
                UserDto userDto = userClient.findUserStatus(currentUser.getId());
                if (userDto == null) {
                    throw new BussException("查询不到用户信息");
                }
                if (!userDto.getOnlineStatus().equals(UserOnlineStatusEnum.在线)) {
                    throw new BussException("正" + userDto.getOnlineStatus().name() + "状态中，不可抢单，请手动切换在线状态");
                }
                OrderDto orderDto = new OrderDto();
                orderDto.setId(orderId);
                orderDto.setUserId(currentUser.getId());
                orderDto.setGrabSingleMember(currentUser.getUsername());

                kafkaTemplate.send(KAFKA_TOPIC_GRABBING_ORDER_KEY, orderDto);

                //抢单将用户置为忙碌
//                userDto.setOnlineStatus(UserOnlineStatusEnum.忙碌);
//                userClient.updateUserOnlineStatus(userDto);

                json.setSuccess(true);
                json.setMsg("系统正在处理抢单，请稍后...");
            }
        } catch (BussException e) {
            log.warn("grabSingle{}", e);
            json.setMsg(e.getMessage());
            json.setSuccess(false);
        } catch (Exception e) {
            log.error("grabSingle{}", e);
            json.setMsg("服务器内部错误");
            json.setSuccess(false);
        }

        return json;
    }

    /**
     * 方法：网点平台待抢单列表
     *
     * @param orderPageQueryDto
     * @return
     */
    @ApiOperation(value = "网点平台待抢单列表", httpMethod = "POST")
    @RequestMapping(method = RequestMethod.POST, value = "/robbingOrderPage")
    @ResponseBody
    public Json<Page<OrderDto>> robbingOrderPage(@RequestBody OrderPageQueryDto orderPageQueryDto) {
        Json json = new Json<>();
        Page<OrderDto> page = new Page<>();
        try {
            /**
             * 获取用户展示权限
             */
            List<Integer> hasAuthority = SecurityUtils.getHasAuthority();
            orderPageQueryDto.setOrderTypeList(hasAuthority);
            page = orderClient.robbingOrderPage(orderPageQueryDto);
            json.setSuccess(Boolean.TRUE);
            json.setObj(page);
        } catch (Exception e) {
            log.error("robbingOrderPage{}", e);
            json.setSuccess(Boolean.FALSE);
            json.setMsg("订单查询错误！");
        }
        return json;
    }

    @ApiOperation(value = "网点平台订单列表", httpMethod = "POST")
    @RequestMapping(method = RequestMethod.POST, value = "/queryOrderPage")
    @ResponseBody
    public Json<Page<OrderDto>> queryOrderPage(@RequestBody OrderPageQueryDto orderPageQueryDto) {
        Json json = new Json();
        try {
            User currentUser = securityHelper.getCurrentUser();
            //订单超时通过时间判断，无法通过状态判断
            if (orderPageQueryDto.getManageStatus() != null) {
                if (ManageStatusEnum.已超时.equals(orderPageQueryDto.getManageStatus())) {
                    orderPageQueryDto.setOvertime(new Date());
                    orderPageQueryDto.setManageStatus(null);
                }
            }
            orderPageQueryDto.setUserId(currentUser.getId().intValue());
            Page page = orderClient.queryOrderPage(orderPageQueryDto);
            json.setObj(page);
            json.setSuccess(Boolean.TRUE);
            json.setMsg("订单查询成功");
        } catch (BussException e) {
            json.setMsg(e.getMessage());
            json.setSuccess(Boolean.FALSE);
        } catch (Exception e) {
            json.setMsg("服务器内部错误");
            json.setSuccess(Boolean.FALSE);
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
            //Todo 判断这个用户有没有这个订单权限
//            OrderDto order = orderService.findOrderById(orderId);
//            if (order != null && StringUtil.isNotEmpty(order.getId())) {
//                if (!order.getUserId().equals(securityHelper.getCurrentUser().getId())) {
//                    throw new BussException("您没有查看该订单日志的权限");
//                }
//            }
            json.setSuccess(true);
            json.setObj(operateLogService.findByAble("Order", orderId));
        } catch (BussException e) {
            json.setSuccess(false);
            json.setMsg(e.getMessage());
            log.warn("operateLog {}", e);
        } catch (Exception e) {
            json.setSuccess(false);
            json.setMsg("系统内部错误");
            log.error("operateLog {}", e);
        }
        return json;
    }

    /**
     * 查询订单详情
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

    @ApiOperation(value = "订单退回操作接口", httpMethod = "POST")
    @PostMapping("/sendBackOrder")
    public Json sendBackOrder(@RequestBody OrderDto orderDto) {
        Json json = orderService.sendBackOrder(orderDto);
        return json;

    }

    @ApiOperation(value = "已抢订单确认接口", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单ID", dataType = "String", paramType = "path", required = true)
    })
    @GetMapping("/confirmGrabbedOrder/{orderId}")
    public Json confirmGrabbedOrder(@PathVariable("orderId") String orderId) {
        Json json = new Json();
        try {
            json.setSuccess(true);
            User currentUser = securityHelper.getCurrentUser();
            orderService.confirmGrabbedOrder(orderId, currentUser.getId().intValue());
            json.setSuccess(Boolean.TRUE);
            json.setMsg("已抢订单确认成功！");
            operateLogService.addOperateLog(securityHelper.getCurrentUser().getId(),
                    "Order", orderId, OperateTypeEnum.抢单确认成功, "抢单确认成功");

            //抢单成功消息确认
            messageService.rushOrderSuccessConfirm(currentUser.getId().intValue(), orderId, MessageTypeEnum.抢单成功.getCode());
        } catch (BussException e) {
            json.setMsg(e.getMessage());
        } catch (Exception e) {
            json.setMsg("服务器内部错误");
//            operateLogService.addOperateLog(securityHelper.getCurrentUser().getId(),
//                    "Order",orderId, OperateTypeEnum.抢单确认失败, "抢单确认失败");
        }
        return json;
    }

    @ApiOperation(value = "更新订单的暂存状态,用户所有订单中只能出现一条已暂存", httpMethod = "POST")
    @ApiImplicitParam(name = "orderId", value = "订单id", dataType = "String", paramType = "query", required = true)
    @PostMapping("/toggleOrderTempLockStatus")
    public Json<OrderDto> toggleOrderTempLockStatus(@RequestParam("orderId") String orderId) {
        return orderClient.toggleOrderTempLockStatus(orderId);
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

    @ApiOperation(value = "订单分类统计", httpMethod = "POST")
    @PostMapping("/countOrderTypeNum")
    @ResponseBody
    public Json<CencusDto> countOrderTypeNum(@RequestBody OrderQueryDto orderQueryDto) {
        User currentUser = securityHelper.getCurrentUser();
        orderQueryDto.setUserId(currentUser.getId().intValue());
        Json<CencusDto> json = orderClient.countOrderTypeNum(orderQueryDto);
        return json;
    }


    @ApiOperation(value = "我的订单", httpMethod = "POST")
    @PostMapping("/selectOrderDto")
    public Json<Page<OrderDto>> selectOrderDto(@RequestBody OrderPageQueryDto dto) {
        Json json = new Json();

        try {
            dto.setUserId(securityHelper.getCurrentUser().getId().intValue());
            if (dto.getStartDate() != null && dto.getEndDate() != null) {
                SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                dto.setStartOrderIn(sd.parse(dto.getStartDate()));
                dto.setEndOrderIn(sd.parse(dto.getEndDate()));
            }
            json.setSuccess(true);
            json.setObj(orderClient.selectOrderDto(dto));
        } catch (Exception e) {
            json.setSuccess(false);
            json.setMsg(e.getMessage());
            log.error("list {}", e);
        }
        return json;
    }
}
