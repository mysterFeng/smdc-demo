package com.myster.demo.entity;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户地址实体类
 * 
 * @author myster
 * @since 2025-07-06
 */
@Entity
@Table(name = "addresses")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "name", nullable = false, length = 32)
    private String name;
    
    @Column(name = "phone", nullable = false, length = 20)
    private String phone;
    
    @Column(name = "province", nullable = false, length = 32)
    private String province;
    
    @Column(name = "city", nullable = false, length = 32)
    private String city;
    
    @Column(name = "district", nullable = false, length = 32)
    private String district;
    
    @Column(name = "detail", nullable = false, length = 128)
    private String detail;
    
    @Column(name = "is_default", nullable = false)
    private Integer isDefault;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
} 