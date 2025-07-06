package com.myster.demo.repository;

import com.myster.demo.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 订单明细Repository
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    
    /**
     * 根据订单ID查询订单明细
     */
    List<OrderItem> findByOrderIdOrderByCreatedAtAsc(Long orderId);
    
    /**
     * 根据订单ID删除订单明细
     */
    void deleteByOrderId(Long orderId);
} 