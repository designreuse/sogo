package com.yihexinda.data.dto;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.math.BigDecimal;
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
 * 高峰的线路信息表
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_line")
@ApiModel(value="TLineDto对象", description="高峰的线路信息表")
public class TLineDto extends Model<TLineDto> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "线路名称")
    private String lineName;

    @ApiModelProperty(value = "线路状态")
    private String lineState;

    @ApiModelProperty(value = "价格")
    private Double price;


    @ApiModelProperty(value = "发车间隔")
    private Integer timelip;

    @ApiModelProperty(value = "起点站点id")
    private String lineStartId;

    @ApiModelProperty(value = "终点站点id")
    private String lineEndId;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "创建人")
    private String createId;

    @ApiModelProperty(value = "创建时间")
    private Date updateDate;

    @ApiModelProperty(value = "更新人")
    private String updateId;

    private Integer totalStation;

    @ApiModelProperty(value = "线路标识")
    @TableField(exist = false)
    private Integer index;

    @ApiModelProperty(value = "站点id")
    @TableField(exist = false)
    private String stationId;

    @ApiModelProperty(value = "站点经度")
    @TableField(exist = false)
    private Double longitude;

    @ApiModelProperty(value = "站点纬度")
    @TableField(exist = false)
    private Double latitude;

    /**
     * 取系统表字段，因去除高峰运营时间表字段
     */
    @ApiModelProperty(value = "高峰运营时间段")
    @TableField(exist = false)
    private String operateTime;

    @ApiModelProperty(value = "线路起点")
    @TableField(exist = false)
    private String lineStartName;

    @ApiModelProperty(value = "线路终点")
    @TableField(exist = false)
    private String lineEndName;

    @ApiModelProperty(value = "创建人名称")
    @TableField(exist = false)
    private String createName;

    @ApiModelProperty(value = "高峰运营时间段")
    @TableField(exist = false)
    private List<TRoutePeakTimeRangeDto> routePeakTimeRangeList;

    @ApiModelProperty(value = "途径站点表")
    @TableField(exist = false)
    private  List<TViaDto> viaList;

    @ApiModelProperty(value = "最后一级区域id")
    private String areaId;

    @ApiModelProperty(value = "区域id")
    private String areaPath;

    @ApiModelProperty(value = "区域名称")
    @TableField(exist = false)
    private String regionAddress;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
