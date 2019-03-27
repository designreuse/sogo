package com.yihexinda.nodeweb.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.yihexinda.auth.dto.SysMenuSimpleDto;
import com.yihexinda.auth.dto.SysUserDto;
import com.yihexinda.auth.dto.SysUserQueryDto;
import com.yihexinda.core.dto.Json;
import com.yihexinda.nodeweb.client.AuthClient;
import com.yihexinda.nodeweb.security.SecurityUtils;
import com.yihexinda.nodeweb.service.AuthService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author Jack
 * @date 2018/10/20.
 */
@Api(description = "Auth菜单接口")
@RestController
@RequestMapping("/api/auth")
public class AuthResource {
    @Autowired
    private AuthService authService;
    @Autowired
    private AuthClient authClient;
//    @Autowired
//    private DataUserClient UserClient;
//    @Autowired
//    private DataCompClient dataCompClient;
//    @Autowired
//    private DataDeptClient deptClient;


    @ApiOperation(value = "菜单列表", httpMethod = "GET")
    @GetMapping("/menus")
    @ResponseBody
    public Json<List<SysMenuSimpleDto>> findMenusByUserId() {
        Json json = new Json();
        String loginUsername = SecurityUtils.getCurrentUserLogin();
//        String loginUsername = "platform-user";
        SysUserQueryDto findSysUserDto = new SysUserQueryDto();
        findSysUserDto.setUsername(loginUsername);
        SysUserDto userDto = authClient.findUserByUsername(findSysUserDto);
        json.setSuccess(true);
        json.setObj(authService.findMenusByUserId(userDto.getUserId()));
        return json;
    }

//    @ApiOperation(value = "根据登录账号查询用户", httpMethod = "POST")
//    @PostMapping("/findUserUpdate")
//    @ResponseBody
//    public SysUserDto findUserUpdate(@RequestBody SysUserQueryDto findSysUserDto) {
//        //根据登录账号查询一体化平台用户
//        SysUserDto userByUsername = authClient.findUserByUsername(findSysUserDto);
//        if (userByUsername != null) {
//            //封装成操作调度平台数据库请求参数
//            UserDto findUserUpdateDto = new UserDto();
//            BeanUtils.copyProperties(userByUsername, findUserUpdateDto);
//            findUserUpdateDto.setCompCity(userByUsername.getCompCity());
//            findUserUpdateDto.setCreatedBy(userByUsername.getCreateUser());
//            findUserUpdateDto.setCreatedAt(DateUtils.formatDate(userByUsername.getCreateTime(),DateUtils.PATTERN_yyyy_MM_dd_HH_mm_ss));
//            findUserUpdateDto.setUpdatedBy(userByUsername.getUpdateUser());
//            findUserUpdateDto.setUpdatedAt(DateUtils.formatDate(userByUsername.getUpdateTime(),DateUtils.PATTERN_yyyy_MM_dd_HH_mm_ss));
//            //请求调度数据库
//            UserClient.findUserUpdate(findUserUpdateDto);
//
//            //封装该用户内对应的公司信息
//            CompDto compDto = new CompDto();
//            BeanUtils.copyProperties(userByUsername, compDto);
//            if (compDto != null) {
//                dataCompClient.findUpdateComp(compDto);
//            }
//
//            //封装该用户内的部门信息,并操作
//            DeptDto deptDto = new DeptDto();
//            BeanUtils.copyProperties(userByUsername, deptDto);
//            if (deptDto != null) {
//                deptClient.findUpdateDept(deptDto);
//            }
//        }
//        return userByUsername;
//    }
}
