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
 * 司机对应工时段
 * </p>
 *
 * @author wenbn
 * @since 2018-11-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_driver_work_hour")
@ApiModel(value="TDriverWorkHourDto对象", description="司机对应工时段")
public class TDriverWorkHourDto extends Model<TDriverWorkHourDto> implements  Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(type =IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "排班id")
    private String scheduleId;

    @ApiModelProperty(value = "司机id")
    private String driverId;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "启用状态(0停用 1使用)")
    private String status;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
