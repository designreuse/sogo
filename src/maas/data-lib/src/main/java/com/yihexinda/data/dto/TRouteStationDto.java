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
 * 行程途径站点表
记录站点上车人数，下车人数


 * </p>
 *
 * @author wenbn
 * @since 2018-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_route_station")
@ApiModel(value="TRouteStationDto对象", description="行程途径站点表记录站点上车人数，下车人数")
public class TRouteStationDto extends Model<TRouteStationDto> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "关联行程id( 0:平峰 1:高峰)")
    private String routeId;

    @ApiModelProperty(value = "站点id")
    private String stationId;

    @ApiModelProperty(value = "下车人数")
    private Integer offNum;

    @ApiModelProperty(value = "上车人数")
    private Integer onNum;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "距离上一个站点里程数(km)")
    private Double preStationMile;

    @ApiModelProperty(value = "行程类型( 0:平峰 1:高峰)")
    private String routeType;

    @ApiModelProperty(value = "司机id")
    private String driverId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
