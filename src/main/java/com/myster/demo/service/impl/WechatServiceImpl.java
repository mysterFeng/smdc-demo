package com.myster.demo.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myster.demo.config.WechatConfig;
import com.myster.demo.service.WechatService;
import com.myster.demo.vo.WechatLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信服务实现类
 * 
 * @author myster
 * @since 2024-01-01
 */
@Service
@Slf4j
public class WechatServiceImpl implements WechatService {

    @Autowired
    private WechatConfig wechatConfig;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public WechatLoginVO login(String code) {
        try {
            // 开发环境下使用模拟数据
            if (code.startsWith("test_")) {
                log.info("开发环境使用模拟数据，code: {}", code);
                WechatLoginVO loginVO = new WechatLoginVO();
                loginVO.setOpenid("mock_openid_" + System.currentTimeMillis());
                loginVO.setSessionKey("mock_session_key_" + System.currentTimeMillis());
                return loginVO;
            }
            
            // 调用微信API获取openid和session_key
            String url = "https://api.weixin.qq.com/sns/jscode2session";
            Map<String, String> params = new HashMap<>();
            params.put("appid", wechatConfig.getAppid());
            params.put("secret", wechatConfig.getSecret());
            params.put("js_code", code);
            params.put("grant_type", "authorization_code");
            
            String response = restTemplate.getForObject(
                url + "?appid={appid}&secret={secret}&js_code={js_code}&grant_type={grant_type}",
                String.class,
                params
            );
            
            log.info("微信登录响应: {}", response);
            
            JsonNode jsonNode = objectMapper.readTree(response);
            
            if (jsonNode.has("errcode") && jsonNode.get("errcode").asInt() != 0) {
                log.error("微信登录失败: {}", response);
                throw new RuntimeException("微信登录失败: " + jsonNode.get("errmsg").asText());
            }
            
            WechatLoginVO loginVO = new WechatLoginVO();
            loginVO.setOpenid(jsonNode.get("openid").asText());
            loginVO.setSessionKey(jsonNode.get("session_key").asText());
            
            return loginVO;
            
        } catch (Exception e) {
            log.error("微信登录异常", e);
            throw new RuntimeException("微信登录失败", e);
        }
    }

    @Override
    public WechatLoginVO getUserInfo(String code, String encryptedData, String iv) {
        // 先获取openid和session_key
        WechatLoginVO loginVO = login(code);
        
        try {
            // 解密用户信息
            // 这里需要实现AES解密逻辑
            // 由于解密逻辑比较复杂，这里先返回基本信息
            log.info("获取用户信息: code={}, encryptedData={}, iv={}", code, encryptedData, iv);
            
            // TODO: 实现AES解密逻辑
            // 暂时返回默认信息
            loginVO.setNickName("微信用户");
            loginVO.setAvatarUrl("/images/default-avatar.png");
            loginVO.setGender(0);
            
            return loginVO;
            
        } catch (Exception e) {
            log.error("获取用户信息异常", e);
            throw new RuntimeException("获取用户信息失败", e);
        }
    }
} 