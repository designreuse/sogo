package com.yihexinda.platformweb.service;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.yihexinda.auth.domain.User;
import com.yihexinda.auth.dto.SysUserDto;
import com.yihexinda.core.dto.Json;
import com.yihexinda.core.dto.Page;
import com.yihexinda.data.dto.UserDto;
import com.yihexinda.data.dto.UserQueryDto;
import com.yihexinda.platformweb.client.DataUserClient;
import com.yihexinda.platformweb.security.SecurityHelper;
import com.yihexinda.platformweb.security.SecurityUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Jack
 * @date 2018/10/13.
 */
@Service
@Slf4j
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    @Lazy
    private AuthService authService;
    @Autowired
    private DataUserClient userClient;
    @Autowired
    private SecurityHelper securityHelper;

    public User findByUsername(String username) {
        SysUserDto sysUserDto = authService.findSysUserDtoByUsername(username);
        if (sysUserDto == null) return null;
        User user = new User();
        user.setId(sysUserDto.getUserId());
        user.setUsername(sysUserDto.getUsername());
        user.setPassword(sysUserDto.getPassword());
        return user;
    }

    public void createOrUpdateUser(SysUserDto userDto) {
        userClient.createOrUpdate(userDto);
    }

    /**
     * todo
     * 登录的时候同步权限
     */
    public void updateAuthorities() {
        Set<String> authorities = SecurityUtils.getAuthorities().stream().map(x -> x.getAuthority()).collect(Collectors.toSet());
        userClient.updateAuthorities(securityHelper.getCurrentUser().getId().intValue(), authorities);
    }

    public User getUserInfoFromSSO(String url, String token) {
        ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<String>() {
        };
        url = url + "?token=" + token;
        System.out.println(url);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(""), responseType);
        String username = response.getBody();
        if (StringUtils.isNotEmpty(username)) {
            log.info("Asms获取用户信息" + username);
            return new User(username);
        } else {
            throw new RuntimeException("Asms获取用户信息异常");
        }
    }

    public Json<List<UserDto>> findAvailableUsersToAssign(String orderId) {
        Json json = new Json();
        try {
            json.setSuccess(true);
            json.setObj(userClient.findAvailableUsersToAssign(orderId));
        } catch (Exception e) {
            json.setMsg("系统内部错误");
        }
        return json;
    }

    public Json<Page<UserDto>> findUserByUserQueryDto (UserQueryDto dto){
        Json json=new Json();
        try {
            json.setSuccess(true);
            json.setObj(userClient.findUserByUserQueryDto(dto));
        } catch (Exception e) {
            json.setMsg("系统内部错误");
        }
        return json;
    }

}
