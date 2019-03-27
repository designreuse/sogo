package com.yihexinda.data.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 排班信息表
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_schedule_work")
@ApiModel(value="TScheduleWorkDto对象", description="排班信息表")
public class TScheduleWorkDto extends Model<TScheduleWorkDto> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "休息时间")
    private String holidays;

    @ApiModelProperty(value = "司机id")
    private String driverId;

    @ApiModelProperty(value = "排班状态(0停用 1使用)")
    private String status;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "创建人")
    private String createId;

    @ApiModelProperty(value = "修改时间")
    private Date updateDate;

    @ApiModelProperty(value = "更新人")
    private String updateId;

    @ApiModelProperty(value = "开始时间")
    private Date startDate;

    @ApiModelProperty(value = "结束时间")
    private Date endDate;

    @ApiModelProperty(value = "排班名称")
    private String scheduleName;

    @ApiModelProperty(value = "工时列表")
    @TableField(exist = false)
    private List<TWorkHourDto> workHourList;

    @ApiModelProperty(value = "创建人名称")
    @TableField(exist = false)
    private String userName;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
