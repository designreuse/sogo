package com.yihexinda.nodeweb.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.web.filter.GenericFilterBean;

import com.yihexinda.auth.constant.AuthoritiesConstants;
import com.yihexinda.nodeweb.service.UserService;

/**
 * @author Jack
 * @date 2018/10/13.
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private ConfigProperties configProperties;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Configuration
    public static class NodeWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
        @Autowired
        private UserService userService;
        @Autowired
        private ConfigProperties configProperties;

        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring().antMatchers("/assets/**",
                    "/v2/api-docs",
                    "/configuration/ui",
                    "/swagger-resources/**",
                    "/configuration/security",
                    "/swagger-ui.html",
                    "/webjars/**");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable().addFilterBefore(new StaticResourceFilter(), BasicAuthenticationFilter.class)
                    .antMatcher("/**")
                    .authorizeRequests()
                    .antMatchers("/test-api/**").permitAll()
                    .antMatchers("/test/**").permitAll()
                    .antMatchers("/ssoLogin").permitAll()
                    .antMatchers("/ssoLogout").permitAll()
                    .antMatchers("/logout").permitAll()
                    .anyRequest().hasAnyAuthority(AuthoritiesConstants.NODE_MANAGEMENT)
                    .and()
                    .formLogin()
                    .loginPage(String.format("%s?redir=%s/ssoLogin", configProperties.getCheckLoginAndRedirUrl(), configProperties.getBasePath()))//这个url是由其他服务提供
                    .permitAll().and().logout().logoutUrl("/logout").permitAll();
            http.headers().frameOptions().disable();
        }


    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
//
//
//
//    @Bean
//    public CookieSerializer cookieSerializer() {
//        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
//        serializer.setCookieName("YOUR_COOKIE");
//        serializer.setCookiePath("/");
//        serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$");
//        return serializer;
//    }

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName(configProperties.getSessionName());
        serializer.setCookiePath("/");
//        serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$");
        return serializer;
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationEventPublisher(authenticationEventPublisher())
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public DefaultAuthenticationEventPublisher authenticationEventPublisher() {
        return new DefaultAuthenticationEventPublisher();
    }

    public static class StaticResourceFilter extends GenericFilterBean {
        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            String xForwardedPrefix = ((HttpServletRequest) request).getHeader("x-forwarded-prefix");
            if (StringUtils.isEmpty(xForwardedPrefix)) {
                ((HttpServletRequest) request).getSession().setAttribute("basePath", "");
            } else {
                ((HttpServletRequest) request).getSession().setAttribute("basePath", xForwardedPrefix);
            }
            chain.doFilter(request, response);
        }
    }
}
