package com.myster.demo.vo;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户优惠券VO
 * 
 * @author myster
 * @since 2025-07-06
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCouponVO {
    
    private Long id;
    private Long userId;
    private Long couponId;
    private Integer status;
    private Long orderId;
    private LocalDateTime receivedAt;
    private LocalDateTime usedAt;
    private LocalDateTime expiredAt;
    
    // 优惠券模板信息
    private String couponName;
    private String couponType;
    private BigDecimal couponValue;
    private BigDecimal couponMinAmount;
    private String couponDescription;
    
    // 状态描述
    private String statusDesc;
    
    // 是否可用
    private Boolean isAvailable;
} 