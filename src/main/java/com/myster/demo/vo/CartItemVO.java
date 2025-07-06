package com.myster.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 购物车项目VO
 * 
 * @author myster
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemVO {

    /**
     * 购物车项目ID
     */
    private Long id;

    /**
     * 菜品ID
     */
    private Long dishId;

    /**
     * 菜品名称
     */
    private String dishName;

    /**
     * 菜品图片URL
     */
    private String dishImageUrl;

    /**
     * 菜品数量
     */
    private Integer quantity;

    /**
     * 菜品单价
     */
    private BigDecimal unitPrice;

    /**
     * 小计金额
     */
    private BigDecimal subtotal;

    /**
     * 是否选中
     */
    private Boolean selected;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
} 