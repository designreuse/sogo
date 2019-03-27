package com.yihexinda.authservice.domian;

import java.util.Date;

import lombok.Data;

/**
 * 用户表
 * 
 * @author wcyong
 * 
 * @date 2018-10-19
 */
@Data
public class SysUser {
    /**
     * ID
     */
    private Integer userId;

    /**
     * 公司id
     */
    private String compId;

    /**
     * 部门id
     */
    private String deptId;

    /**
     * 登录名称
     */
    private String username;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 姓名
     */
    private String name;

    /**
     * 版本号
     */
    private String sex;

    /**
     * 版本号
     */
    private String avatar;

    /**
     * 版本号
     */
    private String tel;

    /**
     * 版本号
     */
    private String mobile;

    /**
     * 版本号
     */
    private String email;

    /**
     * 版本号
     */
    private String qq;

    /**
     * 版本号
     */
    private String wechart;

    /**
     * 版本号
     */
    private String addr;

    /**
     * 错误密码次数
     */
    private Integer pwdErrorTimes;

    /**
     * 最后登录时间
     */
    private Long lastLoginTime;

    /**
     * 版本号
     */
    private Date expiry;

    /**
     * 版本号
     */
    private String status;

    /**
     * 版本号
     */
    private String wxOpenid;

    /**
     * 版本号
     */
    private String qqOpenid;

    /**
     * 版本号
     */
    private String createUser;

    /**
     * 版本号
     */
    private Date createTime;

    /**
     * 版本号
     */
    private String updateUser;

    /**
     * 版本号
     */
    private Date updateTime;

    /**
     * 版本号
     */
    private String delFlg;

    /**
     * 版本号
     */
    private Long version;

}