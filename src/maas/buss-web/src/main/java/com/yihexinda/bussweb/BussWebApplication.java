package com.yihexinda.bussweb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * @author wenbn
 * @version 1.0
 * @date 2018/11/28 0028
 */
@SpringBootApplication( exclude = {DataSourceAutoConfiguration.class})
@EnableEurekaClient
@EnableFeignClients
//@EnableRedisHttpSession(redisFlushMode = RedisFlushMode.IMMEDIATE, maxInactiveIntervalInSeconds = 12 * 3600)
//@MapperScan("com.yihexinda.bussweb.dao")
@ServletComponentScan
public class BussWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(BussWebApplication.class, args);
    }
}
