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
 * 广播时间段表
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_boradcast_time_range")
@ApiModel(value="TBoradcastTimeRangeDto对象", description="")
public class TBoradcastTimeRangeDto extends Model<TBoradcastTimeRangeDto> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "广播id")
    private String boradcastId;

    @ApiModelProperty(value = "显示开始时间")
    private Date startDate;

    @ApiModelProperty(value = "显示结束时间")
    private Date endDate;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}