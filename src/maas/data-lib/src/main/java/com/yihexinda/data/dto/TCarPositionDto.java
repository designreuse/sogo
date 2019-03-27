package com.yihexinda.data.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import java.io.Serializable;

import com.sun.corba.se.impl.presentation.rmi.IDLType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 车辆实时位置记录表
 * </p>
 *
 * @author wenbn
 * @since 2018-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_car_position")
@ApiModel(value="TCarPositionDto对象", description="车辆实时位置记录表")
public class TCarPositionDto extends Model<TCarPositionDto> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "车辆id")
    private String carId;

    @ApiModelProperty(value = "上一时刻经度")
    private Double lastLongitude;

    @ApiModelProperty(value = "上一时刻纬度")
    private Double lastLatitude;

    @ApiModelProperty(value = "经度")
    private Double longitude;

    @ApiModelProperty(value = "纬度")
    private Double latitude;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "创建人")
    private String createId;

    @ApiModelProperty(value = "更新时间")
    private Date updateDate;

    @ApiModelProperty(value = "更新人")
    private String updateId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
