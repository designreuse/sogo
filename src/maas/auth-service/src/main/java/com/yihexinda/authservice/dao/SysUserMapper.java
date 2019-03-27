package com.yihexinda.authservice.dao;

import com.yihexinda.auth.dto.SysUserDto;

public interface SysUserMapper {
    SysUserDto selectByUsername(String username);

//    List<SysUserNameResponseDto> findSysUserLikeUserName(@Param("name") String username, @Param("start") int start,
//                                                         @Param("end") int end);
//
//    Integer findUserLikeUserNameAmount(@Param("name") String username);
//
//    List<SysUserNameResponseDto> findSysUserLikeName(@Param("name") String name);
}