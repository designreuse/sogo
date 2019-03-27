package com.yihexinda.data.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 行程评价表
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_travel_evaluate")
@ApiModel(value="TTravelEvaluateDto对象", description="行程评价表")
public class TTravelEvaluateDto extends Model<TTravelEvaluateDto> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "关联司机id")
    private String driverUserId;

    @ApiModelProperty(value = "关联行程id")
    private String routeId;

    @ApiModelProperty(value = "行程评价内容")
    private String content;

    @ApiModelProperty(value = "关联用户")
    private String userId;

    @ApiModelProperty(value = "服务星级( 1,2,3,4,5 )")
    private String startNo;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "创建人")
    private String createId;

    @ApiModelProperty(value = "创建时间")
    private Date updateDate;

    @ApiModelProperty(value = "更新人")
    private String updateId;

    @ApiModelProperty(value = "车内环境星级（1,2,3,4,5）")
    private String carEnvStartNo;

    @ApiModelProperty(value = "订单id")
    private String orderId;

    @ApiModelProperty(value = "行程类型")
    private String routeType;

    @ApiModelProperty(value = "上车站点名称")
    @TableField(exist = false)
    private String startName;

    @ApiModelProperty(value = "下车站点名称")
    @TableField(exist = false)
    private String endName;

    @ApiModelProperty(value = "司机名称")
    @TableField(exist = false)
    private String name;

    @ApiModelProperty(value = "用户名称")
    @TableField(exist = false)
    private String nick;

    @ApiModelProperty(value = "订单编号")
    @TableField(exist = false)
    private String orderNo;

    @ApiModelProperty(value = "用车状态（0即时，1预约）")
    @TableField(exist = false)
    private String orderType;

    @ApiModelProperty(value = "购票类型（0单票，1多票）")
    @TableField(exist = false)
    private String ticketType;

    @ApiModelProperty(value = "上车时间")
    @TableField(exist = false)
    private Date rideStartDate;

    @ApiModelProperty(value = "下车时间")
    @TableField(exist = false)
    private Date rideEndDate;

    @ApiModelProperty(value = "站点距离")
    @TableField(exist = false)
    private String siteDis;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
