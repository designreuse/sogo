package com.yihexinda.pcweb.client;

import com.yihexinda.core.vo.ResultVo;
import com.yihexinda.data.dto.TDriverDto;
import com.yihexinda.data.dto.TSysUserDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author pengfeng
 * @date 2018/11/29
 */
@FeignClient(value = "data-service")
@RequestMapping("/sysUser/client")
public interface SysUserClient {


    /**
     *  用户登录
     * @param paramter map参数
     * @return
     */
    @RequestMapping(value = "/loginUser",method = RequestMethod.POST)
    ResultVo loginUser(@RequestBody Map<String,String> paramter);

    /**
     * 退出登录
     * @param paramter
     * @return
     */
    @PostMapping(value = "/loginout")
    ResultVo loginout(Map<String, String> paramter);

    /**
     * pengFeng
     * 查询用户信息列表
     * @param  condition 参数map
     * @return 用户信息列表
     */
    @RequestMapping(method = RequestMethod.POST, value = "/getUserList")
    ResultVo getUserList(@RequestBody Map<String, Object> condition);
    /**
     * pengFeng
     * 根据id获取用户详情
     * @param id 用户id
     * @return  用户详情
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getUser/{id}")
    ResultVo getUser(@PathVariable("id") String id);

    /**
     * pengFeng
     *  添加用户信息
     * @param tSysUserDto 用户信息
     * @return ResultVo
     */
    @RequestMapping(method = RequestMethod.POST, value = "/addUser")
    ResultVo addUser(@RequestBody TSysUserDto tSysUserDto);

    /**
     *  修改用户信息
     * @param tSysUserDto 用户信息
     * @return ResultVo
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/updateUser")
    ResultVo updateUser(@RequestBody TSysUserDto tSysUserDto);

    /**
     * pengFeng
     * 查询角色下用户
     * @param  roleId  角色id
     * @return 用户信息列表
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getRoleUser/{roleId}")
    ResultVo getRoleUser(@PathVariable("roleId") String roleId);


}
