package com.yihexinda.data.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 途经站点表（ 子表）
 * </p>
 *
 * @author wenbn
 * @since 2018-12-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_via")
@ApiModel(value="TViaDto对象", description="途经站点表（ 子表）")
public class TViaDto extends Model<TViaDto> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "线路关联标识")
    private String lineId;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "第几站标识")
    private Integer index;

    @ApiModelProperty(value = "站点id")
    private String stationId;

    @ApiModelProperty(value = "站点名称")
    @TableField(exist = false)
    private String siteName;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
