package com.alibou.security.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class PaymentResponse {
    private Long id;
    private String transactionId;
    private BigDecimal amount;
    private String currency;
}
