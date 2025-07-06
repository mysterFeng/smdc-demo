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
     * 判断是否为排除路径
     */
    private boolean isExcludedPath(String requestURI) {
        return requestURI.startsWith("/v1/users/login") ||
               requestURI.startsWith("/v1/users/phone-login") ||
               requestURI.startsWith("/v1/users/register") ||
               requestURI.startsWith("/v1/users/send-verify-code") ||
               requestURI.startsWith("/v1/users/test") ||
               requestURI.startsWith("/v1/dishes") ||
               requestURI.startsWith("/v1/categories") ||
               requestURI.startsWith("/error") ||
               requestURI.equals("/");
    }
} 