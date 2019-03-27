package com.yihexinda.core.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wenbn
 * @version 1.0
 * @date 2018/12/7 0007
 */
@Data
public class BaseParams {

    /**
     * 用户登录标识
     */
    @ApiModelProperty("用户登录标识")//
    private String token;
}
