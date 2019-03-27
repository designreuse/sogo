package com.yihexinda.auth.dto;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * Created by T470p on 2018/10/19.
 */
@Data
@ToString
@ApiModel("根据登录账号查询用户的返回实体")
public class SysUserDto implements Serializable{

    /**
     * ID
     */
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 公司id
     */
    @ApiModelProperty("公司id")
    private String compId;

    /**
     * 公司名称
     */
    @ApiModelProperty("公司名称")
    private String compName;

    /**
     * 公司城市
     */
    @ApiModelProperty("公司城市")
    private String compCity;

    /**
     * 部门城市
     */
    @ApiModelProperty("部门城市")
    private String deptCity;

    /**
     * 部门id
     */
    @ApiModelProperty("部门id")
    private String deptId;

    /**
     * 部门名称
     */
    @ApiModelProperty("部门名称")
    private String deptName;

    /**
     * 登录名称
     */
    @ApiModelProperty("登录名称")
    private String username;

    /**
     * 登录密码
     */
    @ApiModelProperty("登录密码")
    private String password;

    /**
     * 姓名
     */
    @ApiModelProperty("姓名")
    private String name;

    /**
     * 性别 1：男、0：女
     */
    @ApiModelProperty("性别 1：男、0：女")
    private String sex;

    /**
     * 头像
     */
    @ApiModelProperty("头像")
    private String avatar;

    /**
     * 联系电话
     */
    @ApiModelProperty("联系电话")
    private String tel;

    /**
     * 手机
     */
    @ApiModelProperty("手机")
    private String mobile;

    /**
     * 电子邮箱
     */
    @ApiModelProperty("电子邮箱")
    private String email;

    /**
     * qq
     */
    @ApiModelProperty("qq")
    private String qq;

    /**
     * 微信
     */
    @ApiModelProperty("微信")
    private String wechart;

    /**
     * 地址
     */
    @ApiModelProperty("地址")
    private String addr;

    /**
     * 错误密码次数
     */
    @ApiModelProperty("错误密码次数")
    private Integer pwdErrorTimes;

    /**
     * 最后登录时间
     */
    @ApiModelProperty("最后登录时间")
    private Long lastLoginTime;

    /**
     * 有效期
     */
    @ApiModelProperty("有效期")
    private Date expiry;

    /**
     * 在职状态	 0:在职，1:离职
     */
    @ApiModelProperty("在职状态 0:在职，1:离职")
    private String status;

    /**
     * 微信openid
     */
    @ApiModelProperty("微信openid")
    private String wxOpenid;

    /**
     * QQ openid
     */
    @ApiModelProperty("QQ openid")
    private String qqOpenid;

    /**
     * 创建者
     */
    @ApiModelProperty("创建者")
    private String createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 更新者
     */
    @ApiModelProperty("更新者")
    private String updateUser;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;

    /**
     * 删除标识	 1:删除 0:有效
     */
    @ApiModelProperty("删除标识 1:删除 0:有效")
    private String delFlg;

    /**
     * 版本号
     */
    @ApiModelProperty("")
    private Long version;


}
