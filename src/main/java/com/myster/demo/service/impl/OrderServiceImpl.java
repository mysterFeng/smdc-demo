package com.myster.demo.service.impl;

import com.myster.demo.dto.OrderCreateDTO;
import com.myster.demo.entity.CartItem;
import com.myster.demo.entity.Order;
import com.myster.demo.entity.OrderItem;
import com.myster.demo.repository.CartItemRepository;
import com.myster.demo.repository.OrderItemRepository;
import com.myster.demo.repository.OrderRepository;
import com.myster.demo.service.OrderService;
import com.myster.demo.vo.OrderItemVO;
import com.myster.demo.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单Service实现类
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderItemRepository orderItemRepository;
    
    @Autowired
    private CartItemRepository cartItemRepository;
    
    @Override
    @Transactional
    public OrderVO createOrder(OrderCreateDTO orderCreateDTO) {
        log.info("创建订单，用户ID：{}", orderCreateDTO.getUserId());
        
        // 1. 查询用户购物车中的商品
        List<CartItem> cartItems = cartItemRepository.findByUserIdAndSelectedTrue(orderCreateDTO.getUserId());
        if (cartItems.isEmpty()) {
            throw new RuntimeException("购物车中没有选中的商品");
        }
        
        // 2. 生成订单号
        String orderNo = generateOrderNo();
        
        // 3. 计算订单总金额
        BigDecimal totalAmount = cartItems.stream()
                .map(item -> BigDecimal.valueOf(item.getUnitPrice()).multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // 4. 创建订单
        Order order = Order.builder()
                .orderNo(orderNo)
                .userId(orderCreateDTO.getUserId())
                .status(Order.OrderStatus.PENDING_PAYMENT)
                .totalAmount(totalAmount)
                .receiverName(orderCreateDTO.getReceiverName())
                .receiverPhone(orderCreateDTO.getReceiverPhone())
                .receiverAddress(orderCreateDTO.getReceiverAddress())
                .remark(orderCreateDTO.getRemark())
                .build();
        
        Order savedOrder = orderRepository.save(order);
        log.info("订单创建成功，订单号：{}", orderNo);
        
        // 5. 创建订单明细
        List<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> OrderItem.builder()
                        .orderId(savedOrder.getId())
                        .dishId(cartItem.getDishId())
                        .dishName(cartItem.getDishName())
                        .dishImageUrl(cartItem.getDishImageUrl())
                        .unitPrice(BigDecimal.valueOf(cartItem.getUnitPrice()))
                        .quantity(cartItem.getQuantity())
                        .subtotal(BigDecimal.valueOf(cartItem.getUnitPrice()).multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                        .remark(cartItem.getRemark())
                        .build())
                .collect(Collectors.toList());
        
        orderItemRepository.saveAll(orderItems);
        log.info("订单明细创建成功，共{}项", orderItems.size());
        
        // 6. 清空购物车中已选中的商品
        cartItemRepository.deleteSelectedByUserId(orderCreateDTO.getUserId());
        log.info("购物车已清空");
        
        // 7. 返回订单信息
        return getOrderById(savedOrder.getId());
    }
    
    @Override
    public OrderVO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        
        return convertToOrderVO(order);
    }
    
    @Override
    public OrderVO getOrderByOrderNo(String orderNo) {
        Order order = orderRepository.findByOrderNo(orderNo)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        
        return convertToOrderVO(order);
    }
    
    @Override
    public Page<OrderVO> getUserOrders(Long userId, Pageable pageable) {
        Page<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        return orders.map(this::convertToOrderVO);
    }
    
    @Override
    public Page<OrderVO> getUserOrdersByStatus(Long userId, String status, Pageable pageable) {
        Order.OrderStatus orderStatus = Order.OrderStatus.valueOf(status.toUpperCase());
        Page<Order> orders = orderRepository.findByUserIdAndStatusOrderByCreatedAtDesc(userId, orderStatus, pageable);
        return orders.map(this::convertToOrderVO);
    }
    
    @Override
    @Transactional
    public OrderVO cancelOrder(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        
        // 验证订单所属用户
        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此订单");
        }
        
        // 只有待支付的订单可以取消
        if (order.getStatus() != Order.OrderStatus.PENDING_PAYMENT) {
            throw new RuntimeException("订单状态不允许取消");
        }
        
        order.setStatus(Order.OrderStatus.CANCELLED);
        order = orderRepository.save(order);
        
        log.info("订单已取消，订单号：{}", order.getOrderNo());
        return convertToOrderVO(order);
    }
    
    @Override
    @Transactional
    public OrderVO payOrder(Long orderId, Long userId, String paymentMethod) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        
        // 验证订单所属用户
        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此订单");
        }
        
        // 只有待支付的订单可以支付
        if (order.getStatus() != Order.OrderStatus.PENDING_PAYMENT) {
            throw new RuntimeException("订单状态不允许支付");
        }
        
        // 更新订单状态
        order.setStatus(Order.OrderStatus.PAID);
        order.setPaidAmount(order.getTotalAmount());
        order.setPaymentMethod(Order.PaymentMethod.valueOf(paymentMethod.toUpperCase()));
        order.setPaidTime(LocalDateTime.now());
        order.setExpectedPickupTime(LocalDateTime.now().plusMinutes(30)); // 预计30分钟后取餐
        
        order = orderRepository.save(order);
        
        log.info("订单支付成功，订单号：{}，支付方式：{}", order.getOrderNo(), paymentMethod);
        return convertToOrderVO(order);
    }
    
    @Override
    @Transactional
    public OrderVO updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        
        Order.OrderStatus newStatus = Order.OrderStatus.valueOf(status.toUpperCase());
        order.setStatus(newStatus);
        
        // 如果状态变为待取餐，设置预计取餐时间
        if (newStatus == Order.OrderStatus.READY) {
            order.setExpectedPickupTime(LocalDateTime.now());
        }
        
        // 如果状态变为已完成，设置实际取餐时间
        if (newStatus == Order.OrderStatus.COMPLETED) {
            order.setActualPickupTime(LocalDateTime.now());
        }
        
        order = orderRepository.save(order);
        
        log.info("订单状态已更新，订单号：{}，新状态：{}", order.getOrderNo(), status);
        return convertToOrderVO(order);
    }
    
    @Override
    public Page<OrderVO> getAllOrders(Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(pageable);
        return orders.map(this::convertToOrderVO);
    }
    
    @Override
    public Page<OrderVO> getOrdersByStatus(String status, Pageable pageable) {
        Order.OrderStatus orderStatus = Order.OrderStatus.valueOf(status.toUpperCase());
        Page<Order> orders = orderRepository.findByStatusOrderByCreatedAtDesc(orderStatus, pageable);
        return orders.map(this::convertToOrderVO);
    }
    
    /**
     * 生成订单号
     */
    private String generateOrderNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = String.valueOf((int) ((Math.random() * 9 + 1) * 1000));
        return "ORDER" + timestamp + random;
    }
    
    /**
     * 转换为OrderVO
     */
    private OrderVO convertToOrderVO(Order order) {
        OrderVO orderVO = OrderVO.fromEntity(order);
        
        // 查询订单明细
        List<OrderItem> orderItems = orderItemRepository.findByOrderIdOrderByCreatedAtAsc(order.getId());
        List<OrderItemVO> orderItemVOs = orderItems.stream()
                .map(OrderItemVO::fromEntity)
                .collect(Collectors.toList());
        
        orderVO.setOrderItems(orderItemVOs);
        return orderVO;
    }
} 