package com.alibou.security.mapper;

import com.alibou.security.entity.MovieReview;
import com.alibou.security.model.request.MovieRequest;
import com.alibou.security.model.request.MovieReviewRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MovieReviewMapper {

    @Mapping(target = "id", ignore = true)
    MovieReview toMovieReview(MovieRequest request);

//    MovieResponse toMovieResponse(Movie movie);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateMovieReview(@MappingTarget MovieReview movieReview, MovieReviewRequest request);

    MovieReview toEntity(MovieReview request);
}
