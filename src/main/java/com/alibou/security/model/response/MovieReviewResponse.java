package com.alibou.security.model.response;

import com.alibou.security.entity.Movie;
import com.alibou.security.entity.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MovieReviewResponse {

//    @ToString.Exclude
//    User user;
//    @ToString.Exclude
//    Movie movie;
    Long userId;
    Long movieId;
    String content;
    Double rating;
    LocalDateTime createdAt = LocalDateTime.now();
    LocalDateTime updatedAt = LocalDateTime.now();
    Long createdBy;
    Long updatedBy;
}
