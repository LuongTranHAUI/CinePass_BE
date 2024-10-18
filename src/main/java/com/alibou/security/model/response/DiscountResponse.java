package com.alibou.security.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class DiscountResponse {
    private Long id;
    private String code;
    private String description;
    private BigDecimal discountPercent;
    private LocalDateTime expirationDate;
}
