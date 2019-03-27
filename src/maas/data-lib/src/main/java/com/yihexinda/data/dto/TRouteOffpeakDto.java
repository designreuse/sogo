package com.yihexinda.data.dto;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * 平峰行程信息表
 * </p>
 *
 * @author wenbn
 * @since 2018-12-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_route_offpeak")
@ApiModel(value="TRouteOffpeakDto对象", description="平峰行程信息表")
public class TRouteOffpeakDto extends Model<TRouteOffpeakDto> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "关联司机id")
    private String driverUserId;

    @ApiModelProperty(value = "关联车辆id")
    private String carId;

    @ApiModelProperty(value = "行程状态(0:未完成  1：已完成)")
    private String routeState;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "创建人")
    private String createId;

    @ApiModelProperty(value = "创建时间")
    private Date updateDate;

    @ApiModelProperty(value = "更新人")
    private String updateId;

    @ApiModelProperty(value = "平峰途径站点（1，2，3）")
    private String midwayStation;

    @ApiModelProperty(value = "已到站点")
    private String currStation;

    @ApiModelProperty(value = "保存所有导航数据")
    private String allPosition;

    @ApiModelProperty(value = "开始时间")
    private Date startDate;

    @ApiModelProperty(value = "结束时间")
    private Date endDate;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
