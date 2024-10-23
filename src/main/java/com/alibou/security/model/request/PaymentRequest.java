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
    private String transactionId;
    @NotNull
    private BigDecimal amount;
    @NotNull
    private String currency;
    @NotNull
    private Long paymentMethodId;
    private String bankCode;
    private String language;
}
