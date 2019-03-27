package com.yihexinda.auth.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Page分页类 on 2018/10/19.
 * 根据业务定义入参参数，不要把没有用的参数定义在这里
 */
@Data
@ApiModel("用户查询返回实体")
public class SysUserNameResponseDto {

    /**
     * 主键id
     */
    @ApiModelProperty("用户id")
    private Integer userId;

    /**
     * 登录账号
     */
    @ApiModelProperty("登录账号")
    private String username;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String name;
}
