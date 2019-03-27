package com.yihexinda.data.dto;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * 高峰的线路信息表
 * </p>
 *
 * @author wenbn
 * @since 2018-12-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_line_driver_bind")
@ApiModel(value="TLineDriverBindDto对象", description="高峰的线路信息表")
public class TLineDriverBindDto extends Model<TLineDriverBindDto> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "司机id")
    private String driverId;

    @ApiModelProperty(value = "高峰线路id")
    private String lineId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
