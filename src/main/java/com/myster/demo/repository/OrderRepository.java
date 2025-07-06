package com.myster.demo.repository;

import com.myster.demo.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 订单Repository
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    /**
     * 根据订单号查询订单
     */
    Optional<Order> findByOrderNo(String orderNo);
    
    /**
     * 根据用户ID分页查询订单
     */
    Page<Order> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    /**
     * 根据用户ID和状态查询订单
     */
    List<Order> findByUserIdAndStatusOrderByCreatedAtDesc(Long userId, Order.OrderStatus status);
    
    /**
     * 根据状态查询订单
     */
    Page<Order> findByStatusOrderByCreatedAtDesc(Order.OrderStatus status, Pageable pageable);
    
    /**
     * 根据用户ID和状态分页查询订单
     */
    Page<Order> findByUserIdAndStatusOrderByCreatedAtDesc(Long userId, Order.OrderStatus status, Pageable pageable);
    
    /**
     * 查询指定时间范围内的订单
     */
    @Query("SELECT o FROM Order o WHERE o.createdAt BETWEEN :startTime AND :endTime ORDER BY o.createdAt DESC")
    List<Order> findByCreatedAtBetween(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
    
    /**
     * 统计用户订单数量
     */
    long countByUserId(Long userId);
    
    /**
     * 统计用户指定状态的订单数量
     */
    long countByUserIdAndStatus(Long userId, Order.OrderStatus status);
    
    /**
     * 查询待处理的订单（待支付、已支付、制作中）
     */
    @Query("SELECT o FROM Order o WHERE o.status IN ('PENDING_PAYMENT', 'PAID', 'PREPARING') ORDER BY o.createdAt ASC")
    List<Order> findPendingOrders();
} 