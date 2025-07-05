package com.myster.demo.service.impl;

import com.myster.demo.dto.UserLoginDTO;
import com.myster.demo.entity.User;
import com.myster.demo.repository.UserRepository;
import com.myster.demo.service.UserService;
import com.myster.demo.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional
    public UserVO login(UserLoginDTO loginDTO) {
        log.info("用户登录，code: {}", loginDTO.getCode());
        
        // TODO: 调用微信API获取openid
        String openid = getOpenidFromWechat(loginDTO.getCode());
        
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
            user.setStatus(1);
            user.setLastLoginTime(LocalDateTime.now());
            
            // TODO: 解析用户信息
            if (loginDTO.getUserInfo() != null) {
                // 解析用户信息并设置昵称、头像等
            }
        }
        
        user = userRepository.save(user);
        return convertToVO(user);
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
     * 从微信获取OpenID
     */
    private String getOpenidFromWechat(String code) {
        // TODO: 实现微信登录逻辑
        // 这里先返回一个模拟的openid
        return "mock_openid_" + System.currentTimeMillis();
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