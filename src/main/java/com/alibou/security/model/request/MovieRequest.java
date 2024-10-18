package com.alibou.security.model.request;

import com.alibou.security.entity.MovieReview;
import com.alibou.security.entity.Showtime;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Data
@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MovieRequest {

    Long id;
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
    Set<MovieReview> reviews;
    Set<Showtime> showtimes;
}
