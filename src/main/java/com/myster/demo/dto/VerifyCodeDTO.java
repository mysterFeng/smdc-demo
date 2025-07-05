package com.myster.demo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 验证码DTO
 * 
 * @author myster
 * @since 2024-01-01
 */
@Data
public class VerifyCodeDTO {

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    /**
     * 验证码类型：register-注册，login-登录，reset-重置密码
     */
    @NotBlank(message = "验证码类型不能为空")
    private String type = "register";
} 