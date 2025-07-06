package com.myster.demo.service.impl;

import com.myster.demo.dto.CouponDTO;
import com.myster.demo.entity.Coupon;
import com.myster.demo.entity.UserCoupon;
import com.myster.demo.repository.CouponRepository;
import com.myster.demo.repository.UserCouponRepository;
import com.myster.demo.service.CouponService;
import com.myster.demo.vo.CouponVO;
import com.myster.demo.vo.UserCouponVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 优惠券Service实现类
 * 
 * @author myster
 * @since 2025-07-06
 */
@Service
@Slf4j
public class CouponServiceImpl implements CouponService {
    
    @Autowired
    private CouponRepository couponRepository;
    
    @Autowired
    private UserCouponRepository userCouponRepository;
    
    @Override
    public List<CouponVO> getAvailableCoupons() {
        log.info("查询可用优惠券列表");
        List<Coupon> coupons = couponRepository.findAvailableCoupons(1, LocalDateTime.now());
        return coupons.stream().map(this::convertToVO).collect(Collectors.toList());
    }
    
    @Override
    public CouponVO getCouponById(Long id) {
        log.info("根据ID查询优惠券: {}", id);
        Optional<Coupon> coupon = couponRepository.findById(id);
        return coupon.map(this::convertToVO).orElse(null);
    }
    
    @Override
    @Transactional
    public CouponVO createCoupon(CouponDTO couponDTO) {
        log.info("创建优惠券: {}", couponDTO.getName());
        Coupon coupon = new Coupon();
        BeanUtils.copyProperties(couponDTO, coupon);
        coupon.setReceivedCount(0);
        coupon.setCreatedAt(LocalDateTime.now());
        coupon.setUpdatedAt(LocalDateTime.now());
        
        Coupon savedCoupon = couponRepository.save(coupon);
        return convertToVO(savedCoupon);
    }
    
    @Override
    @Transactional
    public CouponVO updateCoupon(Long id, CouponDTO couponDTO) {
        log.info("更新优惠券: {}", id);
        Optional<Coupon> optionalCoupon = couponRepository.findById(id);
        if (!optionalCoupon.isPresent()) {
            throw new RuntimeException("优惠券不存在");
        }
        
        Coupon coupon = optionalCoupon.get();
        BeanUtils.copyProperties(couponDTO, coupon);
        coupon.setUpdatedAt(LocalDateTime.now());
        
        Coupon savedCoupon = couponRepository.save(coupon);
        return convertToVO(savedCoupon);
    }
    
    @Override
    @Transactional
    public void deleteCoupon(Long id) {
        log.info("删除优惠券: {}", id);
        couponRepository.deleteById(id);
    }
    
    @Override
    @Transactional
    public UserCouponVO receiveCoupon(Long userId, Long couponId) {
        log.info("用户{}领取优惠券{}", userId, couponId);
        
        // 检查优惠券是否存在且可用
        Optional<Coupon> couponOptional = couponRepository.findById(couponId);
        if (!couponOptional.isPresent()) {
            throw new RuntimeException("优惠券不存在");
        }
        
        Coupon coupon = couponOptional.get();
        if (coupon.getStatus() != 1) {
            throw new RuntimeException("优惠券已禁用");
        }
        
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(coupon.getStartTime()) || now.isAfter(coupon.getEndTime())) {
            throw new RuntimeException("优惠券不在有效期内");
        }
        
        // 检查是否已领取
        Optional<UserCoupon> existingUserCoupon = userCouponRepository.findByUserIdAndCouponId(userId, couponId);
        if (existingUserCoupon.isPresent()) {
            throw new RuntimeException("您已领取过此优惠券");
        }
        
        // 检查发放数量限制
        if (coupon.getTotalCount() > 0) {
            long receivedCount = userCouponRepository.countByUserIdAndCouponId(userId, couponId);
            if (receivedCount >= coupon.getTotalCount()) {
                throw new RuntimeException("优惠券已发放完毕");
            }
        }
        
        // 创建用户优惠券
        UserCoupon userCoupon = UserCoupon.builder()
                .userId(userId)
                .couponId(couponId)
                .status(0) // 未使用
                .receivedAt(now)
                .expiredAt(coupon.getEndTime())
                .createdAt(now)
                .updatedAt(now)
                .build();
        
        UserCoupon savedUserCoupon = userCouponRepository.save(userCoupon);
        
        // 更新优惠券已领取数量
        coupon.setReceivedCount(coupon.getReceivedCount() + 1);
        coupon.setUpdatedAt(now);
        couponRepository.save(coupon);
        
