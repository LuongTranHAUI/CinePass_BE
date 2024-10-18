package com.alibou.security.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class SnackResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String type;
}
