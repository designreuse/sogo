package com.yihexinda.data.dto;

import com.yihexinda.core.dto.PageParamEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel("用户查询条件")
public class UserQueryDto extends PageParamEntity {
    /**
     * 公司ID
     */
    @ApiModelProperty("公司ID")//
    private String compId;

    /**
     * 部门ID
     */
    @ApiModelProperty("部门ID")//
    private String deptId;

    /**
     * 姓名
     */
    @ApiModelProperty("姓名")//
    private String name;

    /**
     * 登陆账号
     */
    @ApiModelProperty("登陆账号")
    private String username;//
}
