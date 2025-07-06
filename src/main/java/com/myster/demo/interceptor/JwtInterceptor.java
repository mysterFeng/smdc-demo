package com.myster.demo.interceptor;

import com.myster.demo.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT拦截器
 * 
 * @author myster
 * @since 2024-01-01
 */
@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求路径
        String requestURI = request.getRequestURI();
        
        // 不需要验证token的路径
        if (isExcludedPath(requestURI)) {
            return true;
        }
        
        // 获取Authorization头
        String authHeader = request.getHeader("Authorization");
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            log.warn("请求缺少Authorization头: {}", requestURI);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        
        // 提取token
        String token = authHeader.substring(7);
        
        try {
            // 验证token
            if (jwtUtil.isTokenExpired(token)) {
                log.warn("Token已过期: {}", requestURI);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
            
            // 将用户信息存入request
            String openid = jwtUtil.getOpenidFromToken(token);
            Long userId = jwtUtil.getUserIdFromToken(token);
            request.setAttribute("openid", openid);
            request.setAttribute("userId", userId);
            
            return true;
            
        } catch (Exception e) {
            log.error("Token验证失败", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }
    
    /**
     * 判断是否为排除路径（公开接口白名单）
     * 这些接口不需要JWT验证，可以直接访问
     */
    private boolean isExcludedPath(String requestURI) {
        // 用户相关公开接口
        if (requestURI.startsWith("/v1/users/login") ||
            requestURI.startsWith("/v1/users/phone-login") ||
            requestURI.startsWith("/v1/users/register") ||
            requestURI.startsWith("/v1/users/send-verify-code") ||
            requestURI.startsWith("/v1/users/test")) {
            return true;
        }
        
        // 菜品相关公开接口
        if (requestURI.startsWith("/v1/dishes")) {
            return true;
        }
        
        // 分类相关公开接口
        if (requestURI.startsWith("/v1/categories")) {
            return true;
        }
        
        // 购物车相关接口（暂时公开访问，后续可根据需要调整）
        if (requestURI.startsWith("/v1/cart")) {
            return true;
        }
        
        // 其他公开接口
        if (requestURI.startsWith("/error") ||
            requestURI.equals("/") ||
            requestURI.startsWith("/v3/api-docs") ||
            requestURI.startsWith("/swagger-ui") ||
            requestURI.startsWith("/swagger-resources") ||
            requestURI.startsWith("/webjars") ||
            requestURI.startsWith("/actuator") ||
            requestURI.startsWith("/static") ||
            requestURI.startsWith("/public")) {
            return true;
        }
        
        return false;
    }
} 