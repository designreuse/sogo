package com.yihexinda.data.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
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
 * 广播信息表
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_broadcast")
@ApiModel(value="TBroadcastDto对象", description="广播信息表")
public class TBroadcastDto extends Model<TBroadcastDto> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "广播信息")
    private String broadcastInfo;

    @ApiModelProperty(value = "广播状态(0 下线  1上线 )")
    private String status;

    @ApiModelProperty(value = "下线时间")
    private Date offlineTime;

    @ApiModelProperty(value = "上线时间")
    private Date onlineTime;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "创建人")
    private String createId;

    @ApiModelProperty(value = "创建时间")
    private Date updateDate;

    @ApiModelProperty(value = "更新人")
    private String updateId;

    @ApiModelProperty(value = "运营时间")
    @TableField(exist = false)
    private List<TBoradcastTimeRangeDto> boradcastTimeRangeDtoList;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
