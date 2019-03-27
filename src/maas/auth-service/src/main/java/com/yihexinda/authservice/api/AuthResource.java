package com.yihexinda.authservice.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yihexinda.auth.dto.AuthorityDto;
import com.yihexinda.auth.dto.SysMenuQueryDto;
import com.yihexinda.auth.dto.SysMenuSimpleDto;
import com.yihexinda.auth.dto.SysUserDto;
import com.yihexinda.auth.dto.SysUserQueryDto;
import com.yihexinda.authservice.service.AuthorityService;
import com.yihexinda.authservice.service.SysMenuService;
import com.yihexinda.authservice.service.SysUserService;

/**
 * @author Jack
 * @date 2018/10/19.
 */
@RestController
@RequestMapping("/auth")
public class AuthResource {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private AuthorityService authorityService;

    @PostMapping("/findUserByUsername")
    public SysUserDto findUserByUsername(@RequestBody SysUserQueryDto findSysUserDto) {
        return sysUserService.findSysUserByUsername(findSysUserDto.getUsername());
    }

//    @PostMapping("/findSysUserLikeUserName")
//    public Page<SysUserNameResponseDto> findSysUserLikeUserName(@RequestBody SysUserPageQueryDto pageQueryDto) {
//        return sysUserService.findSysUserLikeUserName(pageQueryDto);
//    }

    /**
     * 测试
     *
     * @param username
     * @return
     */
    @PostMapping("/findUserByUsernameForm")
    public SysUserDto findUserByUsernameForm(@RequestParam String username) {
        return sysUserService.findSysUserByUsername(username);
    }


    @PostMapping("/findMenusBySystemIdAndUserId")
    public List<SysMenuSimpleDto> findMenusBySystemIdAndUserId(@RequestBody SysMenuQueryDto findMenuDto) {
        return sysMenuService.findBySystemIdAndUserId(findMenuDto.getSystemId(), findMenuDto.getUserId());
    }

    @PostMapping("/findAuthoritiesBySystemIdAndUserId")
    public List<AuthorityDto> findAuthoritiesBySystemIdAndUserId(@RequestBody SysMenuQueryDto findMenuDto) {
        return authorityService.findBySystemIdAndUserId(findMenuDto.getSystemId(), findMenuDto.getUserId());
    }

//    @PostMapping("/findSysUserLikeName")
//    public List<SysUserNameResponseDto> findSysUserLikeName(@RequestBody SysUserPageQueryDto pageQueryDto) {
//        return sysUserService.findSysUserLikeName(pageQueryDto);
//    }
}