        return convertToUserCouponVO(savedUserCoupon, coupon);
    }
    
    @Override
    public List<UserCouponVO> getUserCoupons(Long userId, Integer status) {
        log.info("查询用户{}优惠券列表，状态: {}", userId, status);
        List<UserCoupon> userCoupons;
        if (status != null) {
            userCoupons = userCouponRepository.findByUserIdAndStatus(userId, status);
        } else {
            userCoupons = userCouponRepository.findByUserId(userId);
        }
        
        return userCoupons.stream().map(this::convertToUserCouponVO).collect(Collectors.toList());
    }
    
    @Override
    public List<UserCouponVO> getAvailableUserCoupons(Long userId, BigDecimal orderAmount) {
        log.info("查询用户{}可用优惠券，订单金额: {}", userId, orderAmount);
        List<UserCoupon> userCoupons = userCouponRepository.findAvailableUserCoupons(userId, LocalDateTime.now());
        
        return userCoupons.stream()
                .map(this::convertToUserCouponVO)
                .filter(vo -> vo.getIsAvailable() && vo.getCouponMinAmount().compareTo(orderAmount) <= 0)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public boolean useCoupon(Long userId, Long userCouponId, Long orderId) {
        log.info("使用优惠券: 用户={}, 用户优惠券ID={}, 订单ID={}", userId, userCouponId, orderId);
        
        Optional<UserCoupon> userCouponOptional = userCouponRepository.findById(userCouponId);
        if (!userCouponOptional.isPresent()) {
            throw new RuntimeException("用户优惠券不存在");
        }
        
        UserCoupon userCoupon = userCouponOptional.get();
        if (!userCoupon.getUserId().equals(userId)) {
            throw new RuntimeException("无权使用此优惠券");
        }
        
        if (userCoupon.getStatus() != 0) {
            throw new RuntimeException("优惠券已使用或已过期");
        }
        
        if (userCoupon.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("优惠券已过期");
        }
        
        // 更新优惠券状态
        userCoupon.setStatus(1); // 已使用
        userCoupon.setOrderId(orderId);
        userCoupon.setUsedAt(LocalDateTime.now());
        userCoupon.setUpdatedAt(LocalDateTime.now());
        
        userCouponRepository.save(userCoupon);
        return true;
    }
    
    @Override
    public boolean validateCoupon(Long userId, Long userCouponId, BigDecimal orderAmount) {
        log.info("验证优惠券: 用户={}, 用户优惠券ID={}, 订单金额={}", userId, userCouponId, orderAmount);
        
        Optional<UserCoupon> userCouponOptional = userCouponRepository.findById(userCouponId);
        if (!userCouponOptional.isPresent()) {
            return false;
        }
        
        UserCoupon userCoupon = userCouponOptional.get();
        if (!userCoupon.getUserId().equals(userId)) {
            return false;
        }
        
        if (userCoupon.getStatus() != 0) {
            return false;
        }
        
        if (userCoupon.getExpiredAt().isBefore(LocalDateTime.now())) {
            return false;
        }
        
        // 获取优惠券模板信息
        Optional<Coupon> couponOptional = couponRepository.findById(userCoupon.getCouponId());
        if (!couponOptional.isPresent()) {
            return false;
        }
        
        Coupon coupon = couponOptional.get();
        return coupon.getMinAmount().compareTo(orderAmount) <= 0;
    }
    
    /**
     * 转换为CouponVO
     */
    private CouponVO convertToVO(Coupon coupon) {
        return CouponVO.builder()
                .id(coupon.getId())
                .name(coupon.getName())
                .type(coupon.getType())
                .value(coupon.getValue())
                .minAmount(coupon.getMinAmount())
                .description(coupon.getDescription())
                .startTime(coupon.getStartTime())
                .endTime(coupon.getEndTime())
                .status(coupon.getStatus())
                .createdAt(coupon.getCreatedAt())
                .build();
    }
    
    /**
     * 转换为UserCouponVO
     */
    private UserCouponVO convertToUserCouponVO(UserCoupon userCoupon) {
        Optional<Coupon> couponOptional = couponRepository.findById(userCoupon.getCouponId());
        Coupon coupon = couponOptional.orElse(null);
        return convertToUserCouponVO(userCoupon, coupon);
    }
    
    /**
     * 转换为UserCouponVO（带优惠券信息）
     */
    private UserCouponVO convertToUserCouponVO(UserCoupon userCoupon, Coupon coupon) {
        UserCouponVO vo = UserCouponVO.builder()
                .id(userCoupon.getId())
                .userId(userCoupon.getUserId())
                .couponId(userCoupon.getCouponId())
                .status(userCoupon.getStatus())
                .orderId(userCoupon.getOrderId())
                .receivedAt(userCoupon.getReceivedAt())
                .usedAt(userCoupon.getUsedAt())
                .expiredAt(userCoupon.getExpiredAt())
                .build();
        
        if (coupon != null) {
            vo.setCouponName(coupon.getName());
            vo.setCouponType(coupon.getType());
            vo.setCouponValue(coupon.getValue());
            vo.setCouponMinAmount(coupon.getMinAmount());
            vo.setCouponDescription(coupon.getDescription());
        }
        
        // 设置状态描述
        switch (userCoupon.getStatus()) {
            case 0:
                vo.setStatusDesc("未使用");
                vo.setIsAvailable(userCoupon.getExpiredAt().isAfter(LocalDateTime.now()));
                break;
            case 1:
                vo.setStatusDesc("已使用");
                vo.setIsAvailable(false);
                break;
            case 2:
                vo.setStatusDesc("已过期");
                vo.setIsAvailable(false);
                break;
            default:
                vo.setStatusDesc("未知状态");
                vo.setIsAvailable(false);
        }
        
        return vo;
    }
    
    @Override
    @Transactional
    public void updateUserCouponExpiryForTest(Long userId) {
        log.info("临时更新用户{}优惠券过期时间", userId);
        LocalDateTime newExpiryTime = LocalDateTime.now().plusYears(1); // 设置为一年后过期
        
        List<UserCoupon> userCoupons = userCouponRepository.findByUserId(userId);
        for (UserCoupon userCoupon : userCoupons) {
            userCoupon.setExpiredAt(newExpiryTime);
            userCoupon.setUpdatedAt(LocalDateTime.now());
            userCouponRepository.save(userCoupon);
        }
        
        log.info("已更新用户{}的{}张优惠券过期时间", userId, userCoupons.size());
    }
} 