package com.alibou.security.mapper;

import com.alibou.security.entity.MovieReview;
import com.alibou.security.model.request.MovieReviewRequest;
import com.alibou.security.model.response.MovieReviewResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MovieReviewMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "movie.id", source = "movieId")
    MovieReview toMovieReview(MovieReviewRequest request);

//    MovieResponse toMovieResponse(Movie movie);
    @Mapping(target = "username", source = "movieReview.user.username")
    @Mapping(target = "movieTitle", source = "movieReview.movie.title")
    MovieReviewResponse toMovieReviewResponse(MovieReview movieReview);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateMovieReview(@MappingTarget MovieReview movieReview, MovieReviewRequest request);

    MovieReview toEntity(MovieReview request);
}
