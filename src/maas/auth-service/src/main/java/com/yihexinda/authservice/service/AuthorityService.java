package com.yihexinda.authservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.yihexinda.auth.dto.AuthorityDto;
import com.yihexinda.auth.enums.SysMenuType;

/**
 * @author Jack
 * @date 2018/10/24.
 */
@Service
public class AuthorityService extends AbstractSysMenuService {
    public List<AuthorityDto> findBySystemIdAndUserId(Integer systemId, Long userId) {
        systemId = null;
        return findSysMenusOrAuthoritiesBySystemIdAndUserId(systemId, userId, SysMenuType.按钮).stream().map(x -> AuthorityDto.getFromSysMenuDto(x)).collect(Collectors.toList());
    }
}
