package com.myster.demo.service;

import com.myster.demo.vo.WechatLoginVO;

/**
 * 微信服务接口
 * 
 * @author myster
 * @since 2024-01-01
 */
public interface WechatService {
    
    /**
     * 微信登录
     */
    WechatLoginVO login(String code);
    
    /**
     * 获取用户信息
     */
    WechatLoginVO getUserInfo(String code, String encryptedData, String iv);
} 