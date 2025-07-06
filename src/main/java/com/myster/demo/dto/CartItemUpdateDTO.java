package com.myster.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 购物车项目更新DTO
 * 
 * @author myster
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemUpdateDTO {

    /**
     * 购物车项目ID
     */
    @NotNull(message = "购物车项目ID不能为空")
    private Long cartItemId;

    /**
     * 菜品数量
     */
    @NotNull(message = "菜品数量不能为空")
    @Min(value = 1, message = "菜品数量必须大于0")
    private Integer quantity;

    /**
     * 是否选中
     */
    private Boolean selected;

    /**
     * 备注信息
     */
    private String remark;
} 