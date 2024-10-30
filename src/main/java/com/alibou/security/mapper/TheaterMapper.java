package com.alibou.security.mapper;

import com.alibou.security.entity.Theater;
import com.alibou.security.model.request.TheaterRequest;
import com.alibou.security.model.response.TheaterResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TheaterMapper {

    Theater toTheater(TheaterRequest request);
    TheaterResponse toTheaterResponse(Theater theater);
}
