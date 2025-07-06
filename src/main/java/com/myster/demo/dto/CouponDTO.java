package com.myster.demo.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 优惠券DTO
 * 
 * @author myster
 * @since 2025-07-06
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponDTO {
    
    private Long id;
    
    @NotBlank(message = "优惠券名称不能为空")
    @Size(max = 64, message = "优惠券名称长度不能超过64个字符")
    private String name;
    
    @NotBlank(message = "优惠券类型不能为空")
    @Pattern(regexp = "^(discount|cash)$", message = "优惠券类型只能是discount或cash")
    private String type;
    
    @NotNull(message = "优惠金额不能为空")
    @DecimalMin(value = "0.01", message = "优惠金额必须大于0")
    private BigDecimal value;
    
    @NotNull(message = "满减门槛不能为空")
    @DecimalMin(value = "0.00", message = "满减门槛不能小于0")
    private BigDecimal minAmount;
    
    @NotNull(message = "发放总量不能为空")
    @Min(value = 0, message = "发放总量不能小于0")
    private Integer totalCount;
    
    @NotNull(message = "生效时间不能为空")
    private LocalDateTime startTime;
    
    @NotNull(message = "失效时间不能为空")
    private LocalDateTime endTime;
    
    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = "状态值无效")
    @Max(value = 1, message = "状态值无效")
    private Integer status;
    
    @Size(max = 255, message = "描述长度不能超过255个字符")
    private String description;
} 