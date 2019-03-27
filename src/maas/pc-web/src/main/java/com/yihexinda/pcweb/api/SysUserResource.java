package com.yihexinda.pcweb.api;


import com.alibaba.fastjson.JSONObject;
import com.yihexinda.core.utils.JsonUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yihexinda.core.annotation.NoRequireLogin;
import com.yihexinda.core.constants.ResultConstant;
import com.yihexinda.core.utils.RequestUtil;
import com.yihexinda.core.utils.StringUtil;
import com.yihexinda.core.vo.PayLoadVo;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TDriverDto;
import com.yihexinda.data.dto.TSysUserDto;
import com.yihexinda.pcweb.client.DriverCarBindClient;
import com.yihexinda.pcweb.client.DriverClient;
import com.yihexinda.pcweb.client.SysUserClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author pengfeng
 * @date 2018/12/08
 */
@Api(description = "系统用户接口")
@RestController()
@RequestMapping("api/pc")
@Slf4j
public class SysUserResource {

    @Autowired
    private SysUserClient sysUserClient;

    /**
     *  用户登录
     * @param paramter map参数
     * @return
     */
    @ApiOperation(value = "用户登录", httpMethod = "POST")
    @PostMapping("/loginUser")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, paramType = "string"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "string"),
    })
    @NoRequireLogin
    public ResultVo loginUser(@RequestBody Map<String,String> paramter){
        if(StringUtil.isEmpty(paramter.get("userName")) || StringUtil.isEmpty(paramter.get("password"))){
            return ResultVo.error(ResultConstant.SYS_REQUIRED_FAILURE, ResultConstant.SYS_REQUIRED_FAILURE_VALUE);
        }
        return sysUserClient.loginUser(paramter);
    }

    /**
     *  退出登录
     * @param paramter map参数
     * @return
     */
    @ApiOperation(value = "退出登录", httpMethod = "POST")
    @PostMapping("/loginout")
    @NoRequireLogin
    public ResultVo loginout(@RequestBody Map<String,String> paramter){
        String token = StringUtil.trim(paramter.get("token"));
        if(StringUtil.isEmpty(token)){
            return ResultVo.error(ResultVo.Status.REQUIRED_PARAMETER_ERROR);
        }

        return sysUserClient.loginout(paramter);
    }

    /**
     * 查询用户信息列表
     * @return 用户信息列表
     */
    @ApiOperation(value = "查询用户信息列表", httpMethod = "POST")
    @RequestMapping(value = "/getUserList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "页码", required = true, paramType = "string"),
            @ApiImplicitParam(name = "pageSize", value = "条数", required = true, paramType = "string"),
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string")
    })
    public ResultVo getUserList(@RequestBody Map<String,Object> condition) {
        return sysUserClient.getUserList(condition);
    }

    /**
     * 查询用户详情
     * @param data  用户id
     * @return 用户详情信息
     */
    @ApiOperation(value = "查询用户详情", httpMethod = "POST")
    @RequestMapping(value = "/getUser")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = true, paramType = "string"),
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string")
    })
    public ResultVo getUser(@RequestBody Map<String,Object> data) {
        return sysUserClient.getUser(String.valueOf(data.get("id")));
    }

    /**
     * 新增用户信息
     * @param data 用户信息
     * @return ResultVo
     */
    @ApiOperation(value = "添加用户信息", httpMethod = "POST")
    @RequestMapping(value = "/addUser")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tSysUserDto", value = "用户信息", required = true, paramType = "string"),
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string")
    })
    public ResultVo addUser(@RequestBody Map<String,Object> data) {
        TSysUserDto tSysUserDto = JSONObject.parseObject(JsonUtil.toJson(data.get("tSysUserDto")),TSysUserDto.class);
        return sysUserClient.addUser(tSysUserDto);
    }

    /**
     * 修改用户信息
     * @param data 用户信息
     * @return ResultVo
     */
    @ApiOperation(value = "修改用户信息", httpMethod = "POST")
    @RequestMapping(value = "/updateUser")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tSysUserDto", value = "用户信息", required = true, paramType = "string"),
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string")
    })
    public ResultVo updateUser(@RequestBody Map<String,Object> data) {
        TSysUserDto tSysUserDto = JSONObject.parseObject(JsonUtil.toJson(data.get("tSysUserDto")),TSysUserDto.class);
        return sysUserClient.updateUser(tSysUserDto);
    }

    /**
     * 角色下用户列表
     * @param data  角色id
     * @return 用户信息列表
     */
    @ApiOperation(value = "查询角色用户信息列表", httpMethod = "POST")
    @RequestMapping(value = "/getRoleUser")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色id", required = true, paramType = "string"),
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string")
    })
    public ResultVo getRoleUser(@RequestBody Map<String,Object> data) {
       return sysUserClient.getRoleUser(String.valueOf(data.get("roleId")));
    }

    /**
     * 重置用户密码
     * @param data  用户id
     * @return ResultVo
     */
    @ApiOperation(value = "重置用户密码", httpMethod = "POST")
    @RequestMapping(value = "/updatePassWord")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "string"),
            @ApiImplicitParam(name = "passWord", value = "密码", required = true, paramType = "string"),
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string")
    })
    public ResultVo updatePassWord(@RequestBody Map<String,Object> data) {
        TSysUserDto tSysUserDto = new TSysUserDto();
        tSysUserDto.setId(StringUtil.trim(data.get("userId")));
        tSysUserDto.setPassword(StringUtil.trim(data.get("passWord")));
        return sysUserClient.updateUser(tSysUserDto);
    }



}
