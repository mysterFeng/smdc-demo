package com.myster.demo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Data
public class DishDTO {
    
    private Long id;
    
    @NotBlank(message = "菜品名称不能为空")
    private String name;
    
    private String description;
    
    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "0.01", message = "价格必须大于0")
    private BigDecimal price;
    
    private BigDecimal originalPrice;
    
    private Long categoryId;
    
    private String imageUrl;
    
    private Integer stock;
    
    private Integer status;
    
    private Integer sortOrder;
} 