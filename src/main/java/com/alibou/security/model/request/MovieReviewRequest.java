package com.alibou.security.model.request;

import com.alibou.security.entity.Movie;
import com.alibou.security.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)

public class MovieReviewRequest {
//    Long id;
//    User user;
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
