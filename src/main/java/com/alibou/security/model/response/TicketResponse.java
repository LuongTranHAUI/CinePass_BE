package com.alibou.security.model.response;

import com.alibou.security.entity.DiscountApplication;
import com.alibou.security.entity.Showtime;
import com.alibou.security.entity.User;
import com.alibou.security.enums.TicketStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    User user;
//    @ToString.Exclude
////    @JsonIgnore
//    Showtime showtime;
    @JsonIgnore
    @ToString.Exclude
    DiscountApplication discountApplication;
    Long id;
    String seatNumber;
    String ticketType;
    BigDecimal price = BigDecimal.ZERO;
    BigDecimal serviceFee = BigDecimal.ZERO;
    TicketStatus status;
    LocalDateTime createdAt = LocalDateTime.now();
    LocalDateTime updatedAt = LocalDateTime.now();
    Long createdBy;
    Long updatedBy;

    LocalDateTime showTime;
    String movieTitle;
    String theaterName;
    String hallName;

    public TicketResponse(Long id, String seatNumber, String ticketType, BigDecimal price, BigDecimal serviceFee,
                          TicketStatus status, LocalDateTime createdAt, LocalDateTime updatedAt, Long createdBy,
                          Long updatedBy, LocalDateTime showTime, String movieTitle, String theaterName,
                          String hallName) {
        this.id = id;
        this.seatNumber = seatNumber;
        this.ticketType = ticketType;
        this.price = price;
        this.serviceFee = serviceFee;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.showTime = showTime;
        this.movieTitle = movieTitle;
        this.theaterName = theaterName;
        this.hallName = hallName;
    }
}
