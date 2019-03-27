package com.yihexinda.gatewayservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;

/**
 * @author Jack
 * @date 2018/10/13.
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("user").password("password")
                .roles("USER").and().withUser("admin").password("admin")
                .roles("ADMIN");
    }

    @Configuration
    @Order(1)
    public static class DiscoveryWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
                    http.antMatcher("/eureka/**")
                    .csrf().disable()
                    .authorizeRequests()
                    .anyRequest().hasRole("ADMIN");
        }

    }

    @Configuration
    @Order(2)
    public static class PlatformWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/platform-web/**")
                    .csrf().disable().sessionManagement().disable()
                    .authorizeRequests()
                    .antMatchers("/platform-web/**").permitAll();
            http.headers().disable();
        }

    }

    @Configuration
    @Order(3)
    public static class NodeWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
                http.antMatcher("/node-web/**")
                    .csrf().disable().sessionManagement().disable()
                    .authorizeRequests()
                    .antMatchers("/node-web/**").permitAll();
            http.headers().disable();
        }

    }

    @Configuration
    @Order(5)
    public static class OtherWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
                    http.antMatcher("/**")
                    .csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER).and()
                    .authorizeRequests()
                    .antMatchers("/**").permitAll();
        }

    }

}
