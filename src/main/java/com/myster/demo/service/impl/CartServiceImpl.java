package com.myster.demo.service.impl;

import com.myster.demo.dto.CartItemDTO;
import com.myster.demo.dto.CartItemUpdateDTO;
import com.myster.demo.entity.CartItem;
import com.myster.demo.entity.Dish;
import com.myster.demo.exception.BusinessException;
import com.myster.demo.repository.CartItemRepository;
import com.myster.demo.repository.DishRepository;
import com.myster.demo.service.CartService;
import com.myster.demo.vo.CartItemVO;
import com.myster.demo.vo.CartVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 购物车服务实现类
 * 
 * @author myster
 */
@Service
@Slf4j
public class CartServiceImpl implements CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private DishRepository dishRepository;

    @Override
    @Transactional
    public CartItemVO addToCart(Long userId, CartItemDTO cartItemDTO) {
        log.info("用户{}添加菜品{}到购物车，数量：{}", userId, cartItemDTO.getDishId(), cartItemDTO.getQuantity());

        // 验证菜品是否存在且上架
        Dish dish = dishRepository.findById(cartItemDTO.getDishId())
                .orElseThrow(() -> new BusinessException("菜品不存在"));

        if (dish.getStatus() != 1) {
            throw new BusinessException("菜品已下架，无法添加到购物车");
        }

        // 检查库存
        if (dish.getStock() < cartItemDTO.getQuantity()) {
            throw new BusinessException("菜品库存不足");
        }

        // 检查购物车中是否已存在该菜品
        Optional<CartItem> existingCartItem = cartItemRepository.findByUserIdAndDishId(userId, cartItemDTO.getDishId());

        CartItem cartItem;
        if (existingCartItem.isPresent()) {
            // 如果已存在，更新数量
            cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + cartItemDTO.getQuantity());
            
            // 检查更新后的数量是否超过库存
            if (dish.getStock() < cartItem.getQuantity()) {
                throw new BusinessException("菜品库存不足");
            }
        } else {
                    // 如果不存在，创建新的购物车项目
        cartItem = CartItem.builder()
                .userId(userId)
                .dishId(cartItemDTO.getDishId())
                .quantity(cartItemDTO.getQuantity())
                .unitPrice(dish.getPrice().doubleValue())
                .dishName(dish.getName())
                .dishImageUrl(dish.getImageUrl())
                .selected(true)
                .remark(cartItemDTO.getRemark())
                .build();
        }

        cartItem = cartItemRepository.save(cartItem);
        return convertToVO(cartItem);
    }

    @Override
    @Transactional
    public CartItemVO updateCartItem(Long userId, CartItemUpdateDTO updateDTO) {
        log.info("用户{}更新购物车项目{}，数量：{}", userId, updateDTO.getCartItemId(), updateDTO.getQuantity());

        CartItem cartItem = cartItemRepository.findById(updateDTO.getCartItemId())
                .orElseThrow(() -> new BusinessException("购物车项目不存在"));

        // 验证是否是当前用户的购物车项目
        if (!cartItem.getUserId().equals(userId)) {
            throw new BusinessException("无权操作此购物车项目");
        }

        // 验证菜品是否存在且上架
        Dish dish = dishRepository.findById(cartItem.getDishId())
                .orElseThrow(() -> new BusinessException("菜品不存在"));

        if (dish.getStatus() != 1) {
            throw new BusinessException("菜品已下架");
        }

        // 检查库存
        if (dish.getStock() < updateDTO.getQuantity()) {
            throw new BusinessException("菜品库存不足");
        }

        // 更新购物车项目
        cartItem.setQuantity(updateDTO.getQuantity());
        if (updateDTO.getSelected() != null) {
            cartItem.setSelected(updateDTO.getSelected());
        }
        if (updateDTO.getRemark() != null) {
            cartItem.setRemark(updateDTO.getRemark());
        }

        cartItem = cartItemRepository.save(cartItem);
        return convertToVO(cartItem);
    }

    @Override
    @Transactional
    public void removeCartItem(Long userId, Long cartItemId) {
        log.info("用户{}删除购物车项目{}", userId, cartItemId);

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new BusinessException("购物车项目不存在"));

        if (!cartItem.getUserId().equals(userId)) {
            throw new BusinessException("无权操作此购物车项目");
        }

        cartItemRepository.delete(cartItem);
    }

    @Override
    @Transactional
    public void removeCartItemByDishId(Long userId, Long dishId) {
        log.info("用户{}根据菜品ID{}删除购物车项目", userId, dishId);
        cartItemRepository.deleteByUserIdAndDishId(userId, dishId);
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        log.info("用户{}清空购物车", userId);
        cartItemRepository.deleteByUserId(userId);
    }

    @Override
    @Transactional
    public void clearSelectedItems(Long userId) {
        log.info("用户{}清空选中的购物车项目", userId);
        cartItemRepository.deleteSelectedByUserId(userId);
    }

    @Override
    public CartVO getUserCart(Long userId) {
        log.info("获取用户{}的购物车", userId);

        List<CartItem> cartItems = cartItemRepository.findByUserIdOrderByCreatedAtDesc(userId);
        List<CartItemVO> cartItemVOs = cartItems.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        // 计算统计数据
        int totalCount = cartItems.size();
        int selectedCount = (int) cartItems.stream().filter(CartItem::getSelected).count();
        
        BigDecimal totalAmount = cartItems.stream()
                .map(item -> BigDecimal.valueOf(item.getUnitPrice()).multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal selectedTotalAmount = cartItems.stream()
                .filter(CartItem::getSelected)
                .map(item -> BigDecimal.valueOf(item.getUnitPrice()).multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartVO.builder()
                .items(cartItemVOs)
                .totalCount(totalCount)
                .selectedCount(selectedCount)
                .totalAmount(totalAmount)
                .selectedTotalAmount(selectedTotalAmount)
                .build();
    }

    @Override
    public List<CartItemVO> getUserCartItems(Long userId) {
        log.info("获取用户{}的购物车项目列表", userId);
        List<CartItem> cartItems = cartItemRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return cartItems.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CartItemVO> getSelectedCartItems(Long userId) {
        log.info("获取用户{}选中的购物车项目", userId);
        List<CartItem> cartItems = cartItemRepository.findByUserIdAndSelectedTrue(userId);
        return cartItems.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CartItemVO toggleCartItemSelection(Long userId, Long cartItemId) {
        log.info("用户{}切换购物车项目{}的选中状态", userId, cartItemId);

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new BusinessException("购物车项目不存在"));

        if (!cartItem.getUserId().equals(userId)) {
            throw new BusinessException("无权操作此购物车项目");
        }

        cartItem.setSelected(!cartItem.getSelected());
        cartItem = cartItemRepository.save(cartItem);
        return convertToVO(cartItem);
    }

    @Override
    @Transactional
    public void toggleAllCartItemsSelection(Long userId, Boolean selected) {
        log.info("用户{}全选/取消全选购物车项目，选中状态：{}", userId, selected);

        List<CartItem> cartItems = cartItemRepository.findByUserIdOrderByCreatedAtDesc(userId);
        cartItems.forEach(cartItem -> cartItem.setSelected(selected));
        cartItemRepository.saveAll(cartItems);
    }

    @Override
    public Integer getCartItemCount(Long userId) {
        return (int) cartItemRepository.countByUserId(userId);
    }

    /**
     * 将CartItem实体转换为CartItemVO
     * 
     * @param cartItem 购物车项目实体
     * @return 购物车项目VO
     */
    private CartItemVO convertToVO(CartItem cartItem) {
        BigDecimal unitPrice = BigDecimal.valueOf(cartItem.getUnitPrice());
        BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity()));

        return CartItemVO.builder()
                .id(cartItem.getId())
                .dishId(cartItem.getDishId())
                .dishName(cartItem.getDishName())
                .dishImageUrl(cartItem.getDishImageUrl())
                .quantity(cartItem.getQuantity())
                .unitPrice(unitPrice)
                .subtotal(subtotal)
                .selected(cartItem.getSelected())
                .remark(cartItem.getRemark())
                .createdAt(cartItem.getCreatedAt())
                .build();
    }
} 