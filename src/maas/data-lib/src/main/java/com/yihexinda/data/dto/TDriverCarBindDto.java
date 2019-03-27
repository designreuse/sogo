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
 * 司机车辆绑定记录表
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_driver_car_bind")
@ApiModel(value="TDriverCarBindDto对象", description="司机车辆绑定记录表")
public class TDriverCarBindDto extends Model<TDriverCarBindDto> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "司机id")
    private String driverId;

    @ApiModelProperty(value = "车辆id")
    private String carId;

    @ApiModelProperty(value = "绑定状态( 0未绑定  1已绑定  3注销 )")
    private String bindStatus;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "创建人")
    private String createId;

    @ApiModelProperty(value = "创建时间")
    private Date updateDate;

    @ApiModelProperty(value = "更新人")
    private String updateId;

    @ApiModelProperty(value = "司机名称")
    @TableField(exist = false)
    private String name;

    @ApiModelProperty(value = "车牌号")
    @TableField(exist = false)
    private String carNo;

    @ApiModelProperty(value = "车辆类型")
    @TableField(exist = false)
    private String type;

    @ApiModelProperty(value = "车辆载客人数")
    @TableField(exist = false)
    private String passNums;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
