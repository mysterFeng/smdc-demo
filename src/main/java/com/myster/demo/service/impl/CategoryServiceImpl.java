package com.myster.demo.service.impl;

import com.myster.demo.entity.Category;
import com.myster.demo.repository.CategoryRepository;
import com.myster.demo.service.CategoryService;
import com.myster.demo.vo.CategoryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Override
    public List<CategoryVO> getAllCategories() {
        List<Category> categories = categoryRepository.findAllOrderBySortOrder();
        return categories.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<CategoryVO> getActiveCategories() {
        List<Category> categories = categoryRepository.findByStatusOrderBySortOrderAsc(1);
        return categories.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }
    
    @Override
    public CategoryVO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("分类不存在"));
        return convertToVO(category);
    }
    
    @Override
    @Transactional
    public CategoryVO createCategory(CategoryVO categoryVO) {
        // 检查分类名称是否已存在
        if (categoryRepository.findByName(categoryVO.getName()) != null) {
            throw new RuntimeException("分类名称已存在");
        }
        
        Category category = Category.builder()
                .name(categoryVO.getName())
                .description(categoryVO.getDescription())
                .sortOrder(categoryVO.getSortOrder() != null ? categoryVO.getSortOrder() : 0)
                .status(categoryVO.getStatus() != null ? categoryVO.getStatus() : 1)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .version(0L)
                .build();
        
        Category savedCategory = categoryRepository.save(category);
        return convertToVO(savedCategory);
    }
    
    @Override
    @Transactional
    public CategoryVO updateCategory(Long id, CategoryVO categoryVO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("分类不存在"));
        
        // 检查分类名称是否已被其他分类使用
        Category existingCategory = categoryRepository.findByName(categoryVO.getName());
        if (existingCategory != null && !existingCategory.getId().equals(id)) {
            throw new RuntimeException("分类名称已存在");
        }
        
        category.setName(categoryVO.getName());
        category.setDescription(categoryVO.getDescription());
        category.setSortOrder(categoryVO.getSortOrder());
        category.setStatus(categoryVO.getStatus());
        category.setUpdatedAt(LocalDateTime.now());
        
        Category updatedCategory = categoryRepository.save(category);
        return convertToVO(updatedCategory);
    }
    
    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("分类不存在"));
        
        // TODO: 检查分类下是否有菜品，如果有则不允许删除
        categoryRepository.delete(category);
    }
    
    @Override
    @Transactional
    public void toggleCategoryStatus(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("分类不存在"));
        
        category.setStatus(category.getStatus() == 1 ? 0 : 1);
        category.setUpdatedAt(LocalDateTime.now());
        categoryRepository.save(category);
    }
    
    private CategoryVO convertToVO(Category category) {
        return CategoryVO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .sortOrder(category.getSortOrder())
                .status(category.getStatus())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }
} 