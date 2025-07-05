package com.myster.demo.service.impl;

import com.myster.demo.dto.UserLoginDTO;
import com.myster.demo.dto.UserPhoneLoginDTO;
import com.myster.demo.entity.User;
import com.myster.demo.repository.UserRepository;
import com.myster.demo.service.UserService;
import com.myster.demo.service.WechatService;
import com.myster.demo.util.JwtUtil;
import com.myster.demo.vo.UserVO;
import com.myster.demo.vo.WechatLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 用户服务实现类
 * 
 * @author myster
 * @since 2024-01-01
 */
@Service
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private WechatService wechatService;
    
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    @Transactional
    public UserVO login(UserLoginDTO loginDTO) {
        log.info("用户微信登录，code: {}", loginDTO.getCode());
        
        // 调用微信API获取openid和用户信息
        WechatLoginVO wechatLoginVO = wechatService.login(loginDTO.getCode());
        String openid = wechatLoginVO.getOpenid();
        
        // 查找或创建用户
        Optional<User> userOpt = userRepository.findByOpenid(openid);
        User user;
        
        if (userOpt.isPresent()) {
            user = userOpt.get();
            // 更新最后登录时间
            user.setLastLoginTime(LocalDateTime.now());
        } else {
            // 创建新用户
            user = new User();
            user.setOpenid(openid);
            user.setNickname(wechatLoginVO.getNickName());
            user.setAvatar(wechatLoginVO.getAvatarUrl());
            user.setGender(wechatLoginVO.getGender());
            user.setStatus(1);
            user.setLastLoginTime(LocalDateTime.now());
        }
        
        user = userRepository.save(user);
        UserVO userVO = convertToVO(user);
        
        // 生成JWT token
        String token = jwtUtil.generateToken(openid, user.getId());
        userVO.setToken(token);
        
        return userVO;
    }

    @Override
    @Transactional
    public UserVO phoneLogin(UserPhoneLoginDTO phoneLoginDTO) {
        log.info("用户手机号登录，phone: {}", phoneLoginDTO.getPhone());
        
        // 根据手机号查找用户
        Optional<User> userOpt = userRepository.findByPhone(phoneLoginDTO.getPhone());
        if (!userOpt.isPresent()) {
            throw new RuntimeException("用户不存在，请先注册");
        }
        
        User user = userOpt.get();
        
        // 验证密码
        String inputPassword = DigestUtils.md5DigestAsHex(phoneLoginDTO.getPassword().getBytes());
        if (!inputPassword.equals(user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        
        // 更新最后登录时间
        user.setLastLoginTime(LocalDateTime.now());
        user = userRepository.save(user);
        
        UserVO userVO = convertToVO(user);
        
        // 生成JWT token（使用手机号作为标识）
        String token = jwtUtil.generateToken(phoneLoginDTO.getPhone(), user.getId());
        userVO.setToken(token);
        
        return userVO;
    }

    @Override
    public UserVO getById(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        return userOpt.map(this::convertToVO).orElse(null);
    }

    @Override
    public UserVO getByOpenid(String openid) {
        Optional<User> userOpt = userRepository.findByOpenid(openid);
        return userOpt.map(this::convertToVO).orElse(null);
    }

    @Override
    @Transactional
    public UserVO updateUser(Long id, UserVO userVO) {
        Optional<User> userOpt = userRepository.findById(id);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("用户不存在");
        }
        
        User user = userOpt.get();
        user.setNickname(userVO.getNickname());
        user.setAvatar(userVO.getAvatar());
        user.setGender(userVO.getGender());
        
        user = userRepository.save(user);
        return convertToVO(user);
    }

    @Override
    @Transactional
    public UserVO bindPhone(Long userId, String phone) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("用户不存在");
        }
        
        // 检查手机号是否已被绑定
        if (userRepository.existsByPhone(phone)) {
            throw new RuntimeException("手机号已被绑定");
        }
        
        User user = userOpt.get();
        user.setPhone(phone);
        user = userRepository.save(user);
        
        return convertToVO(user);
    }

    /**
     * 转换为VO对象
     */
    private UserVO convertToVO(User user) {
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }
} 