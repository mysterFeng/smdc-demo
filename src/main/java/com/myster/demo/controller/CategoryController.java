package com.myster.demo.controller;

import com.myster.demo.service.CategoryService;
import com.myster.demo.vo.CategoryVO;
import com.myster.demo.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/categories")
@Slf4j
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;
    
    /**
     * 获取所有分类列表
     */
    @GetMapping
    public Result<List<CategoryVO>> getAllCategories() {
        List<CategoryVO> categories = categoryService.getAllCategories();
        return Result.success(categories);
    }
    
    /**
     * 获取启用的分类列表
     */
    @GetMapping("/active")
    public Result<List<CategoryVO>> getActiveCategories() {
        List<CategoryVO> categories = categoryService.getActiveCategories();
        return Result.success(categories);
    }
    
    /**
     * 根据ID获取分类
     */
    @GetMapping("/{id}")
    public Result<CategoryVO> getCategoryById(@PathVariable Long id) {
        CategoryVO category = categoryService.getCategoryById(id);
        return Result.success(category);
    }
    
    /**
     * 创建分类
     */
    @PostMapping
    public Result<CategoryVO> createCategory(@Valid @RequestBody CategoryVO categoryVO) {
        CategoryVO createdCategory = categoryService.createCategory(categoryVO);
        return Result.success(createdCategory);
    }
    
    /**
     * 更新分类
     */
    @PutMapping("/{id}")
    public Result<CategoryVO> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryVO categoryVO) {
        CategoryVO updatedCategory = categoryService.updateCategory(id, categoryVO);
        return Result.success(updatedCategory);
    }
    
    /**
     * 删除分类
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return Result.success();
    }
    
    /**
     * 启用/禁用分类
     */
    @PutMapping("/{id}/toggle")
    public Result<Void> toggleCategoryStatus(@PathVariable Long id) {
        categoryService.toggleCategoryStatus(id);
        return Result.success();
    }
} 