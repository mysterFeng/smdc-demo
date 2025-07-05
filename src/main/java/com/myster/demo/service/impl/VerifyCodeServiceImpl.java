package com.myster.demo.service.impl;

import com.myster.demo.service.VerifyCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 验证码服务实现类
 * 
 * @author myster
 * @since 2024-01-01
 */
@Service
@Slf4j
public class VerifyCodeServiceImpl implements VerifyCodeService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String VERIFY_CODE_PREFIX = "verify_code:";
    private static final int CODE_EXPIRE_MINUTES = 5; // 验证码5分钟有效
    private static final int CODE_LENGTH = 6; // 验证码长度

    @Override
    public boolean sendCode(String phone, String type) {
        try {
            // 生成验证码
            String code = generateCode();
            
            // 存储到Redis，设置过期时间
            String key = VERIFY_CODE_PREFIX + type + ":" + phone;
            redisTemplate.opsForValue().set(key, code, CODE_EXPIRE_MINUTES, TimeUnit.MINUTES);
            
            // 模拟发送短信（实际项目中需要集成短信服务商）
            log.info("发送验证码到手机号: {}, 验证码: {}, 类型: {}", phone, code, type);
            
            // TODO: 集成短信服务商API
            // 这里只是模拟，实际需要调用短信服务商的API
            
            return true;
        } catch (Exception e) {
            log.error("发送验证码失败: phone={}, type={}", phone, type, e);
            return false;
        }
    }

    @Override
    public boolean verifyCode(String phone, String code, String type) {
        try {
            String key = VERIFY_CODE_PREFIX + type + ":" + phone;
            String storedCode = redisTemplate.opsForValue().get(key);
            
            if (storedCode != null && storedCode.equals(code)) {
                // 验证成功后删除验证码
                redisTemplate.delete(key);
                return true;
            }
            
            return false;
        } catch (Exception e) {
            log.error("验证验证码失败: phone={}, code={}, type={}", phone, code, type, e);
            return false;
        }
    }

    @Override
    public String generateCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(random.nextInt(10));
        }
        
        return code.toString();
    }
} 