package com.alibou.security.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SnackRequest {
    private String name;
    @NotNull
    private String description;
    @NotNull
    private BigDecimal price;
}
