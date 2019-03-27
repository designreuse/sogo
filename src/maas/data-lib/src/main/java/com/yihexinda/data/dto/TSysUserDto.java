package com.yihexinda.data.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import java.util.List;

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
@TableName("t_sys_user")
@ApiModel(value="TSysUserDto对象", description="")
public class TSysUserDto extends Model<TSysUserDto> {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ID_WORKER_STR)
    private String id;

    private String username;

    private String password;

    private String timeCreate;

    private String cname;

    private String phone;

    private String mail;

    private String timeUpdate;

    private Integer loginerrortimes;

    private String isdelete;

    private String usertype;

    private String ipNologin;

    @ApiModelProperty(value = "角色列表")
    @TableField(exist = false)
    private List<TSysRoleUserDto> roleUserDtoList;

    @Override
    protected Serializable pkVal() {
        return null;
    }

}
