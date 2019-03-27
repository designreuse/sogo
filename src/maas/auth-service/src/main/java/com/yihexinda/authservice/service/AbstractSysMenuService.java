package com.yihexinda.authservice.service;

import java.util.List;

import com.yihexinda.auth.dto.SysMenuDto;
import com.yihexinda.auth.dto.SysMenuQueryDto;
import com.yihexinda.auth.enums.SysMenuType;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Jack
 * @date 2018/10/24.
 */
@Slf4j
public abstract class AbstractSysMenuService {
//    @Autowired
//    private SysMenuMapper sysMenuMapper;

    protected List<SysMenuDto> findSysMenusOrAuthoritiesBySystemIdAndUserId(Integer systemId, Long userId, SysMenuType menuType) {
        try {
            SysMenuQueryDto queryDto = new SysMenuQueryDto();
            queryDto.setSystemId(systemId);
            queryDto.setUserId(userId);
            queryDto.setType(menuType);
//            List<SysMenuDto> menuReturns = sysMenuMapper.selectBySystemIdAndUserId(queryDto);
            return null;
        } catch (Exception e) {
            log.error("findSysMenusOrAuthoritiesBySystemIdAndUserId {}", e);
        }
        return null;
    }
}
