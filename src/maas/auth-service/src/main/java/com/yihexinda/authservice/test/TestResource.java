package com.yihexinda.authservice.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yihexinda.authservice.service.SysUserService;

/**
 * @author Jack
 * @date 2018/10/11.
 */
@RestController
@RequestMapping("/test")
public class TestResource {

    @Autowired
    private SysUserService sysUserService;


//    @GetMapping("/findUser")
//    public SysUserDto findUser() {
//        SysUserQueryDto sysUserDto = new SysUserQueryDto();
//        sysUserDto.setUserId(1);
//        return sysUserService.findSysUser(sysUserDto);
//    }
}
