package com.yihexinda.pcweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
@EnableConfigurationProperties
@ServletComponentScan
public class PcWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(PcWebApplication.class, args);
    }
}
