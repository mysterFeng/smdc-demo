package com.myster.demo.controller;

import com.myster.demo.dto.CouponDTO;
import com.myster.demo.service.CouponService;
import com.myster.demo.vo.CouponVO;
import com.myster.demo.vo.UserCouponVO;
import com.myster.demo.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

/**
 * 优惠券Controller
 * 
 * @author myster
 * @since 2025-07-06
 */
@RestController
@RequestMapping("/v1/coupons")
@Slf4j
public class CouponController {
    
    @Autowired
    private CouponService couponService;
    
    /**
     * 查询所有可用优惠券
     */
    @GetMapping
    public Result<List<CouponVO>> getAvailableCoupons() {
        log.info("查询可用优惠券列表");
        List<CouponVO> coupons = couponService.getAvailableCoupons();
        return Result.success(coupons);
    }
    
    /**
     * 根据ID查询优惠券
     */
    @GetMapping("/{id}")
    public Result<CouponVO> getCouponById(@PathVariable Long id) {
        log.info("查询优惠券: {}", id);
        CouponVO coupon = couponService.getCouponById(id);
        if (coupon == null) {
            return Result.error("优惠券不存在");
        }
        return Result.success(coupon);
    }
    
    /**
     * 创建优惠券（商家后台）
     */
    @PostMapping
    public Result<CouponVO> createCoupon(@Valid @RequestBody CouponDTO couponDTO) {
        log.info("创建优惠券: {}", couponDTO.getName());
        try {
            CouponVO coupon = couponService.createCoupon(couponDTO);
            return Result.success(coupon);
        } catch (Exception e) {
            log.error("创建优惠券失败", e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 更新优惠券（商家后台）
     */
    @PutMapping("/{id}")
    public Result<CouponVO> updateCoupon(@PathVariable Long id, @Valid @RequestBody CouponDTO couponDTO) {
        log.info("更新优惠券: {}", id);
        try {
            CouponVO coupon = couponService.updateCoupon(id, couponDTO);
            return Result.success(coupon);
        } catch (Exception e) {
            log.error("更新优惠券失败", e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 删除优惠券（商家后台）
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteCoupon(@PathVariable Long id) {
        log.info("删除优惠券: {}", id);
        try {
            couponService.deleteCoupon(id);
            return Result.success();
        } catch (Exception e) {
            log.error("删除优惠券失败", e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 用户领取优惠券
     */
    @PostMapping("/receive")
    public Result<UserCouponVO> receiveCoupon(@RequestParam Long userId, @RequestParam Long couponId) {
        log.info("用户{}领取优惠券{}", userId, couponId);
        try {
            UserCouponVO userCoupon = couponService.receiveCoupon(userId, couponId);
            return Result.success(userCoupon);
        } catch (Exception e) {
            log.error("领取优惠券失败", e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 查询用户优惠券列表
     */
    @GetMapping("/user/{userId}")
    public Result<List<UserCouponVO>> getUserCoupons(@PathVariable Long userId, 
                                                    @RequestParam(required = false) Integer status) {
        log.info("查询用户{}优惠券列表，状态: {}", userId, status);
        List<UserCouponVO> userCoupons = couponService.getUserCoupons(userId, status);
        return Result.success(userCoupons);
    }
    
    /**
     * 查询用户可用优惠券（根据订单金额筛选）
     */
    @GetMapping("/user/{userId}/available")
    public Result<List<UserCouponVO>> getAvailableUserCoupons(@PathVariable Long userId, 
                                                             @RequestParam BigDecimal orderAmount) {
        log.info("查询用户{}可用优惠券，订单金额: {}", userId, orderAmount);
        List<UserCouponVO> userCoupons = couponService.getAvailableUserCoupons(userId, orderAmount);
        return Result.success(userCoupons);
    }
    
    /**
     * 使用优惠券
     */
    @PostMapping("/use")
    public Result<Boolean> useCoupon(@RequestParam Long userId, 
                                   @RequestParam Long userCouponId, 
                                   @RequestParam Long orderId) {
        log.info("使用优惠券: 用户={}, 用户优惠券ID={}, 订单ID={}", userId, userCouponId, orderId);
        try {
            boolean success = couponService.useCoupon(userId, userCouponId, orderId);
            return Result.success(success);
        } catch (Exception e) {
            log.error("使用优惠券失败", e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 验证优惠券是否可用
     */
    @PostMapping("/validate")
    public Result<Boolean> validateCoupon(@RequestParam Long userId, 
                                        @RequestParam Long userCouponId, 
                                        @RequestParam BigDecimal orderAmount) {
        log.info("验证优惠券: 用户={}, 用户优惠券ID={}, 订单金额={}", userId, userCouponId, orderAmount);
        boolean isValid = couponService.validateCoupon(userId, userCouponId, orderAmount);
        return Result.success(isValid);
    }
    
    /**
     * 临时接口：更新用户优惠券过期时间（测试用）
     */
    @PostMapping("/test/update-expiry")
    public Result<String> updateUserCouponExpiry(@RequestParam Long userId) {
        log.info("临时更新用户{}优惠券过期时间", userId);
        try {
            // 直接调用Service层方法更新过期时间
            couponService.updateUserCouponExpiryForTest(userId);
            return Result.success("更新成功");
        } catch (Exception e) {
            log.error("更新优惠券过期时间失败", e);
            return Result.error(e.getMessage());
        }
    }
} 