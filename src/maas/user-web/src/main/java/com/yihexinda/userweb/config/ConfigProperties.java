package com.yihexinda.userweb.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Jack
 * @date 2018/10/27.
 */
@Configuration
@ConfigurationProperties(prefix = "userLocation")
@Data
public class ConfigProperties {

	/**
	 *  保存用户位置
	 */
	private String saveUserLocation;
}
