package com.alibou.security.model.response;

import com.alibou.security.enums.HallStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class HallResponse {

    private Long id;
    private String name;
    private Integer seatCapacity;
    private Long theaterId;
    private HallStatus status;
}
