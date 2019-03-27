package com.yihexinda.pcweb.api;


import com.alibaba.fastjson.JSONObject;
import com.yihexinda.core.utils.JsonUtil;
import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TDriverDto;
import com.yihexinda.data.dto.TSysRoleDto;
import com.yihexinda.pcweb.client.SysRoleClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author pengfeng
 * @date 2018/12/08
 */
@Api(description = "系统角色接口")
@RestController()
@RequestMapping("api/pc")
@Slf4j
public class SysRoleResource {

    @Autowired
    private SysRoleClient sysRoleClient;

    /**
     * 查询用户角色列表
     * @return 用户橘色列表
     */
    @ApiOperation(value = "查询用户角色列表", httpMethod = "POST")
    @RequestMapping(value = "/getRoleList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "页码", required = true, paramType = "string"),
            @ApiImplicitParam(name = "pageSize", value = "条数", required = true, paramType = "string"),
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string")
    })
    public ResultVo getRoleList(@RequestBody Map<String,Object> condition) {
        return sysRoleClient.getRoleList(condition);
    }

    /**
     * 查询角色详情
     * @param data  角色id
     * @return 用户详情信息
     */
    @ApiOperation(value = "查询角色详情", httpMethod = "POST")
    @RequestMapping(value = "/getRole")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "角色id", required = true, paramType = "string"),
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string")
    })
    public ResultVo getRole(@RequestBody Map<String,Object> data) {
        return sysRoleClient.getRole(String.valueOf(data.get("id")));
    }

    /**
     * 新增角色信息
     * @param data 角色信息
     * @return ResultVo
     */
    @ApiOperation(value = "添加用户信息", httpMethod = "POST")
    @RequestMapping(value = "/addRole")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tSysRoleDto", value = "角色信息", required = true, paramType = "string"),
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string")
    })
    public ResultVo addRole(@RequestBody Map<String,Object> data) {
        TSysRoleDto tSysRoleDto = JSONObject.parseObject(JsonUtil.toJson(data.get("tSysRoleDto")),TSysRoleDto.class);
        return sysRoleClient.addRole(tSysRoleDto);
    }

    /**
     * 修改角色信息
     * @param data 角色信息
     * @return ResultVo
     */
    @ApiOperation(value = "修改角色信息", httpMethod = "PUT")
    @RequestMapping(value = "/updateRole")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tSysRoleDto", value = "角色信息", required = true, paramType = "string"),
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string")
    })
    public ResultVo updateRole(@RequestBody Map<String,Object> data) {
        TSysRoleDto tSysRoleDto = JSONObject.parseObject(JsonUtil.toJson(data.get("tSysRoleDto")),TSysRoleDto.class);
        return sysRoleClient.updateRole(tSysRoleDto);
    }

    /**
     * 用户下角色列表
     * @param data 用户id
     * @return 角色信息列表
     */
    @ApiOperation(value = "查询用户信息列表", httpMethod = "POST")
    @RequestMapping(value = "/getUserRole")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "string"),
            @ApiImplicitParam(name = "token", value = "token参数", required = true, paramType = "string")
    })
    public ResultVo getUserRole(@RequestBody Map<String,Object> data) {
       return sysRoleClient.getUserRole(String.valueOf(data.get("userId")));
    }

}
