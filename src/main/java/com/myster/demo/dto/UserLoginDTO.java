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
    @NotBlank(message = "微信登录code不能为空")
    private String code;

    /**
     * 用户信息
     */
    private String userInfo;

    /**
     * 加密数据
     */
    private String encryptedData;

    /**
     * 初始向量
     */
    private String iv;
} 