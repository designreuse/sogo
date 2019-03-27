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
 * 短信日志表
 * </p>
 *
 * @author wenbn
 * @since 2018-12-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_sms_log")
@ApiModel(value="TSmsLogDto对象", description="短信日志表")
public class TSmsLogDto extends Model<TSmsLogDto> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "发送手机")
    private String smsMobile;

    @ApiModelProperty(value = "发送编码")
    private String smsCode;

    @ApiModelProperty(value = "短信内容")
    private String smsContent;

    @ApiModelProperty(value = "短信类型( 0验证码  1通知 2营销 )")
    private String smsType;

    @ApiModelProperty(value = "ip地址")
    private String smsIp;

    @ApiModelProperty(value = "目标用户id")
    private String smsUserId;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
