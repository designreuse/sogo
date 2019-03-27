package com.yihexinda.authservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.yihexinda.authservice.service.SysUserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataServiceApplicationTests {

    @Autowired
    private SysUserService userService;

    @Test
    public void contextLoads() {

    }

}
