package com.yihexinda.nodeweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.session.data.redis.RedisFlushMode;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

<<<<<<< .mine
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
||||||| .r17105
@SpringBootApplication
=======
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class
        // ,DruidDataSourceAutoConfigure.class,
        // QuartzAutoConfiguration.class
})
>>>>>>> .r17191
@EnableEurekaClient
@EnableFeignClients
@EnableRedisHttpSession(redisFlushMode = RedisFlushMode.IMMEDIATE, maxInactiveIntervalInSeconds = 12 * 3600)
public class NodeWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(NodeWebApplication.class, args);
    }

}
