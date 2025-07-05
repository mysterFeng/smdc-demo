package com.myster.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Spring Security配置类
 * 
 * @author myster
 * @since 2024-01-01
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
                // 允许登录相关接口
                .antMatchers("/v1/users/login", "/v1/users/phone-login", "/v1/users/test").permitAll()
                // 允许SpringDoc OpenAPI相关路径
                .antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/swagger-resources/**", "/webjars/**").permitAll()
                // 允许健康检查端点
                .antMatchers("/actuator/**").permitAll()
                // 允许静态资源
                .antMatchers("/static/**", "/public/**").permitAll()
                // 允许根路径
                .antMatchers("/").permitAll()
                // 其他请求需要认证
                .anyRequest().authenticated()
            .and()
            .formLogin().disable()
            .httpBasic().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
} 