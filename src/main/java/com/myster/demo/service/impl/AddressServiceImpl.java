package com.myster.demo.service.impl;

import com.myster.demo.dto.AddressDTO;
import com.myster.demo.entity.Address;
import com.myster.demo.repository.AddressRepository;
import com.myster.demo.service.AddressService;
import com.myster.demo.vo.AddressVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 地址Service实现类
 * 
 * @author myster
 * @since 2025-07-06
 */
@Service
@Slf4j
public class AddressServiceImpl implements AddressService {
    
    @Autowired
    private AddressRepository addressRepository;
    
    @Override
    public List<AddressVO> getUserAddresses(Long userId) {
        log.info("查询用户{}地址列表", userId);
        List<Address> addresses = addressRepository.findByUserId(userId);
        return addresses.stream().map(this::convertToVO).collect(Collectors.toList());
    }
    
    @Override
    public AddressVO getAddressById(Long userId, Long id) {
        log.info("查询用户{}地址{}", userId, id);
        Optional<Address> address = addressRepository.findByUserIdAndId(userId, id);
        return address.map(this::convertToVO).orElse(null);
    }
    
    @Override
    public AddressVO getDefaultAddress(Long userId) {
        log.info("查询用户{}默认地址", userId);
        Optional<Address> address = addressRepository.findByUserIdAndIsDefault(userId, 1);
        return address.map(this::convertToVO).orElse(null);
    }
    
    @Override
    @Transactional
    public AddressVO createAddress(Long userId, AddressDTO addressDTO) {
        log.info("用户{}创建地址", userId);
        
        // 如果设置为默认地址，先取消其他默认地址
        if (addressDTO.getIsDefault() == 1) {
            addressRepository.clearDefaultAddress(userId);
        }
        
        Address address = new Address();
        BeanUtils.copyProperties(addressDTO, address);
        address.setUserId(userId);
        address.setCreatedAt(LocalDateTime.now());
        address.setUpdatedAt(LocalDateTime.now());
        
        Address savedAddress = addressRepository.save(address);
        return convertToVO(savedAddress);
    }
    
    @Override
    @Transactional
    public AddressVO updateAddress(Long userId, Long id, AddressDTO addressDTO) {
        log.info("用户{}更新地址{}", userId, id);
        
        Optional<Address> addressOptional = addressRepository.findByUserIdAndId(userId, id);
        if (!addressOptional.isPresent()) {
            throw new RuntimeException("地址不存在");
        }
        
        Address address = addressOptional.get();
        
        // 如果设置为默认地址，先取消其他默认地址
        if (addressDTO.getIsDefault() == 1 && address.getIsDefault() == 0) {
            addressRepository.clearDefaultAddress(userId);
        }
        
        // 手动更新字段，避免使用BeanUtils.copyProperties可能导致的ID修改问题
        address.setName(addressDTO.getName());
        address.setPhone(addressDTO.getPhone());
        address.setProvince(addressDTO.getProvince());
        address.setCity(addressDTO.getCity());
        address.setDistrict(addressDTO.getDistrict());
        address.setDetail(addressDTO.getDetail());
        address.setIsDefault(addressDTO.getIsDefault());
        address.setUpdatedAt(LocalDateTime.now());
        
        Address savedAddress = addressRepository.save(address);
        return convertToVO(savedAddress);
    }
    
    @Override
    @Transactional
    public void deleteAddress(Long userId, Long id) {
        log.info("用户{}删除地址{}", userId, id);
        
        Optional<Address> addressOptional = addressRepository.findByUserIdAndId(userId, id);
        if (!addressOptional.isPresent()) {
            throw new RuntimeException("地址不存在");
        }
        
        Address address = addressOptional.get();
        addressRepository.delete(address);
    }
    
    @Override
    @Transactional
    public boolean setDefaultAddress(Long userId, Long id) {
        log.info("用户{}设置默认地址{}", userId, id);
        
        Optional<Address> addressOptional = addressRepository.findByUserIdAndId(userId, id);
        if (!addressOptional.isPresent()) {
            throw new RuntimeException("地址不存在");
        }
        
        // 取消所有默认地址
        addressRepository.clearDefaultAddress(userId);
        
        // 设置新的默认地址
        Address address = addressOptional.get();
        address.setIsDefault(1);
        address.setUpdatedAt(LocalDateTime.now());
        addressRepository.save(address);
        
        return true;
    }
    
    /**
     * 转换为AddressVO
     */
    private AddressVO convertToVO(Address address) {
        AddressVO vo = AddressVO.builder()
                .id(address.getId())
                .userId(address.getUserId())
                .name(address.getName())
                .phone(address.getPhone())
                .province(address.getProvince())
                .city(address.getCity())
                .district(address.getDistrict())
                .detail(address.getDetail())
                .isDefault(address.getIsDefault())
                .createdAt(address.getCreatedAt())
                .updatedAt(address.getUpdatedAt())
                .build();
        
        // 设置完整地址字符串
        vo.setFullAddress(address.getProvince() + " " + address.getCity() + " " + address.getDistrict() + " " + address.getDetail());
        
        // 设置是否默认地址描述
        vo.setIsDefaultDesc(address.getIsDefault() == 1 ? "默认地址" : "普通地址");
        
        return vo;
    }
} 