package com.myster.demo.service;

import com.myster.demo.dto.UserLoginDTO;
import com.myster.demo.dto.UserPhoneLoginDTO;
import com.myster.demo.dto.UserRegisterDTO;
import com.myster.demo.dto.VerifyCodeDTO;
import com.myster.demo.vo.UserVO;

/**
 * 用户服务接口
 * 
 * @author myster
 * @since 2024-01-01
 */
public interface UserService {

    /**
     * 微信登录
     */
    UserVO login(UserLoginDTO loginDTO);

    /**
     * 手机号密码登录
     */
    UserVO phoneLogin(UserPhoneLoginDTO phoneLoginDTO);

    /**
     * 根据ID获取用户信息
     */
    UserVO getById(Long id);

    /**
     * 根据OpenID获取用户信息
     */
    UserVO getByOpenid(String openid);

    /**
     * 更新用户信息
     */
    UserVO updateUser(Long id, UserVO userVO);

    /**
     * 绑定手机号
     */
    UserVO bindPhone(Long userId, String phone);

    /**
     * 用户注册
     */
    UserVO register(UserRegisterDTO registerDTO);

    /**
     * 发送验证码
     */
    void sendVerifyCode(VerifyCodeDTO verifyCodeDTO);

    /**
     * 验证验证码
     */
    boolean verifyCode(String phone, String code, String type);
} 