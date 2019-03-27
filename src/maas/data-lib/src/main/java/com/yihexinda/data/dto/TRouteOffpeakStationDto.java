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
 * 平峰行程途径站点表
平峰行程信息表(子表)
 * </p>
 *
 * @author wenbn
 * @since 2018-12-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_route_offpeak_station")
@ApiModel(value="TRouteOffpeakStationDto对象", description="平峰行程途径站点表平峰行程信息表(子表)")
public class TRouteOffpeakStationDto extends Model<TRouteOffpeakStationDto> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "关联平峰行程id")
    private String routeId;

    @ApiModelProperty(value = "站点id")
    private String stationId;

    @ApiModelProperty(value = "下车人数")
    private Integer offNum;

    @ApiModelProperty(value = "上车人数")
    private Integer onNum;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
