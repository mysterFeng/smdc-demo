package com.myster.demo.controller;

import com.myster.demo.dto.UserLoginDTO;
import com.myster.demo.dto.UserPhoneLoginDTO;
import com.myster.demo.dto.UserRegisterDTO;
import com.myster.demo.dto.VerifyCodeDTO;
import com.myster.demo.service.UserService;
import com.myster.demo.service.OrderService;
import com.myster.demo.service.CouponService;
import com.myster.demo.vo.Result;
import com.myster.demo.vo.UserVO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private CouponService couponService;

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

    @PostMapping("/register")
    public Result<UserVO> register(@Valid @RequestBody UserRegisterDTO registerDTO) {
        log.info("用户注册请求，phone: {}", registerDTO.getPhone());
        try {
            UserVO userVO = userService.register(registerDTO);
            return Result.success("注册成功", userVO);
        } catch (Exception e) {
            log.error("用户注册失败", e);
            return Result.error("注册失败: " + e.getMessage());
        }
    }

    @PostMapping("/send-verify-code")
    public Result<String> sendVerifyCode(@Valid @RequestBody VerifyCodeDTO verifyCodeDTO) {
        log.info("发送验证码请求，phone: {}, type: {}", verifyCodeDTO.getPhone(), verifyCodeDTO.getType());
        try {
            userService.sendVerifyCode(verifyCodeDTO);
            return Result.success("验证码发送成功");
        } catch (Exception e) {
            log.error("发送验证码失败", e);
            return Result.error("发送失败: " + e.getMessage());
        }
    }

    @GetMapping("/test")
    public Result<String> test() {
        return Result.success("Hello, 点餐小程序后端系统运行正常！");
    }
    
    /**
     * 获取用户订单统计
     */
    @GetMapping("/{userId}/order-stats")
    public Result<Map<String, Object>> getUserOrderStats(@PathVariable Long userId) {
        log.info("获取用户{}订单统计", userId);
        try {
            Map<String, Object> stats = new HashMap<>();
            
            // 统计各状态订单数量
            stats.put("pendingCount", orderService.countByUserIdAndStatus(userId, "PENDING_PAYMENT"));
            stats.put("deliveringCount", orderService.countByUserIdAndStatus(userId, "PAID") + 
                                       orderService.countByUserIdAndStatus(userId, "PREPARING") + 
                                       orderService.countByUserIdAndStatus(userId, "READY"));
            stats.put("reviewCount", orderService.countByUserIdAndStatus(userId, "COMPLETED"));
            stats.put("completedCount", orderService.countByUserIdAndStatus(userId, "COMPLETED"));
            
            return Result.success(stats);
        } catch (Exception e) {
            log.error("获取用户订单统计失败", e);
            return Result.error("获取统计失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取用户优惠券统计
     */
    @GetMapping("/{userId}/coupon-stats")
    public Result<Map<String, Object>> getUserCouponStats(@PathVariable Long userId) {
        log.info("获取用户{}优惠券统计", userId);
        try {
            Map<String, Object> stats = new HashMap<>();
            
            // 获取用户所有优惠券
            List<com.myster.demo.vo.UserCouponVO> allCoupons = couponService.getUserCoupons(userId, null);
            
            // 统计各状态优惠券数量
            long availableCount = allCoupons.stream()
                    .filter(coupon -> coupon.getStatus() == 0 && coupon.getIsAvailable())
                    .count();
            long usedCount = allCoupons.stream()
                    .filter(coupon -> coupon.getStatus() == 1)
                    .count();
            long expiredCount = allCoupons.stream()
                    .filter(coupon -> coupon.getStatus() == 2 || (coupon.getStatus() == 0 && !coupon.getIsAvailable()))
                    .count();
            
            stats.put("availableCount", availableCount);
            stats.put("usedCount", usedCount);
            stats.put("expiredCount", expiredCount);
            stats.put("totalCount", allCoupons.size());
            
            return Result.success(stats);
        } catch (Exception e) {
            log.error("获取用户优惠券统计失败", e);
            return Result.error("获取统计失败: " + e.getMessage());
        }
    }
} 