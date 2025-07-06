package com.myster.demo.service;

import com.myster.demo.dto.OrderCreateDTO;
import com.myster.demo.vo.OrderVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 订单Service接口
 */
public interface OrderService {
    
    /**
     * 创建订单（从购物车）
     */
    OrderVO createOrder(OrderCreateDTO orderCreateDTO);
    
    /**
     * 根据订单ID查询订单详情
     */
    OrderVO getOrderById(Long orderId);
    
    /**
     * 根据订单号查询订单详情
     */
    OrderVO getOrderByOrderNo(String orderNo);
    
    /**
     * 分页查询用户订单列表
     */
    Page<OrderVO> getUserOrders(Long userId, Pageable pageable);
    
    /**
     * 根据状态分页查询用户订单列表
     */
    Page<OrderVO> getUserOrdersByStatus(Long userId, String status, Pageable pageable);
    
    /**
     * 取消订单
     */
    OrderVO cancelOrder(Long orderId, Long userId);
    
    /**
     * 支付订单（模拟支付）
     */
    OrderVO payOrder(Long orderId, Long userId, String paymentMethod);
    
    /**
     * 更新订单状态（商家端使用）
     */
    OrderVO updateOrderStatus(Long orderId, String status);
    
    /**
     * 分页查询所有订单（商家端使用）
     */
    Page<OrderVO> getAllOrders(Pageable pageable);
    
    /**
     * 根据状态分页查询订单（商家端使用）
     */
    Page<OrderVO> getOrdersByStatus(String status, Pageable pageable);
    
    /**
     * 统计用户指定状态的订单数量
     */
    long countByUserIdAndStatus(Long userId, String status);
} 