package com.alibou.security.model.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShowtimeRequest {
    Long movieId;
    Long theaterId;
    Long hallId;
    LocalDateTime showTime;
}
