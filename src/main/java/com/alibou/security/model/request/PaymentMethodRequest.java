package com.alibou.security.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodRequest {
    @NotNull
    private String name;
    @NotNull
    private String description;
}
