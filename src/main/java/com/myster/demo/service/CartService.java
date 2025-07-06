package com.myster.demo.service;

import com.myster.demo.dto.CartItemDTO;
import com.myster.demo.dto.CartItemUpdateDTO;
import com.myster.demo.vo.CartItemVO;
import com.myster.demo.vo.CartVO;

import java.util.List;

/**
 * 购物车服务接口
 * 
 * @author myster
 */
public interface CartService {

    /**
     * 添加菜品到购物车
     * 
     * @param userId 用户ID
     * @param cartItemDTO 购物车项目DTO
     * @return 购物车项目VO
     */
    CartItemVO addToCart(Long userId, CartItemDTO cartItemDTO);

    /**
     * 更新购物车项目
     * 
     * @param userId 用户ID
     * @param updateDTO 更新DTO
     * @return 购物车项目VO
     */
    CartItemVO updateCartItem(Long userId, CartItemUpdateDTO updateDTO);

    /**
     * 删除购物车项目
     * 
     * @param userId 用户ID
     * @param cartItemId 购物车项目ID
     */
    void removeCartItem(Long userId, Long cartItemId);

    /**
     * 根据菜品ID删除购物车项目
     * 
     * @param userId 用户ID
     * @param dishId 菜品ID
     */
    void removeCartItemByDishId(Long userId, Long dishId);

    /**
     * 清空购物车
     * 
     * @param userId 用户ID
     */
    void clearCart(Long userId);

    /**
     * 清空选中的购物车项目
     * 
     * @param userId 用户ID
     */
    void clearSelectedItems(Long userId);

    /**
     * 获取用户购物车
     * 
     * @param userId 用户ID
     * @return 购物车VO
     */
    CartVO getUserCart(Long userId);

    /**
     * 获取用户购物车项目列表
     * 
     * @param userId 用户ID
     * @return 购物车项目列表
     */
    List<CartItemVO> getUserCartItems(Long userId);

    /**
     * 获取用户选中的购物车项目
     * 
     * @param userId 用户ID
     * @return 选中的购物车项目列表
     */
    List<CartItemVO> getSelectedCartItems(Long userId);

    /**
     * 切换购物车项目选中状态
     * 
     * @param userId 用户ID
     * @param cartItemId 购物车项目ID
     * @return 购物车项目VO
     */
    CartItemVO toggleCartItemSelection(Long userId, Long cartItemId);

    /**
     * 全选/取消全选购物车项目
     * 
     * @param userId 用户ID
     * @param selected 是否选中
     */
    void toggleAllCartItemsSelection(Long userId, Boolean selected);

    /**
     * 获取购物车项目数量
     * 
     * @param userId 用户ID
     * @return 购物车项目数量
     */
    Integer getCartItemCount(Long userId);
} 