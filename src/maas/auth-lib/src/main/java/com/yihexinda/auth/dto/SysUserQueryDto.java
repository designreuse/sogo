package com.yihexinda.auth.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * Created by  2018/10/19.
 * 根据业务添加参数，不可以添加不必要的参数
 */
@Data
@ToString
@ApiModel("根据登录账号查询用户的请求实体")
public class SysUserQueryDto implements Serializable {
    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Integer userId;

    /**
     * 登录账号
     */
    @ApiModelProperty("登录账号")
    private String username;

}
