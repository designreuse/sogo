package com.yihexinda.data.dto;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.annotation.IdType;
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
 * 司机信息表
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_driver")
@ApiModel(value="TDriverDto对象", description="司机信息表")
public class TDriverDto extends Model<TDriverDto> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "工号")
    private String no;

    @ApiModelProperty(value = "联系电话")
    private String telephone;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "司机状态(0 停用 1启用)")
    private String status;

    @ApiModelProperty(value = "最后一级区域Id")
    private String areaId;

    @ApiModelProperty(value = "省级id_市级id_县级Id_ 例如:110000_110100_110101_")
    private String areaPath;

    @ApiModelProperty(value = "详细地址")
    private String detailAddr;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "创建人")
    private String createId;

    @ApiModelProperty(value = "创建时间")
    private Date updateDate;

    @ApiModelProperty(value = "更新人")
    private String updateId;

    @ApiModelProperty(value = "司机工时列表")
    @TableField(exist = false)
    private List<TDriverWorkHourDto> driverWorkHourList;

    @ApiModelProperty(value = "司机排班id")
    @TableField(exist = false)
    private String scheduleId;

    @ApiModelProperty(value = "司机排班名称")
    @TableField(exist = false)
    private String scheduleName;


    @ApiModelProperty(value = "创建人名称")
    @TableField(exist = false)
    private String userName;

    @ApiModelProperty(value = "省市级名称")
    @TableField(exist = false)
    private String regionAddress;

    @ApiModelProperty(value = "线路名称")
    @TableField(exist = false)
    private String lineName;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
