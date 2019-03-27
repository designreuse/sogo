package com.yihexinda.data.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * 用户反馈表
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_user_feed")
@ApiModel(value="TUserFeedDto对象", description="用户反馈表")
public class TUserFeedDto extends Model<TUserFeedDto> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "关联用户")
    private String userId;
    
    @ApiModelProperty(value = " 0小程序用户  1 司机用户")
    private String userType;

    @ApiModelProperty(value = "反馈内容")
    private String feedContent;

    @ApiModelProperty(value = "手机号码")
    private String mobile;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "创建人")
    private String createId;

    @ApiModelProperty(value = "创建时间")
    private Date updateDate;

    @ApiModelProperty(value = "更新人")
    private String updateId;

    @ApiModelProperty(value = "用户名称")
    @TableField(exist = false)
    private String userName;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
