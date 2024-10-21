package com.alibou.security.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PaymentMethodResponse {
    private Long id;
    private String name;
    private String description;
    private Long paymentMethodId;
}
