package com.myster.demo.repository;

import com.myster.demo.entity.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 用户优惠券Repository
 * 
 * @author myster
 * @since 2025-07-06
 */
@Repository
public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {
    
    /**
     * 根据用户ID查询用户优惠券
     * 
     * @param userId 用户ID
     * @return 用户优惠券列表
     */
    List<UserCoupon> findByUserId(Long userId);
    
    /**
     * 根据用户ID和状态查询用户优惠券
     * 
     * @param userId 用户ID
     * @param status 状态
     * @return 用户优惠券列表
     */
    List<UserCoupon> findByUserIdAndStatus(Long userId, Integer status);
    
    /**
     * 根据用户ID和优惠券ID查询用户优惠券
     * 
     * @param userId 用户ID
     * @param couponId 优惠券ID
     * @return 用户优惠券
     */
    Optional<UserCoupon> findByUserIdAndCouponId(Long userId, Long couponId);
    
    /**
     * 查询用户可用的优惠券（未使用且未过期）
     * 
     * @param userId 用户ID
     * @param now 当前时间
     * @return 用户优惠券列表
     */
    @Query("SELECT uc FROM UserCoupon uc WHERE uc.userId = :userId AND uc.status = 0 AND uc.expiredAt > :now")
    List<UserCoupon> findAvailableUserCoupons(@Param("userId") Long userId, @Param("now") LocalDateTime now);
    
    /**
     * 根据订单ID查询用户优惠券
     * 
     * @param orderId 订单ID
     * @return 用户优惠券
     */
    Optional<UserCoupon> findByOrderId(Long orderId);
    
    /**
     * 统计用户已领取的优惠券数量
     * 
     * @param userId 用户ID
     * @param couponId 优惠券ID
     * @return 数量
     */
    long countByUserIdAndCouponId(Long userId, Long couponId);
} 