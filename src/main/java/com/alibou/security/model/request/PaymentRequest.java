package com.alibou.security.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    private Long id;
    @NotNull
    private String transactionId;
    @NotNull
    private BigDecimal amount;
    @NotNull
    private String currency;
}
