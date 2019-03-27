package com.yihexinda.core.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author wenbn
 * @version 1.0
 * @date 2018/5/22
 */
@Data
public class PayLoadVo {
    private String iss;//jwt签发者
    private Date exp;  //jwt的过期时间，这个过期时间必须要大于签发时间
    private Date iat;  //jwt的签发时间
    private String loginLogo; //登录标识
    private String userId;
    private String openId;
    private String nick;
    private String phone;

}
