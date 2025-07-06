package com.myster.demo.vo;

import com.myster.demo.entity.Order;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单VO
 */
@Data
public class OrderVO {
    
    /**
     * 订单ID
     */
    private Long id;
    
    /**
     * 订单号
     */
    private String orderNo;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 订单状态
     */
    private String status;
    
    /**
     * 订单状态描述
     */
    private String statusDescription;
    
    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;
    
    /**
     * 实付金额
     */
    private BigDecimal paidAmount;
    
    /**
     * 收货人姓名
     */
    private String receiverName;
    
    /**
     * 收货人电话
     */
    private String receiverPhone;
    
    /**
     * 收货地址
     */
    private String receiverAddress;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 支付方式
     */
    private String paymentMethod;
    
    /**
     * 支付方式描述
     */
    private String paymentMethodDescription;
    
    /**
     * 支付时间
     */
    private LocalDateTime paidTime;
    
    /**
     * 预计取餐时间
     */
    private LocalDateTime expectedPickupTime;
    
    /**
     * 实际取餐时间
     */
    private LocalDateTime actualPickupTime;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 订单明细列表
     */
    private List<OrderItemVO> orderItems;
    
    /**
     * 从Order实体转换为OrderVO
     */
    public static OrderVO fromEntity(Order order) {
        OrderVO vo = new OrderVO();
        vo.setId(order.getId());
        vo.setOrderNo(order.getOrderNo());
        vo.setUserId(order.getUserId());
        vo.setStatus(order.getStatus().name());
        vo.setStatusDescription(order.getStatus().getDescription());
        vo.setTotalAmount(order.getTotalAmount());
        vo.setPaidAmount(order.getPaidAmount());
        vo.setReceiverName(order.getReceiverName());
        vo.setReceiverPhone(order.getReceiverPhone());
        vo.setReceiverAddress(order.getReceiverAddress());
        vo.setRemark(order.getRemark());
        if (order.getPaymentMethod() != null) {
            vo.setPaymentMethod(order.getPaymentMethod().name());
            vo.setPaymentMethodDescription(order.getPaymentMethod().getDescription());
        }
        vo.setPaidTime(order.getPaidTime());
        vo.setExpectedPickupTime(order.getExpectedPickupTime());
        vo.setActualPickupTime(order.getActualPickupTime());
        vo.setCreatedAt(order.getCreatedAt());
        vo.setUpdatedAt(order.getUpdatedAt());
        return vo;
    }
} 