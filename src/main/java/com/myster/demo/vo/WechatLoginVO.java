package com.myster.demo.vo;

import lombok.Data;

/**
 * 微信登录返回数据
 * 
 * @author myster
 * @since 2024-01-01
 */
@Data
public class WechatLoginVO {
    
    /**
     * 用户唯一标识
     */
    private String openid;
    
    /**
     * 会话密钥
     */
    private String sessionKey;
    
    /**
     * 用户昵称
     */
    private String nickName;
    
    /**
     * 用户头像
     */
    private String avatarUrl;
    
    /**
     * 用户性别 0-未知 1-男 2-女
     */
    private Integer gender;
    
    /**
     * 用户所在城市
     */
    private String city;
    
    /**
     * 用户所在省份
     */
    private String province;
    
    /**
     * 用户所在国家
     */
    private String country;
    
    /**
     * 用户语言
     */
    private String language;
} 