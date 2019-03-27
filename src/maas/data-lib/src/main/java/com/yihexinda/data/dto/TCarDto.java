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
 * 车辆信息表
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_car")
@ApiModel(value="TCarDto对象", description="车辆信息表")
public class TCarDto extends Model<TCarDto> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "车辆类型(0小巴 1中巴 2大巴)")
    private String type;

    @ApiModelProperty(value = "车辆载客人数")
    private Integer passNums;

    @ApiModelProperty(value = "车牌号牌照")
    private String license;

    @ApiModelProperty(value = "车牌号")
    private String carNo;

    @ApiModelProperty(value = "车辆状态(0停用 1使用)")
    private String status;

    @ApiModelProperty(value = "关联装置设备id")
    private String deviceId;

    @ApiModelProperty(value = "最后一级区域Id")
    private String areaId;

    @ApiModelProperty(value = "省级id_市级id_县级Id_ 例如:110000_110100_110101_")
    private String areaPath;

    @ApiModelProperty(value = "车辆停放地址(详细地址)")
    private String detailAddr;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "创建人")
    private String createId;

    @ApiModelProperty(value = "创建时间")
    private Date updateDate;

    @ApiModelProperty(value = "更新人")
    private String updateId;

    @ApiModelProperty(value = "省市级名称")
    @TableField(exist = false)
    private String regionAddress;

    @ApiModelProperty(value = "创建人名称")
    @TableField(exist = false)
    private String createName;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
