package com.yihexinda.data.dto;

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
 * 行程信息表
 * </p>
 *
 * @author wenbn
 * @since 2018-12-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_route_info")
@ApiModel(value="TRouteInfoDto对象", description="行程信息表")
public class TRouteInfoDto extends Model<TRouteInfoDto> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "行程ID")
    private String routeId;

    @ApiModelProperty(value = "0 高峰 1平峰")
    private String routeType;

    @ApiModelProperty(value = "关联车辆id")
    private String routeCarId;

    @ApiModelProperty(value = "关联线路id")
    private String routeLineId;

    @ApiModelProperty(value = "关联司机id")
    private String routeDriverId;

    @ApiModelProperty(value = "0未结束  1结束")
    private String routeIsend;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "更新时间")
    private Date updateDate;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
