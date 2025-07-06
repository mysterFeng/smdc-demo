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
                // 公开接口
                .antMatchers("/api/v1/coupons").permitAll()  // 查询可用优惠券
                .antMatchers("/api/v1/coupons/{id}").permitAll()  // 查询优惠券详情
                .antMatchers("/api/v1/coupons/user/**").permitAll()  // 用户优惠券相关
                .antMatchers("/api/v1/coupons/receive").permitAll()  // 领取优惠券
                .antMatchers("/api/v1/coupons/use").permitAll()  // 使用优惠券
                .antMatchers("/api/v1/coupons/validate").permitAll()  // 验证优惠券
                .antMatchers("/api/v1/addresses/**").permitAll()  // 地址管理相关
                .antMatchers("/api/v1/cart/**").permitAll()  // 购物车相关
                .antMatchers("/api/v1/orders/**").permitAll()  // 订单相关
                .antMatchers("/api/v1/dishes/**").permitAll()  // 菜品相关
                .antMatchers("/api/v1/categories/**").permitAll()  // 分类相关
                // 暂时允许所有请求（测试用）
                .anyRequest().permitAll()
            .and()
            .formLogin().disable()
            .httpBasic().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
} 