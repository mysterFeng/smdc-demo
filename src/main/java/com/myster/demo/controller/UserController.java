package com.myster.demo.controller;

import com.myster.demo.dto.UserLoginDTO;
import com.myster.demo.dto.UserPhoneLoginDTO;
import com.myster.demo.service.UserService;
import com.myster.demo.vo.Result;
import com.myster.demo.vo.UserVO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 用户控制器
 * 
 * @author myster
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/v1/users")
@Slf4j
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<UserVO> login(@Valid @RequestBody UserLoginDTO loginDTO) {
        log.info("用户微信登录请求，code: {}", loginDTO.getCode());
        try {
            UserVO userVO = userService.login(loginDTO);
            return Result.success("登录成功", userVO);
        } catch (Exception e) {
            log.error("微信登录失败", e);
            return Result.error("登录失败: " + e.getMessage());
        }
    }

    @PostMapping("/phone-login")
    public Result<UserVO> phoneLogin(@Valid @RequestBody UserPhoneLoginDTO phoneLoginDTO) {
        log.info("用户手机号登录请求，phone: {}", phoneLoginDTO.getPhone());
        try {
            UserVO userVO = userService.phoneLogin(phoneLoginDTO);
            return Result.success("登录成功", userVO);
        } catch (Exception e) {
            log.error("手机号登录失败", e);
            return Result.error("登录失败: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Result<UserVO> getById(@PathVariable @NotNull Long id) {
        UserVO userVO = userService.getById(id);
        if (userVO == null) {
            return Result.notFound("用户不存在");
        }
        return Result.success(userVO);
    }

    @PutMapping("/{id}")
    public Result<UserVO> updateUser(@PathVariable @NotNull Long id, @Valid @RequestBody UserVO userVO) {
        UserVO updatedUser = userService.updateUser(id, userVO);
        return Result.success("更新成功", updatedUser);
    }

    @PostMapping("/{id}/bind-phone")
    public Result<UserVO> bindPhone(@PathVariable @NotNull Long id, @RequestParam @NotNull String phone) {
        UserVO userVO = userService.bindPhone(id, phone);
        return Result.success("绑定成功", userVO);
    }

    @GetMapping("/test")
    public Result<String> test() {
        return Result.success("Hello, 点餐小程序后端系统运行正常！");
    }
} 