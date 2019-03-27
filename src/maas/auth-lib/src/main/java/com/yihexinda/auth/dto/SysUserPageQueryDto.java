package com.yihexinda.auth.dto;

import com.yihexinda.core.dto.PageParamEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Page分页类 on 2018/10/19.
 * 根据业务定义入参参数，不要把没有用的参数定义在这里
 */
@Data
@ApiModel("用户查询请求实体")
public class SysUserPageQueryDto extends PageParamEntity {
    /**
     * 主键id
     */
    @ApiModelProperty("用户id")
    private Integer userId;

    /**
     * 公司id
     */
    @ApiModelProperty("公司id")
    private String compId;

//    /**
//     * 登录账号
//     */
//    @ApiModelProperty("登录账号")
//    private String username;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名或登录账号")
    private String name;
}
