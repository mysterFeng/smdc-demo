package com.myster.demo.service;

/**
 * 验证码服务接口
 * 
 * @author myster
 * @since 2024-01-01
 */
public interface VerifyCodeService {

    /**
     * 发送验证码
     * 
     * @param phone 手机号
     * @param type 验证码类型
     * @return 是否发送成功
     */
    boolean sendCode(String phone, String type);

    /**
     * 验证验证码
     * 
     * @param phone 手机号
     * @param code 验证码
     * @param type 验证码类型
     * @return 是否验证成功
     */
    boolean verifyCode(String phone, String code, String type);

    /**
     * 生成验证码
     * 
     * @return 6位数字验证码
     */
    String generateCode();
} 