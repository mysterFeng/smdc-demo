package com.myster.demo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户登录DTO
 * 
 * @author myster
 * @since 2024-01-01
 */
@Data
public class UserLoginDTO {

    /**
     * 微信登录code
     */
    @NotBlank(message = "code不能为空")
    private String code;

    /**
     * 用户信息
     */
    private UserInfo userInfo;

    /**
     * 加密数据
     */
    private String encryptedData;

    /**
     * 初始向量
     */
    private String iv;

    /**
     * 用户信息内部类
     */
    @Data
    public static class UserInfo {
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
} 