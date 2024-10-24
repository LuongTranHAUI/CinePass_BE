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
    Ticket toTicket(TicketRequest ticketRequest);

    TicketResponse toTicketResponse(Ticket ticket);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createAt", ignore = true)
    void updateTicket(@MappingTarget Ticket ticket, TicketRequest ticketRequest);
}
