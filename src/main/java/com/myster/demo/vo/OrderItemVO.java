package com.myster.demo.vo;

import com.myster.demo.entity.OrderItem;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单明细VO
 */
@Data
public class OrderItemVO {
    
    /**
     * 订单明细ID
     */
    private Long id;
    
    /**
     * 订单ID
     */
    private Long orderId;
    
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
     * 单价
     */
    private BigDecimal unitPrice;
    
    /**
     * 数量
     */
    private Integer quantity;
    
    /**
     * 小计金额
     */
    private BigDecimal subtotal;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 从OrderItem实体转换为OrderItemVO
     */
    public static OrderItemVO fromEntity(OrderItem orderItem) {
        OrderItemVO vo = new OrderItemVO();
        vo.setId(orderItem.getId());
        vo.setOrderId(orderItem.getOrderId());
        vo.setDishId(orderItem.getDishId());
        vo.setDishName(orderItem.getDishName());
        vo.setDishImageUrl(orderItem.getDishImageUrl());
        vo.setUnitPrice(orderItem.getUnitPrice());
        vo.setQuantity(orderItem.getQuantity());
        vo.setSubtotal(orderItem.getSubtotal());
        vo.setRemark(orderItem.getRemark());
        vo.setCreatedAt(orderItem.getCreatedAt());
        return vo;
    }
} 