package com.yihexinda.platformweb.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.yihexinda.auth.dto.SysMenuSimpleDto;
import com.yihexinda.auth.dto.SysUserDto;
import com.yihexinda.core.dto.Json;
import com.yihexinda.platformweb.security.SecurityUtils;
import com.yihexinda.platformweb.service.AuthService;

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

    @ApiOperation(value = "菜单列表", httpMethod = "GET")
    @GetMapping("/menus")
    @ResponseBody
    public Json<List<SysMenuSimpleDto>> findMenusByUserId() {
        Json json = new Json();
        String loginUsername = SecurityUtils.getCurrentUserLogin();
        SysUserDto userDto = authService.findSysUserByUsername(loginUsername);
        json.setSuccess(true);
        json.setObj(authService.findMenusByUserId(userDto.getUserId()));
        return json;
    }

//    @ApiOperation(value = "根据登录账号或用户名来模糊查询对应的用户,并作分页处理", httpMethod = "POST")
//    @PostMapping("/findSysUserLikeUserName")
//    @ResponseBody
//    public Json<Page<SysUserNameResponseDto>> findSysUserLikeUserName(@RequestBody SysUserPageQueryDto pageQueryDto) {
//        Json json = new Json();
//        try {
//            json.setSuccess(true);
//            json.setObj(authService.findSysUserLikeUserName(pageQueryDto));
//        } catch (BussException e) {
//            json.setSuccess(false);
//            json.setMsg(e.getExceptionInfo());
//        }
//        return json;
//    }

//    @ApiOperation(value = "根据登录账号或用户名来模糊查询对应的用户", httpMethod = "POST")
//    @PostMapping("/findSysUserLikeName")
//    public Json<List<SysUserNameResponseDto>> findSysUserLikeName(@RequestBody SysUserPageQueryDto pageQueryDto) {
//        Json json = new Json();
//        try {
//            json.setSuccess(true);
//            json.setObj(authService.findSysUserLikeName(pageQueryDto));
//        } catch (BussException e) {
//            json.setSuccess(false);
//            json.setMsg(e.getExceptionInfo());
//        }
//        return json;
//    }

}
