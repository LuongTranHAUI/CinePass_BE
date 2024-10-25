package com.alibou.security.model.request;

import com.alibou.security.enums.HallStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HallRequest {

    @NotNull
    private String name;
    @NotNull
    private Integer seatCapacity;
    @NotNull
    private Long theaterId;
    @NotNull
    private HallStatus status;
}
