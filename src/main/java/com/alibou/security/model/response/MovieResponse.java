package com.alibou.security.model.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MovieResponse {

    String title;
    String director;
    String actor;
    int runTime;
    String genre;
    LocalDateTime releaseDate;
    String posterUrl;
    String thumbnailUrl;
    String summary;
    String trailerUrl;
    LocalDateTime createdAt;
    LocalDateTime updatedAt ;
    Long createdBy;
    Long updatedBy;
    Double rating;

    Set<ShowtimeResponse> showtimes;
    Set<MovieReviewResponse> reviews;

}
