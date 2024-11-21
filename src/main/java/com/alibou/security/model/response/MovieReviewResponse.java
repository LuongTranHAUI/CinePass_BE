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
    Long id;
    String username;
    String movieTitle;
    String content;
    Double rating;
    LocalDateTime createdAt = LocalDateTime.now();
    LocalDateTime updatedAt = LocalDateTime.now();
    Long createdBy;
    Long updatedBy;

    public MovieReviewResponse(Long id, String content, LocalDateTime createdAt, Long createdBy, Double rating,
                               LocalDateTime updatedAt, Long updatedBy, String movieTitle, String username) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.rating = rating;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
        this.movieTitle = movieTitle;
        this.username = username;
    }
}
