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
 * 
 * </p>
 *
 * @author wenbn
 * @since 2018-11-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_sys_role_user")
@ApiModel(value="TSysRoleUserDto对象", description="")
public class TSysRoleUserDto extends Model<TSysRoleUserDto> {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ID_WORKER_STR)
    private String id;

    private String roleId;

    private String userId;

    @ApiModelProperty(value = "用户名称")
    @TableField(exist = false)
    private String userName;

    @ApiModelProperty(value = "角色名称")
    @TableField(exist = false)
    private String roleName;



    @Override
    protected Serializable pkVal() {
        return null;
    }

}
