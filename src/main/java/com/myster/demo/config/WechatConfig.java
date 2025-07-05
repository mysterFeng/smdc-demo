package com.myster.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信小程序配置
 * 
 * @author myster
 * @since 2024-01-01
 */
@Data
@Component
@ConfigurationProperties(prefix = "wechat.miniprogram")
public class WechatConfig {
    
    /**
     * 小程序AppID
     */
    private String appid;
    
    /**
     * 小程序AppSecret
     */
    private String secret;
} 