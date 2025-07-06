package com.myster.demo.service;

import com.myster.demo.dto.DishDTO;
import com.myster.demo.vo.DishVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DishService {
    
    /**
     * 获取菜品列表（分页）
     */
    Page<DishVO> getDishList(Pageable pageable);
    
    /**
     * 根据分类获取菜品列表（分页）
     */
    Page<DishVO> getDishListByCategory(Long categoryId, Pageable pageable);
    
    /**
     * 根据分类获取菜品列表
     */
    List<DishVO> getDishesByCategory(Long categoryId);
    
    /**
     * 根据ID获取菜品详情
     */
    DishVO getDishById(Long id);
    
    /**
     * 搜索菜品
     */
    List<DishVO> searchDishes(String keyword);
    
    /**
     * 获取推荐菜品
     */
    List<DishVO> getRecommendDishes(int limit);
    
    /**
     * 创建菜品
     */
    DishVO createDish(DishDTO dishDTO);
    
    /**
     * 更新菜品
     */
    DishVO updateDish(Long id, DishDTO dishDTO);
    
    /**
     * 删除菜品
     */
    void deleteDish(Long id);
    
    /**
     * 上架/下架菜品
     */
    void toggleDishStatus(Long id);
    
    /**
     * 更新菜品库存
     */
    void updateDishStock(Long id, Integer stock);
} 