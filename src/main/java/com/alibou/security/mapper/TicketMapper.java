package com.alibou.security.mapper;

import com.alibou.security.entity.Ticket;
import com.alibou.security.model.request.TicketRequest;
import com.alibou.security.model.response.TicketResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TicketMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "showtime.id",source = "showtime_id")
    @Mapping(target = "user.id", source = "user_id")
    @Mapping(target = "discountApplication.id", source = "discount_application_id")
    Ticket toTicket(TicketRequest ticketRequest);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "discountApplication", ignore = true)
//    @Mapping(target = "showtime", ignore = true)
    @Mapping(target = "showTime", source = "showtime.showTime")
    @Mapping(target = "movieTitle", source = "showtime.movie.title")
    @Mapping(target = "theaterName", source = "showtime.theater.name")
    @Mapping(target = "hallName", source = "showtime.hall.name")
    TicketResponse toTicketResponse(Ticket ticket);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateTicket(@MappingTarget Ticket ticket, TicketRequest ticketRequest);
}
