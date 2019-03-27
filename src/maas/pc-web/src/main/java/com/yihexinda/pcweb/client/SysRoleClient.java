package com.yihexinda.pcweb.client;

import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TSysRoleDto;
import com.yihexinda.data.dto.TSysUserDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * @author pengfeng
 * @date 2018/11/29
 */
@FeignClient(value = "data-service")
@RequestMapping("/sysRole/client")
public interface SysRoleClient {

    /**
     * pengFeng
     * 查询用户信息列表
     * @param  condition 参数map
     * @return 用户信息列表
     */
    @RequestMapping(method = RequestMethod.POST, value = "/getRoleList")
    ResultVo getRoleList(@RequestBody Map<String, Object> condition);
    /**
     * pengFeng
     * 根据id获取用户详情
     * @param id 用户id
     * @return  用户详情
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getRole/{id}")
    ResultVo getRole(@PathVariable("id") String id);

    /**
     * pengFeng
     *  添加用户信息
     * @param tSysRoleDto 用户信息
     * @return ResultVo
     */
    @RequestMapping(method = RequestMethod.POST, value = "/addRole")
    ResultVo addRole(@RequestBody TSysRoleDto tSysRoleDto);

    /**
     *  修改用户信息
     * @param tSysRoleDto 用户信息
     * @return ResultVo
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/updateRole")
    ResultVo updateRole(@RequestBody TSysRoleDto tSysRoleDto);

    /**
     * pengFeng
     * 查询角色下用户
     * @param  userId  用户id
     * @return 角色信息列表
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getUserRole/{userId}")
    ResultVo getUserRole(@PathVariable("userId") String userId);

}
