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
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 高峰行程信息表
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_route_peak")
@ApiModel(value="TRoutePeakDto对象", description="高峰行程信息表")
public class TRoutePeakDto extends Model<TRoutePeakDto> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.UUID)
    private String id;


    @ApiModelProperty(value = "高峰线路id")
    private String lineId;

    @ApiModelProperty(value = "关联司机id")
    private String driverUserId;

    @ApiModelProperty(value = "发车站点id")
    private String lineStartId;

    @ApiModelProperty(value = "终点站点id")
    private String lineEndId;

    @ApiModelProperty(value = "开始时间")
    private Date startDate;

    @ApiModelProperty(value = "结束时间")
    private Date endDate;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "创建人")
    private String createId;

    @ApiModelProperty(value = "创建时间")
    private Date updateDate;

    @ApiModelProperty(value = "更新人")
    private String updateId;

    /**
     * 取系统表字段，因去除高峰运营时间表字段
     */
    @ApiModelProperty(value = "高峰运营时间段")
    @TableField(exist = false)
    private String operateTime;

    @ApiModelProperty(value = "线路名称")
    @TableField(exist = false)
    private String lineName;

    @ApiModelProperty(value = "线路起点")
    @TableField(exist = false)
    private String lineStartName;

    @ApiModelProperty(value = "线路终点")
    @TableField(exist = false)
    private String lineEndName;

    @ApiModelProperty(value = "线路状态")
    @TableField(exist = false)
    private String lineState;

    @ApiModelProperty(value = "发车间隔时间")
    @TableField(exist = false)
    private Integer timeLip;

    @ApiModelProperty(value = "价格")
    @TableField(exist = false)
    private BigDecimal price;

    @ApiModelProperty(value = "创建人名称")
    @TableField(exist = false)
    private String createName;

    @ApiModelProperty(value = "高峰运营时间段")
    @TableField(exist = false)
    private List<TRoutePeakTimeRangeDto> routePeakTimeRangeList;

    @ApiModelProperty(value = "线路信息表")
    @TableField(exist = false)
    private TLineDto lineDto;

    @ApiModelProperty(value = "途径站点表")
    @TableField(exist = false)
    private  List<TViaDto> viaList;

    @ApiModelProperty(value = "区域id")
    @TableField(exist = false)
    private String areaPath;

    @ApiModelProperty(value = "区域名称")
    @TableField(exist = false)
    private String regionAddress;

    @ApiModelProperty(value = "行程状态(0:未完成  1：已完成)")
    private String routeState;

    @ApiModelProperty(value = "车辆ID)")
    private String carId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
