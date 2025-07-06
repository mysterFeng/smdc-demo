package com.myster.demo.repository;

import com.myster.demo.entity.Dish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    
    /**
     * 根据分类ID查询菜品列表
     */
    List<Dish> findByCategoryIdAndStatusOrderBySortOrderAsc(Long categoryId, Integer status);
    
    /**
     * 根据状态查询菜品列表
     */
    List<Dish> findByStatusOrderBySortOrderAsc(Integer status);
    
    /**
     * 根据名称模糊查询菜品
     */
    @Query("SELECT d FROM Dish d WHERE d.name LIKE %:keyword% AND d.status = :status ORDER BY d.sortOrder ASC")
    List<Dish> findByNameContainingAndStatus(@Param("keyword") String keyword, @Param("status") Integer status);
    
    /**
     * 分页查询菜品
     */
    Page<Dish> findByStatus(Integer status, Pageable pageable);
    
    /**
     * 根据分类ID分页查询菜品
     */
    Page<Dish> findByCategoryIdAndStatus(Long categoryId, Integer status, Pageable pageable);
    
    /**
     * 查询推荐菜品（按销量或评分排序）
     */
    @Query("SELECT d FROM Dish d WHERE d.status = 1 ORDER BY d.sortOrder ASC, d.id ASC")
    List<Dish> findRecommendDishes(Pageable pageable);
} 