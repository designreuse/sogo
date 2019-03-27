package com.yihexinda.authservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yihexinda.auth.dto.SysMenuSimpleDto;

/**
 * 类说明：实现类
 *
 *
 * <p>
 * 详细描述：.
 *
 * @author luoshuming
 * <p>
 * <p>
 * CreateDate: 2018-10-14
 */
@Service
public class SysMenuService extends AbstractSysMenuService {

    public List<SysMenuSimpleDto> findBySystemIdAndUserId(Integer systemId, Long userId) {
        return null;
    }
}
