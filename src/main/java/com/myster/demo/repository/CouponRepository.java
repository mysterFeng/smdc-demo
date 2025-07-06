package com.myster.demo.repository;

import com.myster.demo.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 优惠券Repository
 * 
 * @author myster
 * @since 2025-07-06
 */
@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    
    /**
     * 查询可用的优惠券列表
     * 
     * @param status 状态
     * @param now 当前时间
     * @return 优惠券列表
     */
    @Query("SELECT c FROM Coupon c WHERE c.status = :status AND c.startTime <= :now AND c.endTime >= :now")
    List<Coupon> findAvailableCoupons(@Param("status") Integer status, @Param("now") LocalDateTime now);
    
    /**
     * 根据状态查询优惠券
     * 
     * @param status 状态
     * @return 优惠券列表
     */
    List<Coupon> findByStatus(Integer status);
    
    /**
     * 根据类型查询优惠券
     * 
     * @param type 类型
     * @return 优惠券列表
     */
    List<Coupon> findByType(String type);
} 