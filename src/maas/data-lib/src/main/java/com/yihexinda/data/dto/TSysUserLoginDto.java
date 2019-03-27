package com.yihexinda.data.dto;

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
 * 
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_sys_user_login")
@ApiModel(value="TSysUserLoginDto对象", description="")
public class TSysUserLoginDto extends Model<TSysUserLoginDto> {

    private static final long serialVersionUID = 1L;

    private String id;

    private String userId;

    private String sessionId;

    private String createtime;

    private String ip;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
