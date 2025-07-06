package com.myster.demo.repository;

import com.myster.demo.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 购物车项目Repository
 * 
 * @author myster
 */
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    /**
     * 根据用户ID查询购物车项目列表
     * 
     * @param userId 用户ID
     * @return 购物车项目列表
     */
    List<CartItem> findByUserIdOrderByCreatedAtDesc(Long userId);

    /**
     * 根据用户ID和菜品ID查询购物车项目
     * 
     * @param userId 用户ID
     * @param dishId 菜品ID
     * @return 购物车项目
     */
    Optional<CartItem> findByUserIdAndDishId(Long userId, Long dishId);

    /**
     * 根据用户ID查询选中的购物车项目
     * 
     * @param userId 用户ID
     * @return 选中的购物车项目列表
     */
    List<CartItem> findByUserIdAndSelectedTrue(Long userId);

    /**
     * 根据用户ID统计购物车项目数量
     * 
     * @param userId 用户ID
     * @return 购物车项目数量
     */
    long countByUserId(Long userId);

    /**
     * 根据用户ID统计选中的购物车项目数量
     * 
     * @param userId 用户ID
     * @return 选中的购物车项目数量
     */
    long countByUserIdAndSelectedTrue(Long userId);

    /**
     * 根据用户ID删除购物车项目
     * 
     * @param userId 用户ID
     */
    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.userId = :userId")
    void deleteByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID删除选中的购物车项目
     * 
     * @param userId 用户ID
     */
    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.userId = :userId AND c.selected = true")
    void deleteSelectedByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID和菜品ID删除购物车项目
     * 
     * @param userId 用户ID
     * @param dishId 菜品ID
     */
    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.userId = :userId AND c.dishId = :dishId")
    void deleteByUserIdAndDishId(@Param("userId") Long userId, @Param("dishId") Long dishId);
} 