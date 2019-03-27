package com.yihexinda.core.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wenbn
 * @version 1.0
 * @date 2018/12/4 0004
 */
@Data
@ApiModel("二维码回调参数")
public class QRCodeParam {

    /**
     * 订单ID
     */
    @ApiModelProperty("订单ID")//
    private String oid;

    /**
     * 用户登录标识
     */
    @ApiModelProperty("用户登录标识")//
    private String token;

    /**
     * 过期时间
     */
    @ApiModelProperty("过期时间")//
    private String expTime;

    /**
     * 当前时间
     */
    @ApiModelProperty("系统当前时间")//
    private String nowTime;


    /**
     * 装置码
     */
    @ApiModelProperty("装置码")//
    private String deviceCode;

}
