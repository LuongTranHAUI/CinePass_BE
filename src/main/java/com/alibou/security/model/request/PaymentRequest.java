package com.alibou.security.model.request;

import com.alibou.security.entity.Ticket;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    private Long userId;
    private String transactionId;
    @NotNull
    private BigDecimal amount;
    @NotNull
    private String currency;
    @NotNull
    private Long paymentMethodId;
    @NotNull
    private Set<Long> ticketIds = new HashSet<>();
    private String bankCode;
    private String language;
}
