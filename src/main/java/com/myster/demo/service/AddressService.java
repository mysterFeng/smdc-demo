package com.myster.demo.service;

import com.myster.demo.dto.AddressDTO;
import com.myster.demo.vo.AddressVO;

import java.util.List;

/**
 * 地址Service接口
 * 
 * @author myster
 * @since 2025-07-06
 */
public interface AddressService {
    
    /**
     * 查询用户地址列表
     * 
     * @param userId 用户ID
     * @return 地址列表
     */
    List<AddressVO> getUserAddresses(Long userId);
    
    /**
     * 根据ID查询地址
     * 
     * @param userId 用户ID
     * @param id 地址ID
     * @return 地址信息
     */
    AddressVO getAddressById(Long userId, Long id);
    
    /**
     * 获取用户默认地址
     * 
     * @param userId 用户ID
     * @return 默认地址
     */
    AddressVO getDefaultAddress(Long userId);
    
    /**
     * 创建地址
     * 
     * @param userId 用户ID
     * @param addressDTO 地址DTO
     * @return 地址信息
     */
    AddressVO createAddress(Long userId, AddressDTO addressDTO);
    
    /**
     * 更新地址
     * 
     * @param userId 用户ID
     * @param id 地址ID
     * @param addressDTO 地址DTO
     * @return 地址信息
     */
    AddressVO updateAddress(Long userId, Long id, AddressDTO addressDTO);
    
    /**
     * 删除地址
     * 
     * @param userId 用户ID
     * @param id 地址ID
     */
    void deleteAddress(Long userId, Long id);
    
    /**
     * 设置默认地址
     * 
     * @param userId 用户ID
     * @param id 地址ID
     * @return 是否成功
     */
    boolean setDefaultAddress(Long userId, Long id);
} 