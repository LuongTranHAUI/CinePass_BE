package com.alibou.security.model.request;

import com.alibou.security.entity.Ticket;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShowtimeRequest {
    long id;
    Long movieId;
    Long theaterId;
    Long hallId;
    LocalDateTime showTime;
    Set<Ticket> tickets;
}
