package com.yihexinda.data.dto;

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

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 工时段统计dto
 * </p>
 *
 * @author pengfeng
 * @since 2018-12-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="工时段统计", description="工时段统计")
public class RouteCountDto extends Model<RouteCountDto> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableField(exist = false)
    private String id;

    @ApiModelProperty(value = "司机id")
    @TableField(exist = false)
    private String driverUserId;

    @ApiModelProperty(value = "总站点")
    @TableField(exist = false)
    private String totalStation;

    @ApiModelProperty(value = "上车人数")
    @TableField(exist = false)
    private Integer onNum;

    @ApiModelProperty(value = "下车人数")
    @TableField(exist = false)
    private Integer offNum;

    @ApiModelProperty(value = "距离")
    @TableField(exist = false)
    private Integer preStationMile;

    @ApiModelProperty(value = "司机名称")
    @TableField(exist = false)
    private String name;

    @ApiModelProperty(value = "行程类型")
    @TableField(exist = false)
    private String type;

    @ApiModelProperty(value = "开始时间")
    @TableField(exist = false)
    private Date startDate;

    @ApiModelProperty(value = "结束时间")
    @TableField(exist = false)
    private Date endDate;

    @ApiModelProperty(value = "发车站点")
    @TableField(exist = false)
    private String startName;

    @ApiModelProperty(value = "终点")
    @TableField(exist = false)
    private String endName;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
