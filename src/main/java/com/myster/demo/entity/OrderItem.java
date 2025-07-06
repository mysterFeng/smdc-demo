package com.myster.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单明细实体类
 */
@Entity
@Table(name = "order_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 订单ID
     */
    @Column(name = "order_id", nullable = false)
    private Long orderId;
    
    /**
     * 菜品ID
     */
    @Column(name = "dish_id", nullable = false)
    private Long dishId;
    
    /**
     * 菜品名称（快照，防止菜品信息变更影响历史订单）
     */
    @Column(name = "dish_name", nullable = false, length = 100)
    private String dishName;
    
    /**
     * 菜品图片URL（快照）
     */
    @Column(name = "dish_image_url", length = 200)
    private String dishImageUrl;
    
    /**
     * 单价
     */
    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;
    
    /**
     * 数量
     */
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    
    /**
     * 小计金额
     */
    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;
    
    /**
     * 备注
     */
    @Column(name = "remark", length = 200)
    private String remark;
    
    /**
     * 创建时间
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    /**
     * 版本号（乐观锁）
     */
    @Version
    @Column(name = "version")
    private Long version;
} 