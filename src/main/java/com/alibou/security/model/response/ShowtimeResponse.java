package com.alibou.security.model.response;

import com.alibou.security.entity.Hall;
import com.alibou.security.entity.Movie;
import com.alibou.security.entity.Theater;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
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
}
