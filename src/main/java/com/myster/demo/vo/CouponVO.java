package com.myster.demo.vo;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 优惠券VO
 * 
 * @author myster
 * @since 2025-07-06
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponVO {
    
    private Long id;
    private String name;
    private String type;
    private BigDecimal value;
    private BigDecimal minAmount;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer status;
    private LocalDateTime createdAt;
} 