package com.myster.demo.controller;

import com.myster.demo.dto.DishDTO;
import com.myster.demo.service.DishService;
import com.myster.demo.vo.DishVO;
import com.myster.demo.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/dishes")
@Slf4j
public class DishController {
    
    @Autowired
    private DishService dishService;
    
    /**
     * 获取菜品列表（分页）
     */
    @GetMapping
    public Result<Page<DishVO>> getDishList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long categoryId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DishVO> dishPage;
        
        if (categoryId != null) {
            // 按分类过滤
            dishPage = dishService.getDishListByCategory(categoryId, pageable);
        } else {
            // 获取所有菜品
            dishPage = dishService.getDishList(pageable);
        }
        
        return Result.success(dishPage);
    }
    
    /**
     * 根据分类获取菜品列表
     */
    @GetMapping("/category/{categoryId}")
    public Result<List<DishVO>> getDishesByCategory(@PathVariable Long categoryId) {
        List<DishVO> dishes = dishService.getDishesByCategory(categoryId);
        return Result.success(dishes);
    }
    
    /**
     * 根据ID获取菜品详情
     */
    @GetMapping("/{id}")
    public Result<DishVO> getDishById(@PathVariable Long id) {
        DishVO dish = dishService.getDishById(id);
        return Result.success(dish);
    }
    
    /**
     * 搜索菜品
     */
    @GetMapping("/search")
    public Result<List<DishVO>> searchDishes(@RequestParam String keyword) {
        List<DishVO> dishes = dishService.searchDishes(keyword);
        return Result.success(dishes);
    }
    
    /**
     * 获取推荐菜品
     */
    @GetMapping("/recommend")
    public Result<List<DishVO>> getRecommendDishes(@RequestParam(defaultValue = "4") int limit) {
        List<DishVO> dishes = dishService.getRecommendDishes(limit);
        return Result.success(dishes);
    }
    
    /**
     * 创建菜品
     */
    @PostMapping
    public Result<DishVO> createDish(@Valid @RequestBody DishDTO dishDTO) {
        DishVO createdDish = dishService.createDish(dishDTO);
        return Result.success(createdDish);
    }
    
    /**
     * 更新菜品
     */
    @PutMapping("/{id}")
    public Result<DishVO> updateDish(@PathVariable Long id, @Valid @RequestBody DishDTO dishDTO) {
        DishVO updatedDish = dishService.updateDish(id, dishDTO);
        return Result.success(updatedDish);
    }
    
    /**
     * 删除菜品
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteDish(@PathVariable Long id) {
        dishService.deleteDish(id);
        return Result.success();
    }
    
    /**
     * 上架/下架菜品
     */
    @PutMapping("/{id}/toggle")
    public Result<Void> toggleDishStatus(@PathVariable Long id) {
        dishService.toggleDishStatus(id);
        return Result.success();
    }
    
    /**
     * 更新菜品库存
     */
    @PutMapping("/{id}/stock")
    public Result<Void> updateDishStock(@PathVariable Long id, @RequestParam Integer stock) {
        dishService.updateDishStock(id, stock);
        return Result.success();
    }
} 