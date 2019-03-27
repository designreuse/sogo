package com.yihexinda.data.dto;

import com.yihexinda.core.abst.AbstractDto;
//import com.yihexinda.data.enums.UserOnlineStatusEnum;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 根据一体化平台的用户信息来操作本地的简单用户信息表,查看更新及保存
 *
 * @author: yuanbailin
 * @create: 2018-10-22 09:53
 **/
@Data
@ApiModel("根据用户信息操作本地用户表的返回实体")
public class UserDto extends AbstractDto {

    private static final long serialVersionUID = 8496815705405796851L;
    /**
     * 主键ID
     */
    @ApiModelProperty("主键ID")
    private Integer userId;

    /**
     * 公司ID
     */
    @ApiModelProperty("公司ID")
    private String compId;

    /**
     * 部门ID
     */
    @ApiModelProperty("部门ID")
    private String deptId;

    /**
     * 公司名称
     */
    @ApiModelProperty("公司名称")
    private String compName;

    /**
     * 部门名称
     */
    @ApiModelProperty("部门名称")
    private String deptName;

    /**
     * 姓名
     */
    @ApiModelProperty("姓名")
    private String name;

    /**
     * 登陆账号
     */
    @ApiModelProperty("登陆账号")
    private String username;

    /**
     * 用户公司所在城市
     */
    @ApiModelProperty("用户公司所在城市")
    private String compCity;

    /**
     * 用户部门所在城市
     */
    @ApiModelProperty("用户部门所在城市")
    private String deptCity;

//    @ApiModelProperty("用户在线状态：1-在线，2-忙碌，3-离线")
//    private UserOnlineStatusEnum onlineStatus;

    /**
     * 分组
     */
    @ApiModelProperty("分组 一般不用关心该字段")
    private String group;

    /**
     * 客户所在地城市
     */
    @ApiModelProperty("客户所在地城市")
    private String customerCity;

    @ApiModelProperty("始发地城市")
    private String departureCity;

    @ApiModelProperty("目的地城市")
    private String destinationCity;

    @ApiModelProperty(hidden = true)
    private boolean force;

}
