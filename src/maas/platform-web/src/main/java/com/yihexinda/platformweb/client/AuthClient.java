package com.yihexinda.platformweb.client;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yihexinda.auth.dto.AuthorityDto;
import com.yihexinda.auth.dto.SysMenuQueryDto;
import com.yihexinda.auth.dto.SysMenuSimpleDto;
import com.yihexinda.auth.dto.SysUserDto;
import com.yihexinda.auth.dto.SysUserNameResponseDto;
import com.yihexinda.auth.dto.SysUserPageQueryDto;
import com.yihexinda.auth.dto.SysUserQueryDto;
import com.yihexinda.core.dto.Page;
import com.yihexinda.platformweb.config.FeignClientConfig;

/**
 * @author Jack
 * @date 2018/10/20.
 */
@FeignClient(value = "auth-service", configuration = FeignClientConfig.class)
@RequestMapping("/auth")
public interface AuthClient {
    @RequestMapping(method = RequestMethod.POST, value = "/findMenusBySystemIdAndUserId")
    @ResponseBody
    List<SysMenuSimpleDto> findMenusBySystemIdAndUserId(SysMenuQueryDto findMenuDto);

    @RequestMapping(method = RequestMethod.POST, value = "/findAuthoritiesBySystemIdAndUserId")
    @ResponseBody
    List<AuthorityDto> findAuthoritiesBySystemIdAndUserId(SysMenuQueryDto findMenuDto);

    @RequestMapping(method = RequestMethod.POST, value = "/findUserByUsername")
    @ResponseBody
    SysUserDto findUserByUsername(SysUserQueryDto findSysUserDto);

    @RequestMapping(method = RequestMethod.POST, value = "/findUserByUsernameForm", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
//    @Headers("Content-Type: application/x-www-form-urlencoded")
    @ResponseBody
    SysUserDto findUserByUsernameForm(Map<String, ?> formParams);

    @RequestMapping(value = "/findSysUserLikeUserName",method = RequestMethod.POST)
    @ResponseBody
    Page<SysUserNameResponseDto> findSysUserLikeUserName(SysUserPageQueryDto pageQueryDto);

    @RequestMapping(value = "/findSysUserLikeName",method = RequestMethod.POST)
    @ResponseBody
    List<SysUserNameResponseDto> findSysUserLikeName(SysUserPageQueryDto pageQueryDto);
}
