package com.yihexinda.userweb.config;

import com.yihexinda.userweb.interceptor.SSOInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


/**
 * @author wenbn
 * @version 1.0
 * @date 2018/11/28 0028
 */
@Configuration
public class MyWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {


    public void addInterceptors(InterceptorRegistry registry) {
        //addPathPatterns 用于添加拦截规则
        //excludePathPatterns 用于排除拦截
        registry.addInterceptor(new SSOInterceptor()).addPathPatterns("/api/**")
                .excludePathPatterns("/buss/login") //登录
                .excludePathPatterns("/buss/logout"); //用户退出
        super.addInterceptors(registry);
    }

}
