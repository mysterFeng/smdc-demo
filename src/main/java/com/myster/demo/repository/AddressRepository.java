package com.myster.demo.repository;

import com.myster.demo.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 地址Repository
 * 
 * @author myster
 * @since 2025-07-06
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    
    /**
     * 根据用户ID查询地址列表
     * 
     * @param userId 用户ID
     * @return 地址列表
     */
    List<Address> findByUserId(Long userId);
    
    /**
     * 根据用户ID和是否默认查询地址
     * 
     * @param userId 用户ID
     * @param isDefault 是否默认
     * @return 地址
     */
    Optional<Address> findByUserIdAndIsDefault(Long userId, Integer isDefault);
    
    /**
     * 根据用户ID和地址ID查询地址
     * 
     * @param userId 用户ID
     * @param id 地址ID
     * @return 地址
     */
    Optional<Address> findByUserIdAndId(Long userId, Long id);
    
    /**
     * 统计用户地址数量
     * 
     * @param userId 用户ID
     * @return 数量
     */
    long countByUserId(Long userId);
    
    /**
     * 取消用户所有默认地址
     * 
     * @param userId 用户ID
     */
    @Modifying
    @Query("UPDATE Address a SET a.isDefault = 0 WHERE a.userId = :userId")
    void clearDefaultAddress(@Param("userId") Long userId);
} 