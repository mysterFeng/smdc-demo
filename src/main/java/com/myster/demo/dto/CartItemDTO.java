package com.myster.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 购物车项目DTO
 * 
 * @author myster
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {

    /**
     * 菜品ID
     */
    @NotNull(message = "菜品ID不能为空")
    private Long dishId;

    /**
     * 菜品数量
     */
    @NotNull(message = "菜品数量不能为空")
    @Min(value = 1, message = "菜品数量必须大于0")
    private Integer quantity;

    /**
     * 备注信息
     */
    private String remark;
} 