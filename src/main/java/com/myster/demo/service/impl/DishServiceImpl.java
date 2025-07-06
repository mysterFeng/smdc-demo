package com.myster.demo.service.impl;

import com.myster.demo.dto.DishDTO;
import com.myster.demo.entity.Dish;
import com.myster.demo.repository.DishRepository;
import com.myster.demo.service.DishService;
import com.myster.demo.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl implements DishService {
    
    @Autowired
    private DishRepository dishRepository;
    
    @Override
    public Page<DishVO> getDishList(Pageable pageable) {
        Page<Dish> dishPage = dishRepository.findByStatus(1, pageable);
        return dishPage.map(this::convertToVO);
    }
    
    @Override
    public Page<DishVO> getDishListByCategory(Long categoryId, Pageable pageable) {
        Page<Dish> dishPage = dishRepository.findByCategoryIdAndStatus(categoryId, 1, pageable);
        return dishPage.map(this::convertToVO);
    }
    
    @Override
    public List<DishVO> getDishesByCategory(Long categoryId) {
        List<Dish> dishes = dishRepository.findByCategoryIdAndStatusOrderBySortOrderAsc(categoryId, 1);
        return dishes.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }
    
    @Override
    public DishVO getDishById(Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("菜品不存在"));
        return convertToVO(dish);
    }
    
    @Override
    public List<DishVO> searchDishes(String keyword) {
        List<Dish> dishes = dishRepository.findByNameContainingAndStatus(keyword, 1);
        return dishes.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<DishVO> getRecommendDishes(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<Dish> dishes = dishRepository.findRecommendDishes(pageable);
        return dishes.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public DishVO createDish(DishDTO dishDTO) {
        Dish dish = Dish.builder()
                .name(dishDTO.getName())
                .description(dishDTO.getDescription())
                .price(dishDTO.getPrice())
                .originalPrice(dishDTO.getOriginalPrice())
                .categoryId(dishDTO.getCategoryId())
                .imageUrl(dishDTO.getImageUrl())
                .stock(dishDTO.getStock() != null ? dishDTO.getStock() : 0)
                .status(dishDTO.getStatus() != null ? dishDTO.getStatus() : 1)
                .sortOrder(dishDTO.getSortOrder() != null ? dishDTO.getSortOrder() : 0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .version(0L)
                .build();
        
        Dish savedDish = dishRepository.save(dish);
        return convertToVO(savedDish);
    }
    
    @Override
    @Transactional
    public DishVO updateDish(Long id, DishDTO dishDTO) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("菜品不存在"));
        
        dish.setName(dishDTO.getName());
        dish.setDescription(dishDTO.getDescription());
        dish.setPrice(dishDTO.getPrice());
        dish.setOriginalPrice(dishDTO.getOriginalPrice());
        dish.setCategoryId(dishDTO.getCategoryId());
        dish.setImageUrl(dishDTO.getImageUrl());
        dish.setStock(dishDTO.getStock());
        dish.setStatus(dishDTO.getStatus());
        dish.setSortOrder(dishDTO.getSortOrder());
        dish.setUpdatedAt(LocalDateTime.now());
        
        Dish updatedDish = dishRepository.save(dish);
        return convertToVO(updatedDish);
    }
    
    @Override
    @Transactional
    public void deleteDish(Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("菜品不存在"));
        
        dishRepository.delete(dish);
    }
    
    @Override
    @Transactional
    public void toggleDishStatus(Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("菜品不存在"));
        
        dish.setStatus(dish.getStatus() == 1 ? 0 : 1);
        dish.setUpdatedAt(LocalDateTime.now());
        dishRepository.save(dish);
    }
    
    @Override
    @Transactional
    public void updateDishStock(Long id, Integer stock) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("菜品不存在"));
        
        dish.setStock(stock);
        dish.setUpdatedAt(LocalDateTime.now());
        dishRepository.save(dish);
    }
    
    private DishVO convertToVO(Dish dish) {
        return DishVO.builder()
                .id(dish.getId())
                .name(dish.getName())
                .description(dish.getDescription())
                .price(dish.getPrice())
                .originalPrice(dish.getOriginalPrice())
                .categoryId(dish.getCategoryId())
                .categoryName(dish.getCategory() != null ? dish.getCategory().getName() : null)
                .imageUrl(dish.getImageUrl())
                .stock(dish.getStock())
                .status(dish.getStatus())
                .sortOrder(dish.getSortOrder())
                .createdAt(dish.getCreatedAt())
                .updatedAt(dish.getUpdatedAt())
                .build();
    }
} 