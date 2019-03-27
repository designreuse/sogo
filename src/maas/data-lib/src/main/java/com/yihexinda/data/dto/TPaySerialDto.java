package com.yihexinda.data.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 支付流水表
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_pay_serial")
@ApiModel(value = "TPaySerialDto对象", description = "支付流水表")
public class TPaySerialDto extends Model<TPaySerialDto> {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键")
	@TableId(type = IdType.UUID)
	private String id;

	@ApiModelProperty(value = "关联订单id")
	private String orderId;

	@ApiModelProperty(value = "关联用户")
	private String userId;

	@ApiModelProperty(value = "支付金额")
	private Double payAmount;

	@ApiModelProperty(value = "支付状态( 0 失败  1成功 )")
	private String payStatus;

	@ApiModelProperty(value = "创建时间")
	private Date createDate;

	@ApiModelProperty(value = "创建人")
	private String createId;

	@ApiModelProperty(value = "创建时间")
	private Date updateDate;

	@ApiModelProperty(value = "更新人")
	private String updateId;

	@ApiModelProperty(value = "退款时间")
	@TableField(exist = false)
	private Date refundTime;

	@ApiModelProperty(value = "用户名称")
	@TableField(exist = false)
	private String nick;

	@ApiModelProperty(value = "订单号")
	@TableField(exist = false)
	private String orderNo;

	@ApiModelProperty(value = "订单类型")
	@TableField(exist = false)
	private String orderType;

	@ApiModelProperty(value = "支付时间")
	@TableField(exist = false)
	private Date payTime;

	@ApiModelProperty(value = "订单流水号")
	@TableField(exist = false)
	private String tradeNo;

	@ApiModelProperty(value = "退款流水号")
	@TableField(exist = false)
	private String refundNo;

	@ApiModelProperty(value = "购票类型")
	@TableField(exist = false)
	private String ticketType;

	@ApiModelProperty(value = "退款金额")
	@TableField(exist = false)
	private Double refundAmount;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
