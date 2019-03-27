package com.yihexinda.redis;

/**
 * @author wenbn
 * @version 1.0
 * @date 2018/12/3 0003
 */

import com.yihexinda.userweb.UserWebApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author wenbn
 * @version 1.0
 * @date 2018/11/20
 */
@RunWith(SpringJUnit4ClassRunner.class)
    @SpringBootTest(classes = UserWebApplication.class,webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class RedisTest {

    @Resource
    private RedisTemplate<String,String> redisTemplate;

    /**
     * 添加
     */
    @Test
    public void testRedisSet(){
        this.redisTemplate.opsForValue().set("test","adsferrrr");
    }

    /**
     * 获取
     */
    @Test
    public void testRedisGet(){
        Object test = this.redisTemplate.opsForValue().get("test");
        System.out.println(test);
    }

    /**
     * 删除
     */
    @Test
    public void testRedisDelete(){
        this.redisTemplate.delete("test");
    }

}
