package com.myster.demo.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.validation.constraints.*;

/**
 * 地址DTO
 * 
 * @author myster
 * @since 2025-07-06
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    
    @NotBlank(message = "收货人姓名不能为空")
    @Size(max = 32, message = "收货人姓名长度不能超过32个字符")
    private String name;
    
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    
    @NotBlank(message = "省份不能为空")
    @Size(max = 32, message = "省份长度不能超过32个字符")
    private String province;
    
    @NotBlank(message = "城市不能为空")
    @Size(max = 32, message = "城市长度不能超过32个字符")
    private String city;
    
    @NotBlank(message = "区县不能为空")
    @Size(max = 32, message = "区县长度不能超过32个字符")
    private String district;
    
    @NotBlank(message = "详细地址不能为空")
    @Size(max = 128, message = "详细地址长度不能超过128个字符")
    private String detail;
    
    @NotNull(message = "是否默认地址不能为空")
    @Min(value = 0, message = "是否默认地址值无效")
    @Max(value = 1, message = "是否默认地址值无效")
    private Integer isDefault;
} 