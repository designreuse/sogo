package com.yihexinda.platformweb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yihexinda.auth.dto.AuthorityDto;
import com.yihexinda.auth.dto.SysMenuQueryDto;
import com.yihexinda.auth.dto.SysMenuSimpleDto;
import com.yihexinda.auth.dto.SysUserDto;
import com.yihexinda.auth.dto.SysUserNameResponseDto;
import com.yihexinda.auth.dto.SysUserPageQueryDto;
import com.yihexinda.auth.dto.SysUserQueryDto;
import com.yihexinda.core.dto.Page;
import com.yihexinda.platformweb.client.AuthClient;
import com.yihexinda.platformweb.client.DataUserClient;
import com.yihexinda.platformweb.config.Constants;

/**
 * @author Jack
 * @date 2018/10/20.
 */
@Service
public class AuthService {
    @Autowired
    private AuthClient authClient;
    @Autowired
    private DataUserClient userClient;

    public List<SysMenuSimpleDto> findMenusByUserId(Long userId) {
        SysMenuQueryDto findMenuDto = new SysMenuQueryDto();
        findMenuDto.setUserId(userId);
        findMenuDto.setSystemId(Constants.SYSTEM_ID);
        return authClient.findMenusBySystemIdAndUserId(findMenuDto);
    }

    public SysUserDto findSysUserByUsername(String username) {
        SysUserQueryDto findSysUserDto = new SysUserQueryDto();
        findSysUserDto.setUsername(username);
        SysUserDto userDto = authClient.findUserByUsername(findSysUserDto);
        //查询更新本地数据库
        userClient.createOrUpdate(userDto);
        return userDto;
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

    public Page<SysUserNameResponseDto> findSysUserLikeUserName(SysUserPageQueryDto pageQueryDto) {
        return authClient.findSysUserLikeUserName(pageQueryDto);
    }

    public List<SysUserNameResponseDto> findSysUserLikeName(SysUserPageQueryDto pageQueryDto) {
        return authClient.findSysUserLikeName(pageQueryDto);
    }
}
