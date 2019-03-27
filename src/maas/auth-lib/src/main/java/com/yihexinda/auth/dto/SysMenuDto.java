package com.yihexinda.auth.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.ToString;

/**
 * Created by T470p on 2018/10/19.
 */
@Data
@ToString
public class SysMenuDto {
    /**
     * 菜单id
     */
    private Integer menuId;

    /**
     * 系统id
     */
    private String systemId;

    /**
     * 名称
     */
    private String name;

    /**
     * 路径
     */
    private String path;

    /**
     *
     */
    private String url;

    /**
     * 父类ID
     */
    private Integer parentId;

    /**
     * 图标
     */
    private String icon;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 类型
     */
    private String type;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新新人
     */
    private String updateUser;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 删除标识
     */
    private String delFlg;

    /**
     * 版本号
     */
    private Long version;

    private List<SysMenuDto> children = new ArrayList<SysMenuDto>();

}
