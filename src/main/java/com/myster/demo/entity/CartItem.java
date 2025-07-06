package com.myster.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 购物车项目实体类
 * 
 * @author myster
 */
@Entity
@Table(name = "cart_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItem extends BaseEntity {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户ID
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * 菜品ID
     */
    @Column(name = "dish_id", nullable = false)
    private Long dishId;

    /**
     * 菜品数量
     */
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    /**
     * 菜品单价（快照，防止菜品价格变化影响购物车）
     */
    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private Double unitPrice;

    /**
     * 菜品名称（快照）
     */
    @Column(name = "dish_name", nullable = false)
    private String dishName;

    /**
     * 菜品图片URL（快照）
     */
    @Column(name = "dish_image_url")
    private String dishImageUrl;

    /**
     * 是否选中
     */
    @Column(name = "selected", nullable = false)
    private Boolean selected = true;

    /**
     * 备注信息
     */
    @Column(name = "remark")
    private String remark;

    /**
     * 关联的菜品信息
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id", insertable = false, updatable = false)
    private Dish dish;

    /**
     * 关联的用户信息
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
} 