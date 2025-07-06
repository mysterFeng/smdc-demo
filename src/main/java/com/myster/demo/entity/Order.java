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
 * 订单实体类
 */
@Entity
@Table(name = "orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 订单号（唯一标识）
     */
    @Column(name = "order_no", unique = true, nullable = false, length = 32)
    private String orderNo;
    
    /**
     * 用户ID
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    /**
     * 订单状态：PENDING_PAYMENT(待支付)、PAID(已支付)、PREPARING(制作中)、READY(待取餐)、COMPLETED(已完成)、CANCELLED(已取消)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private OrderStatus status;
    
    /**
     * 订单总金额
     */
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;
    
    /**
     * 实付金额
     */
    @Column(name = "paid_amount", precision = 10, scale = 2)
    private BigDecimal paidAmount;
    
    /**
     * 收货人姓名
     */
    @Column(name = "receiver_name", length = 50)
    private String receiverName;
    
    /**
     * 收货人电话
     */
    @Column(name = "receiver_phone", length = 20)
    private String receiverPhone;
    
    /**
     * 收货地址
     */
    @Column(name = "receiver_address", length = 200)
    private String receiverAddress;
    
    /**
     * 备注
     */
    @Column(name = "remark", length = 500)
    private String remark;
    
    /**
     * 支付方式：WECHAT(微信支付)、ALIPAY(支付宝)、CASH(现金)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", length = 20)
    private PaymentMethod paymentMethod;
    
    /**
     * 支付时间
     */
    @Column(name = "paid_time")
    private LocalDateTime paidTime;
    
    /**
     * 预计取餐时间
     */
    @Column(name = "expected_pickup_time")
    private LocalDateTime expectedPickupTime;
    
    /**
     * 实际取餐时间
     */
    @Column(name = "actual_pickup_time")
    private LocalDateTime actualPickupTime;
    
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
    
    /**
     * 订单状态枚举
     */
    public enum OrderStatus {
        PENDING_PAYMENT("待支付"),
        PAID("已支付"),
        PREPARING("制作中"),
        READY("待取餐"),
        COMPLETED("已完成"),
        CANCELLED("已取消");
        
        private final String description;
        
        OrderStatus(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    /**
     * 支付方式枚举
     */
    public enum PaymentMethod {
        WECHAT("微信支付"),
        ALIPAY("支付宝"),
        CASH("现金");
        
        private final String description;
        
        PaymentMethod(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
} 