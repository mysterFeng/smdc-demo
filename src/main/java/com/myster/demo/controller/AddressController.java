package com.myster.demo.controller;

import com.myster.demo.dto.AddressDTO;
import com.myster.demo.service.AddressService;
import com.myster.demo.vo.AddressVO;
import com.myster.demo.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 地址Controller
 * 
 * @author myster
 * @since 2025-07-06
 */
@RestController
@RequestMapping("/v1/addresses")
@Slf4j
public class AddressController {
    
    @Autowired
    private AddressService addressService;
    
    /**
     * 查询用户地址列表
     */
    @GetMapping
    public Result<List<AddressVO>> getUserAddresses(@RequestParam Long userId) {
        log.info("查询用户{}地址列表", userId);
        List<AddressVO> addresses = addressService.getUserAddresses(userId);
        return Result.success(addresses);
    }
    
    /**
     * 根据ID查询地址
     */
    @GetMapping("/{id}")
    public Result<AddressVO> getAddressById(@RequestParam Long userId, @PathVariable Long id) {
        log.info("查询用户{}地址{}", userId, id);
        AddressVO address = addressService.getAddressById(userId, id);
        if (address == null) {
            return Result.error("地址不存在");
        }
        return Result.success(address);
    }
    
    /**
     * 获取用户默认地址
     */
    @GetMapping("/default")
    public Result<AddressVO> getDefaultAddress(@RequestParam Long userId) {
        log.info("查询用户{}默认地址", userId);
        AddressVO address = addressService.getDefaultAddress(userId);
        return Result.success(address);
    }
    
    /**
     * 创建地址
     */
    @PostMapping
    public Result<AddressVO> createAddress(@RequestParam Long userId, @Valid @RequestBody AddressDTO addressDTO) {
        log.info("用户{}创建地址", userId);
        try {
            AddressVO address = addressService.createAddress(userId, addressDTO);
            return Result.success(address);
        } catch (Exception e) {
            log.error("创建地址失败", e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 更新地址
     */
    @PutMapping("/{id}")
    public Result<AddressVO> updateAddress(@RequestParam Long userId, 
                                         @PathVariable Long id, 
                                         @Valid @RequestBody AddressDTO addressDTO) {
        log.info("用户{}更新地址{}", userId, id);
        try {
            AddressVO address = addressService.updateAddress(userId, id, addressDTO);
            return Result.success(address);
        } catch (Exception e) {
            log.error("更新地址失败", e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 删除地址
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteAddress(@RequestParam Long userId, @PathVariable Long id) {
        log.info("用户{}删除地址{}", userId, id);
        try {
            addressService.deleteAddress(userId, id);
            return Result.success();
        } catch (Exception e) {
            log.error("删除地址失败", e);
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 设置默认地址
     */
    @PutMapping("/{id}/default")
    public Result<Boolean> setDefaultAddress(@RequestParam Long userId, @PathVariable Long id) {
        log.info("用户{}设置默认地址{}", userId, id);
        try {
            boolean success = addressService.setDefaultAddress(userId, id);
            return Result.success(success);
        } catch (Exception e) {
            log.error("设置默认地址失败", e);
            return Result.error(e.getMessage());
        }
    }
} 