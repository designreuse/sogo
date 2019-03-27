package com.yihexinda.nodeweb.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * @author Jack
 * @date 2018/10/27.
 */
@Configuration
@ConfigurationProperties(prefix = "node-web", ignoreUnknownFields = true)
@Data
public class ConfigProperties {
    private String basePath;
    private String checkLoginAndRedirUrl;
    private String sessionName;
    private String asmsHost;
    private String getLoginUsernameUrl;
}
