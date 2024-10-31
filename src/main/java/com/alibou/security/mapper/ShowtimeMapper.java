package com.alibou.security.mapper;

import com.alibou.security.entity.Showtime;
import com.alibou.security.model.request.ShowtimeRequest;
import com.alibou.security.model.response.ShowtimeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ShowtimeMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "movie.id", source = "movieId")
    @Mapping(target = "theater.id", source = "theaterId")
    @Mapping(target = "hall.id", source = "hallId")
    Showtime toShowtime(ShowtimeRequest request);

    ShowtimeResponse toshowtimeResponse (Showtime showtime);

    @Mapping(target = "id", ignore = true)
    void updateShowtime(@MappingTarget Showtime showtime, ShowtimeRequest request);
}
