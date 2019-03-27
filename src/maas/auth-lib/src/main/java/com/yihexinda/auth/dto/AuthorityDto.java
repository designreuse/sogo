package com.yihexinda.auth.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

/**
 * 权限使用菜单配置的Button
 *
 * @author Jack
 * @date 2018/10/24.
 */
@Data
@ToString
public class AuthorityDto implements Serializable {
    private static final long serialVersionUID = -9218032468120754360L;
    private Integer id;
    private String name;
    private String desc;

    public static AuthorityDto getFromSysMenuDto(SysMenuDto sysMenuDto) {
        AuthorityDto authority = new AuthorityDto();
        authority.setId(sysMenuDto.getMenuId());
        authority.setName(sysMenuDto.getPath());
        authority.setDesc(sysMenuDto.getName());
        return authority;
    }
}
