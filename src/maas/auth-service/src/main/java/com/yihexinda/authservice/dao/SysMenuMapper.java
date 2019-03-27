package com.yihexinda.authservice.dao;

import java.util.List;

import com.yihexinda.auth.dto.SysMenuDto;
import com.yihexinda.auth.dto.SysMenuQueryDto;

public interface SysMenuMapper {
    List<SysMenuDto> selectBySystemIdAndUserId(SysMenuQueryDto findMenuDto);
}