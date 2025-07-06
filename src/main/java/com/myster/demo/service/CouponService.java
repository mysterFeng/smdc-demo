package com.myster.demo.service;

import com.myster.demo.dto.CouponDTO;
import com.myster.demo.vo.CouponVO;
import com.myster.demo.vo.UserCouponVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * 优惠券Service接口
 * 
 * @author myster
 * @since 2025-07-06
 */
public interface CouponService {
    
    /**
     * 查询所有可用优惠券
     * 
     * @return 优惠券列表
     */
    List<CouponVO> getAvailableCoupons();
    
    /**
     * 根据ID查询优惠券
     * 
     * @param id 优惠券ID
     * @return 优惠券信息
     */
    CouponVO getCouponById(Long id);
    
    /**
     * 创建优惠券
     * 
     * @param couponDTO 优惠券DTO
     * @return 优惠券信息
     */
    CouponVO createCoupon(CouponDTO couponDTO);
    
    /**
     * 更新优惠券
     * 
     * @param id 优惠券ID
     * @param couponDTO 优惠券DTO
     * @return 优惠券信息
     */
    CouponVO updateCoupon(Long id, CouponDTO couponDTO);
    
    /**
     * 删除优惠券
     * 
     * @param id 优惠券ID
     */
    void deleteCoupon(Long id);
    
    /**
     * 用户领取优惠券
     * 
     * @param userId 用户ID
     * @param couponId 优惠券ID
     * @return 用户优惠券信息
     */
    UserCouponVO receiveCoupon(Long userId, Long couponId);
    
    /**
     * 查询用户优惠券列表
     * 
     * @param userId 用户ID
     * @param status 状态（可选）
     * @return 用户优惠券列表
     */
    List<UserCouponVO> getUserCoupons(Long userId, Integer status);
    
    /**
     * 查询用户可用优惠券（根据订单金额筛选）
     * 
     * @param userId 用户ID
     * @param orderAmount 订单金额
     * @return 可用优惠券列表
     */
    List<UserCouponVO> getAvailableUserCoupons(Long userId, BigDecimal orderAmount);
    
    /**
     * 使用优惠券
     * 
     * @param userId 用户ID
     * @param userCouponId 用户优惠券ID
     * @param orderId 订单ID
     * @return 是否成功
     */
    boolean useCoupon(Long userId, Long userCouponId, Long orderId);
    
    /**
     * 验证优惠券是否可用
     * 
     * @param userId 用户ID
     * @param userCouponId 用户优惠券ID
     * @param orderAmount 订单金额
     * @return 是否可用
     */
    boolean validateCoupon(Long userId, Long userCouponId, BigDecimal orderAmount);
    
    /**
     * 临时方法：更新用户优惠券过期时间（测试用）
     * 
     * @param userId 用户ID
     */
    void updateUserCouponExpiryForTest(Long userId);
} 