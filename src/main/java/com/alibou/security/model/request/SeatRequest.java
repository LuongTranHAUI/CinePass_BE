package com.alibou.security.model.request;

import com.alibou.security.enums.SeatStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatRequest {
    private Long id;

    private Integer seatNumber;

    private Character seatRow;

    private Integer seatPerRow;

    private SeatStatus status;

    private Long hallId;
}
