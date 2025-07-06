package com.myster.demo.controller;

import com.myster.demo.dto.CartItemDTO;
import com.myster.demo.dto.CartItemUpdateDTO;
import com.myster.demo.service.CartService;
import com.myster.demo.util.Result;
import com.myster.demo.vo.CartItemVO;
import com.myster.demo.vo.CartVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 购物车控制器
 * 
 * @author myster
 */
@RestController
@RequestMapping("/v1/cart")
@Slf4j
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * 添加菜品到购物车
     * 
     * @param userId 用户ID（从token中获取，这里暂时使用路径参数）
     * @param cartItemDTO 购物车项目DTO
     * @return 购物车项目VO
     */
    @PostMapping("/{userId}/items")
    public Result<CartItemVO> addToCart(@PathVariable Long userId, 
                                       @Valid @RequestBody CartItemDTO cartItemDTO) {
        log.info("用户{}添加菜品到购物车，菜品ID：{}，数量：{}", userId, cartItemDTO.getDishId(), cartItemDTO.getQuantity());
        CartItemVO cartItemVO = cartService.addToCart(userId, cartItemDTO);
        return Result.success(cartItemVO);
    }

    /**
     * 更新购物车项目
     * 
     * @param userId 用户ID
     * @param updateDTO 更新DTO
     * @return 购物车项目VO
     */
    @PutMapping("/{userId}/items")
    public Result<CartItemVO> updateCartItem(@PathVariable Long userId, 
                                            @Valid @RequestBody CartItemUpdateDTO updateDTO) {
        log.info("用户{}更新购物车项目{}", userId, updateDTO.getCartItemId());
        CartItemVO cartItemVO = cartService.updateCartItem(userId, updateDTO);
        return Result.success(cartItemVO);
    }

    /**
     * 删除购物车项目
     * 
     * @param userId 用户ID
     * @param cartItemId 购物车项目ID
     * @return 操作结果
     */
    @DeleteMapping("/{userId}/items/{cartItemId}")
    public Result<Void> removeCartItem(@PathVariable Long userId, 
                                      @PathVariable Long cartItemId) {
        log.info("用户{}删除购物车项目{}", userId, cartItemId);
        cartService.removeCartItem(userId, cartItemId);
        return Result.success();
    }

    /**
     * 根据菜品ID删除购物车项目
     * 
     * @param userId 用户ID
     * @param dishId 菜品ID
     * @return 操作结果
     */
    @DeleteMapping("/{userId}/items/dish/{dishId}")
    public Result<Void> removeCartItemByDishId(@PathVariable Long userId, 
                                              @PathVariable Long dishId) {
        log.info("用户{}根据菜品ID{}删除购物车项目", userId, dishId);
        cartService.removeCartItemByDishId(userId, dishId);
        return Result.success();
    }

    /**
     * 清空购物车
     * 
     * @param userId 用户ID
     * @return 操作结果
     */
    @DeleteMapping("/{userId}/items")
    public Result<Void> clearCart(@PathVariable Long userId) {
        log.info("用户{}清空购物车", userId);
        cartService.clearCart(userId);
        return Result.success();
    }

    /**
     * 清空选中的购物车项目
     * 
     * @param userId 用户ID
     * @return 操作结果
     */
    @DeleteMapping("/{userId}/items/selected")
    public Result<Void> clearSelectedItems(@PathVariable Long userId) {
        log.info("用户{}清空选中的购物车项目", userId);
        cartService.clearSelectedItems(userId);
        return Result.success();
    }

    /**
     * 获取用户购物车
     * 
     * @param userId 用户ID
     * @return 购物车VO
     */
    @GetMapping("/{userId}")
    public Result<CartVO> getUserCart(@PathVariable Long userId) {
        log.info("获取用户{}的购物车", userId);
        CartVO cartVO = cartService.getUserCart(userId);
        return Result.success(cartVO);
    }

    /**
     * 获取用户购物车项目列表
     * 
     * @param userId 用户ID
     * @return 购物车项目列表
     */
    @GetMapping("/{userId}/items")
    public Result<List<CartItemVO>> getUserCartItems(@PathVariable Long userId) {
        log.info("获取用户{}的购物车项目列表", userId);
        List<CartItemVO> cartItems = cartService.getUserCartItems(userId);
        return Result.success(cartItems);
    }

    /**
     * 获取用户选中的购物车项目
     * 
     * @param userId 用户ID
     * @return 选中的购物车项目列表
     */
    @GetMapping("/{userId}/items/selected")
    public Result<List<CartItemVO>> getSelectedCartItems(@PathVariable Long userId) {
        log.info("获取用户{}选中的购物车项目", userId);
        List<CartItemVO> cartItems = cartService.getSelectedCartItems(userId);
        return Result.success(cartItems);
    }

    /**
     * 切换购物车项目选中状态
     * 
     * @param userId 用户ID
     * @param cartItemId 购物车项目ID
     * @return 购物车项目VO
     */
    @PutMapping("/{userId}/items/{cartItemId}/toggle")
    public Result<CartItemVO> toggleCartItemSelection(@PathVariable Long userId, 
                                                     @PathVariable Long cartItemId) {
        log.info("用户{}切换购物车项目{}的选中状态", userId, cartItemId);
        CartItemVO cartItemVO = cartService.toggleCartItemSelection(userId, cartItemId);
        return Result.success(cartItemVO);
    }

    /**
     * 全选/取消全选购物车项目
     * 
     * @param userId 用户ID
     * @param selected 是否选中
     * @return 操作结果
     */
    @PutMapping("/{userId}/items/select-all")
    public Result<Void> toggleAllCartItemsSelection(@PathVariable Long userId, 
                                                   @RequestParam Boolean selected) {
        log.info("用户{}全选/取消全选购物车项目，选中状态：{}", userId, selected);
        cartService.toggleAllCartItemsSelection(userId, selected);
        return Result.success();
    }

    /**
     * 获取购物车项目数量
     * 
     * @param userId 用户ID
     * @return 购物车项目数量
     */
    @GetMapping("/{userId}/count")
    public Result<Integer> getCartItemCount(@PathVariable Long userId) {
        log.info("获取用户{}的购物车项目数量", userId);
        Integer count = cartService.getCartItemCount(userId);
        return Result.success(count);
    }
} 