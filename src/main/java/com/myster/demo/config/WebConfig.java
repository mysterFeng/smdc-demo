package com.myster.demo.config;

import com.myster.demo.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置
 * 
 * @author myster
 * @since 2024-01-01
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/v1/**")
                .excludePathPatterns(
                    // 用户相关公开接口
                    "/v1/users/login", 
                    "/v1/users/phone-login", 
                    "/v1/users/register", 
                    "/v1/users/send-verify-code", 
                    "/v1/users/test",
                    // 菜品相关公开接口
                    "/v1/dishes/**", 
                    "/v1/categories/**",
                    // 购物车相关接口（暂时公开访问）
                    "/v1/cart/**"
                );
    }
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
} 