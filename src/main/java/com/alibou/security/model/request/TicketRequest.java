package com.alibou.security.model.request;

import com.alibou.security.entity.DiscountApplication;
import com.alibou.security.entity.Showtime;
import com.alibou.security.entity.User;
import com.alibou.security.enums.TicketStatus;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class TicketRequest {
    Long id;
    Long showtime_id;
    Long user_id;
    Long discount_application_id;
//    User user;
//    Showtime showtime;
//    DiscountApplication discountApplication;
    String seatNumber;
    String ticketType;
    BigDecimal price = BigDecimal.ZERO;
    BigDecimal serviceFee = BigDecimal.ZERO;
    TicketStatus status;
    LocalDateTime createdAt = LocalDateTime.now();
    LocalDateTime updatedAt = LocalDateTime.now();
    Long createdBy;
    Long updatedBy;

}
