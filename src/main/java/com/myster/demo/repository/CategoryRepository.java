package com.myster.demo.repository;

import com.myster.demo.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    /**
     * 根据状态查询分类列表
     */
    List<Category> findByStatusOrderBySortOrderAsc(Integer status);
    
    /**
     * 查询所有分类（按排序）
     */
    @Query("SELECT c FROM Category c ORDER BY c.sortOrder ASC, c.id ASC")
    List<Category> findAllOrderBySortOrder();
    
    /**
     * 根据名称查询分类
     */
    Category findByName(String name);
} 