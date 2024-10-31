package com.alibou.security.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscountRequest {

    @NotNull
    private String code;
    @NotNull
    private String description;
    @NotNull
    private BigDecimal discountPercent;
    @NotNull
    private Date expirationDate;
}
