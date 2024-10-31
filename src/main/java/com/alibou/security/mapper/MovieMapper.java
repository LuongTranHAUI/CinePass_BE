package com.alibou.security.mapper;

import com.alibou.security.entity.Movie;
import com.alibou.security.model.request.MovieRequest;
import com.alibou.security.model.response.MovieResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {ShowtimeMapper.class, MovieReviewMapper.class})
public interface MovieMapper {
    @Mapping(target = "id", ignore = true)
    Movie toMovie(MovieRequest request);

    @Mapping(target = "showtimes", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    MovieResponse toMovieResponse(Movie movie);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateMovie(@MappingTarget Movie movies, MovieRequest request);
}
