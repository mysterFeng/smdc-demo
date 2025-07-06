package com.myster.demo.service;

import com.myster.demo.vo.CategoryVO;
import java.util.List;

public interface CategoryService {
    
    /**
     * 获取所有分类列表
     */
    List<CategoryVO> getAllCategories();
    
    /**
     * 获取启用的分类列表
     */
    List<CategoryVO> getActiveCategories();
    
    /**
     * 根据ID获取分类
     */
    CategoryVO getCategoryById(Long id);
    
    /**
     * 创建分类
     */
    CategoryVO createCategory(CategoryVO categoryVO);
    
    /**
     * 更新分类
     */
    CategoryVO updateCategory(Long id, CategoryVO categoryVO);
    
    /**
     * 删除分类
     */
    void deleteCategory(Long id);
    
    /**
     * 启用/禁用分类
     */
    void toggleCategoryStatus(Long id);
} 