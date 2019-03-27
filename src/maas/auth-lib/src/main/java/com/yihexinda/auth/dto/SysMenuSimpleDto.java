package com.yihexinda.auth.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

/**
 * @author Jack
 * @date 2018/10/24.
 */
@Data
@ToString
@ApiModel("菜单")
public class SysMenuSimpleDto {
    /**
     * 菜单id
     */
    private Integer menuId;

    /**
     * 名称
     */
    private String name;

    /**
     * 路径
     */
    private String path;

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

    private List<SysMenuSimpleDto> children = new ArrayList<SysMenuSimpleDto>();

    public static SysMenuSimpleDto getFromSysMenuDto(SysMenuDto sysMenuDto) {
        SysMenuSimpleDto simpleDto = new SysMenuSimpleDto();
        simpleDto.setMenuId(sysMenuDto.getMenuId());
        simpleDto.setName(sysMenuDto.getName());
        simpleDto.setPath(sysMenuDto.getPath());
        simpleDto.setParentId(sysMenuDto.getParentId());
        simpleDto.setIcon(sysMenuDto.getIcon());
        simpleDto.setSort(sysMenuDto.getSort());
        if (sysMenuDto.getChildren().size() > 0) {
            simpleDto.setChildren(sysMenuDto.getChildren().stream().map(x -> getFromSysMenuDto(x)).collect(Collectors.toList()));
        }
        return simpleDto;
    }
}
