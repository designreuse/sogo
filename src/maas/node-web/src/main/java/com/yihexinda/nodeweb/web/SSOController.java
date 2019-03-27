package com.yihexinda.nodeweb.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.yihexinda.auth.domain.User;
import com.yihexinda.auth.dto.SysUserDto;
import com.yihexinda.data.dto.UserDto;
import com.yihexinda.data.enums.UserOnlineStatusEnum;
import com.yihexinda.nodeweb.client.DataUserClient;
import com.yihexinda.nodeweb.config.ConfigProperties;
import com.yihexinda.nodeweb.security.SecurityHelper;
import com.yihexinda.nodeweb.service.AuthService;
import com.yihexinda.nodeweb.service.UserService;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Jack
 * @date 2018/10/13.
 */
@Controller
@RequestMapping("")
@Slf4j
public class SSOController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;
    @Autowired
    private SecurityHelper securityHelper;
    @Autowired
    private ConfigProperties configProperties;
    @Autowired
    private DataUserClient userClient;

    @RequestMapping("/ssoLogin")
    public String ssoLogin(@RequestParam String token, HttpServletRequest request) {
        System.out.println("token--------------" + token);
        if (token.equals("anonymousUser")) {
            throw new RuntimeException("anonymousUser");
        }
        User user = userService.getUserInfoFromSSO(configProperties.getAsmsHost() + configProperties.getGetLoginUsernameUrl(), token);
        if (user == null) {
            return String.format("redirect:%s?redir=%s/ssoLogin&sessionName=%s", configProperties.getCheckLoginAndRedirUrl(), configProperties.getBasePath(), configProperties.getBasePath());
        } else {
            //todo 保存user信息权限到本地 //注意要对方返回登陆过期时间，以实现两边同时到期，注意告诉对方，手动退出时，顺便手动清除我们的cookie
            SysUserDto userDto = authService.findSysUserDtoByUsername(user.getUsername());
            userService.createOrUpdateUser(userDto);
            //手动登录
            securityHelper.login(user.getUsername(), request);
            //修改用户在线状态 如果是忙碌中退出怎么办？todo
            //userService.online(user.getId().intValue(), false);
            userService.updateAuthorities();
            return String.format("redirect:%s/", configProperties.getBasePath());
        }
    }

    @ApiOperation(value = "登出", httpMethod = "GET")
    @GetMapping("/ssoLogout")
    public String logout(HttpServletResponse response) {
        Long userId = securityHelper.getCurrentUser().getId();
        UserDto userDto = new UserDto();
        userDto.setUserId(userId.intValue());
        userDto.setOnlineStatus(UserOnlineStatusEnum.离线);
        userDto.setForce(false);
        userClient.updateUserOnlineStatus(userDto);
//        Cookie cookie = new Cookie("ASMS_SESSION", null); // Not necessary, but saves bandwidth.
//        cookie.setPath("/");
//        cookie.setHttpOnly(true);
//        cookie.setMaxAge(0); // Don't set to -1 or it will become a session cookie!
//        response.addCookie(cookie);
        SecurityContextHolder.clearContext();
        return "redirect:/";
    }
}
