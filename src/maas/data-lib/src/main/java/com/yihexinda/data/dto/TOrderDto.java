package com.yihexinda.data.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.math.BigDecimal;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单信息表
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_order")
@ApiModel(value="TOrderDto对象", description="订单信息表")
public class TOrderDto extends Model<TOrderDto> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "在线支付交易流水")
    private String tradeNo;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "关联车辆id")
    private String carId;

    @ApiModelProperty(value = "关联司机id")
    private String driverId;

    @ApiModelProperty(value = "关联行程id")
    private String routeId;

    @ApiModelProperty(value = "时间索引段(用于记录某个时间段预约的人数)")
    private Integer prepareTimeIdx;

    @ApiModelProperty(value = "高峰是只能1个，平峰可以为多个人")
    private Integer passengersNum;

    @ApiModelProperty(value = "出行时间(预约出行的时间)")
    private Date tripTime;

    @ApiModelProperty(value = "上车站点id")
    private String startStationId;

    @ApiModelProperty(value = "下车站点id")
    private String endStationId;

    @ApiModelProperty(value = "关联用户")
    private String userId;

    @ApiModelProperty(value = "最后一级区域Id")
    private String areaId;

    @ApiModelProperty(value = "省级id_市级id_县级Id_ 例如:110000_110100_110101_")
    private String areaPath;

    @ApiModelProperty(value = "站点距离")
    private String siteDis;

    @ApiModelProperty(value = "订单金额")
    private BigDecimal orderAmount;

    @ApiModelProperty(value = "订单类型( 0 即时 1预约 )")
    private String orderType;

    @ApiModelProperty(value = "用车状态( 0 单票  1多票 )")
    private String ticketType;

    @ApiModelProperty(value = "订单状态( 0 创建订单 1待使用 2进行中 3已完成 4已失效 )")
    private String orderStatus;

    @ApiModelProperty(value = "是否退款 (0:否 1：是)")
    private String isRefund;

    @ApiModelProperty(value = "0:未支付 1:已支付")
    private String isPay;

    @ApiModelProperty(value = "下单时间")
    private Date createDate;

    @ApiModelProperty(value = "创建人")
    private String createId;

    @ApiModelProperty(value = "创建时间")
    private Date updateDate;

    @ApiModelProperty(value = "更新人")
    private String updateId;

    @ApiModelProperty(value = "乘车开始时间")
    private Date rideStartDate;

    @ApiModelProperty(value = "乘车结束时间")
    private Date rideEndDate;

    @ApiModelProperty(value = "行程类型( 0 平峰 1高峰 )")
    private String routeType;

    @ApiModelProperty(value = "是否取消行程（0：未取消 ，1：用户取消，2系统取消）")
    private String isCancel;

    @ApiModelProperty(value = "高峰线路ID")
    private String lineId;

    @ApiModelProperty(value = "车票过期时间")
    private Date expDate;

    @ApiModelProperty(value = "用户订单导航数据")
    private String orderPosition;

    @ApiModelProperty(value = "支付时间")
    private Date payDate;

    @ApiModelProperty(value = "分配车辆时间")
    private Date allotDate;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
