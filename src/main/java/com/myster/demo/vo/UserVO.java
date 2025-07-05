package com.myster.demo.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户视图对象
 * 
 * @author myster
 * @since 2024-01-01
 */
@Data
public class UserVO {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 微信昵称
     */
    private String nickname;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 性别：0-未知，1-男，2-女
     */
    private Integer gender;

    /**
     * 用户状态：0-禁用，1-正常
     */
    private Integer status;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * JWT token
     */
    private String token;
} 