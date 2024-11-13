package com.alibou.security.model.request;

import com.alibou.security.entity.MovieReview;
import com.alibou.security.entity.Showtime;
import com.alibou.security.model.response.MovieReviewResponse;
import com.alibou.security.model.response.ShowtimeResponse;
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
    String director;
    String actor;
    int runTime;
    String genre;
    Date releaseDate;
    String posterUrl;
    String thumbnailUrl;
    String summary;
    String trailerUrl;
    LocalDateTime createdAt;
    LocalDateTime updatedAt ;
    Long createdBy;
    Long updatedBy;
    Double rating;
    Long showtimeId;
    Long movieReviewId;
//    Set<MovieReview> reviews;
//    Set<Showtime> showtimes;

}
