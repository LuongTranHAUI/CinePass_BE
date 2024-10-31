package com.alibou.security.model.response;

import com.alibou.security.entity.DiscountApplication;
import com.alibou.security.entity.Showtime;
import com.alibou.security.entity.User;
import com.alibou.security.enums.TicketStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TicketResponse {

    @ToString.Exclude
    User user;
    @ToString.Exclude
    Showtime showtime;
    @ToString.Exclude
    DiscountApplication discountApplication;
    String seatNumber;
    String ticketType;
    BigDecimal serviceFee = BigDecimal.ZERO;
    TicketStatus status;
    LocalDateTime createdAt = LocalDateTime.now();
    LocalDateTime updatedAt = LocalDateTime.now();
    Long createdBy;
    Long updatedBy;

}
