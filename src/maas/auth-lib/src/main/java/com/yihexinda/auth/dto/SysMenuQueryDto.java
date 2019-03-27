package com.yihexinda.auth.dto;

import java.io.Serializable;

import com.yihexinda.auth.enums.SysMenuType;

import lombok.Data;
import lombok.ToString;

/**
 * Created by T470p on 2018/10/19.
 */
@Data
@ToString
public class SysMenuQueryDto implements Serializable {

    /**
     * 菜单id
     */
    private Integer menuId;

    /**
     * 系统id
     */
    private Integer systemId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * type 0 菜单， 1 按钮（现在用按钮作为权限使用）
     */
    private SysMenuType type;

}
