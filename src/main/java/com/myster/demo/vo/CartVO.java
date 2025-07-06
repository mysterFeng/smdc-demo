package com.myster.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车VO
 * 
 * @author myster
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartVO {

    /**
     * 购物车项目列表
     */
    private List<CartItemVO> items;

    /**
     * 选中项目数量
     */
    private Integer selectedCount;

    /**
     * 总项目数量
     */
    private Integer totalCount;

    /**
     * 选中项目总金额
     */
    private BigDecimal selectedTotalAmount;

    /**
     * 总金额
     */
    private BigDecimal totalAmount;
} 