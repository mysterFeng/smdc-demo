package com.myster.demo.entity;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户优惠券实体类
 * 
 * @author myster
 * @since 2025-07-06
 */
@Entity
@Table(name = "user_coupons")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCoupon {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "coupon_id", nullable = false)
    private Long couponId;
    
    @Column(name = "status", nullable = false)
    private Integer status;
    
    @Column(name = "order_id")
    private Long orderId;
    
    @Column(name = "received_at", nullable = false)
    private LocalDateTime receivedAt;
    
    @Column(name = "used_at")
    private LocalDateTime usedAt;
    
    @Column(name = "expired_at", nullable = false)
    private LocalDateTime expiredAt;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // 关联优惠券模板信息
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", insertable = false, updatable = false)
    private Coupon coupon;
} 