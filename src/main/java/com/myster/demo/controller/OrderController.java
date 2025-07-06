package com.myster.demo.controller;

import com.myster.demo.dto.OrderCreateDTO;
import com.myster.demo.service.OrderService;
import com.myster.demo.vo.OrderVO;
import com.myster.demo.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 订单Controller
 */
@RestController
@RequestMapping("/v1/orders")
@Slf4j
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    /**
     * 创建订单
     */
    @PostMapping
    public Result<OrderVO> createOrder(@Valid @RequestBody OrderCreateDTO orderCreateDTO) {
        log.info("创建订单请求，用户ID：{}", orderCreateDTO.getUserId());
        try {
            OrderVO orderVO = orderService.createOrder(orderCreateDTO);
            return Result.success(orderVO);
        } catch (Exception e) {
            log.error("创建订单失败", e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 根据订单ID查询订单详情
     */
    @GetMapping("/{orderId}")
    public Result<OrderVO> getOrderById(@PathVariable Long orderId) {
        log.info("查询订单详情，订单ID：{}", orderId);
        try {
            OrderVO orderVO = orderService.getOrderById(orderId);
            return Result.success(orderVO);
        } catch (Exception e) {
            log.error("查询订单详情失败", e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 根据订单号查询订单详情
     */
    @GetMapping("/no/{orderNo}")
    public Result<OrderVO> getOrderByOrderNo(@PathVariable String orderNo) {
        log.info("根据订单号查询订单详情，订单号：{}", orderNo);
        try {
            OrderVO orderVO = orderService.getOrderByOrderNo(orderNo);
            return Result.success(orderVO);
        } catch (Exception e) {
            log.error("根据订单号查询订单详情失败", e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 分页查询用户订单列表
     */
    @GetMapping("/user/{userId}")
    public Result<Page<OrderVO>> getUserOrders(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("查询用户订单列表，用户ID：{}，页码：{}，大小：{}", userId, page, size);
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<OrderVO> orders = orderService.getUserOrders(userId, pageable);
            return Result.success(orders);
        } catch (Exception e) {
            log.error("查询用户订单列表失败", e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 根据状态分页查询用户订单列表
     */
    @GetMapping("/user/{userId}/status/{status}")
    public Result<Page<OrderVO>> getUserOrdersByStatus(
            @PathVariable Long userId,
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("根据状态查询用户订单列表，用户ID：{}，状态：{}，页码：{}，大小：{}", userId, status, page, size);
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<OrderVO> orders = orderService.getUserOrdersByStatus(userId, status, pageable);
            return Result.success(orders);
        } catch (Exception e) {
            log.error("根据状态查询用户订单列表失败", e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 取消订单
     */
    @PostMapping("/{orderId}/cancel")
    public Result<OrderVO> cancelOrder(@PathVariable Long orderId, @RequestParam Long userId) {
        log.info("取消订单，订单ID：{}，用户ID：{}", orderId, userId);
        try {
            OrderVO orderVO = orderService.cancelOrder(orderId, userId);
            return Result.success(orderVO);
        } catch (Exception e) {
            log.error("取消订单失败", e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 支付订单
     */
    @PostMapping("/{orderId}/pay")
    public Result<OrderVO> payOrder(
            @PathVariable Long orderId,
            @RequestParam Long userId,
            @RequestParam String paymentMethod) {
        log.info("支付订单，订单ID：{}，用户ID：{}，支付方式：{}", orderId, userId, paymentMethod);
        try {
            OrderVO orderVO = orderService.payOrder(orderId, userId, paymentMethod);
            return Result.success(orderVO);
        } catch (Exception e) {
            log.error("支付订单失败", e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 更新订单状态（商家端使用）
     */
    @PostMapping("/{orderId}/status")
    public Result<OrderVO> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam String status) {
        log.info("更新订单状态，订单ID：{}，新状态：{}", orderId, status);
        try {
            OrderVO orderVO = orderService.updateOrderStatus(orderId, status);
            return Result.success(orderVO);
        } catch (Exception e) {
            log.error("更新订单状态失败", e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 分页查询所有订单（商家端使用）
     */
    @GetMapping("/admin/all")
    public Result<Page<OrderVO>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("查询所有订单，页码：{}，大小：{}", page, size);
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<OrderVO> orders = orderService.getAllOrders(pageable);
            return Result.success(orders);
        } catch (Exception e) {
            log.error("查询所有订单失败", e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 根据状态分页查询订单（商家端使用）
     */
    @GetMapping("/admin/status/{status}")
    public Result<Page<OrderVO>> getOrdersByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("根据状态查询订单，状态：{}，页码：{}，大小：{}", status, page, size);
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<OrderVO> orders = orderService.getOrdersByStatus(status, pageable);
            return Result.success(orders);
        } catch (Exception e) {
            log.error("根据状态查询订单失败", e);
            return Result.error(e.getMessage());
        }
    }
} 