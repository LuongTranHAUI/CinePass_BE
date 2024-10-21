package com.alibou.security.model.response;

import com.alibou.security.entity.Hall;
import com.alibou.security.entity.Movie;
import com.alibou.security.entity.Theater;
import com.alibou.security.entity.Ticket;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShowtimeResponse {
    Movie movie;
    Theater theater;
    Hall hall;
    LocalDateTime showTime;
    Set<Ticket> tickets;
}
