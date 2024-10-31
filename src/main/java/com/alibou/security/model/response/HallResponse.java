package com.alibou.security.model.response;

import com.alibou.security.enums.HallStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HallResponse {
    private Long id;
    private String name;
    private Integer seatCapacity;
    private HallStatus status;
    private TheaterResponse theaterResponse;
}
