package com.yihexinda.nodeweb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yihexinda.auth.dto.AuthorityDto;
import com.yihexinda.auth.dto.SysMenuQueryDto;
import com.yihexinda.auth.dto.SysMenuSimpleDto;
import com.yihexinda.auth.dto.SysUserDto;
import com.yihexinda.auth.dto.SysUserQueryDto;
import com.yihexinda.nodeweb.client.AuthClient;
import com.yihexinda.nodeweb.config.Constants;

/**
 * @author Jack
 * @date 2018/10/20.
 */
@Service
public class AuthService {
    @Autowired
    private AuthClient authClient;

    public List<SysMenuSimpleDto> findMenusByUserId(Long userId) {
        SysMenuQueryDto findMenuDto = new SysMenuQueryDto();
        findMenuDto.setUserId(userId);
        findMenuDto.setSystemId(Constants.SYSTEM_ID);
        return authClient.findMenusBySystemIdAndUserId(findMenuDto);
    }

    public SysUserDto findSysUserByUsername(String username){
        SysUserQueryDto findSysUserDto = new SysUserQueryDto();
        findSysUserDto.setUsername(username);
        return authClient.findUserByUsername(findSysUserDto);
    }
    public SysUserDto findSysUserDtoByUsername(String username) {
        SysUserQueryDto queryDto = new SysUserQueryDto();
        queryDto.setUsername(username);
        return authClient.findUserByUsername(queryDto);
    }

    public List<AuthorityDto> findAuthoritiesByUserId(Long userId) {
        SysMenuQueryDto findMenuDto = new SysMenuQueryDto();
        findMenuDto.setUserId(userId);
        findMenuDto.setSystemId(Constants.SYSTEM_ID);
        return authClient.findAuthoritiesBySystemIdAndUserId(findMenuDto);
    }
}
