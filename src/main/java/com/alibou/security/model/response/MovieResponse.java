package com.alibou.security.model.response;

import com.alibou.security.entity.MovieReview;
import com.alibou.security.entity.Showtime;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MovieResponse {

    String title;
    String description;
    int duration;
    String genre;
    Date releaseDate;
    String posterUrl;
    String country;
    String summary;
    String trailerUrl;
    LocalDateTime createdAt;
    LocalDateTime updatedAt ;
    Long createdBy;
    Long updatedBy;
    Double rating;
//    MovieReview review;
//    Showtime showtimes;
    Set<MovieReview> reviews;
    Set<Showtime> showtimes;
}
