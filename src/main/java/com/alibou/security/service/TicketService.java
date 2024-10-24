package com.alibou.security.service;

import com.alibou.security.entity.Ticket;
import com.alibou.security.mapper.TicketMapper;
import com.alibou.security.model.request.TicketRequest;
import com.alibou.security.model.response.TicketResponse;
import com.alibou.security.repository.DiscountRepository;
import com.alibou.security.repository.ShowTimeRepository;
import com.alibou.security.repository.TicketRepository;
import com.alibou.security.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.internal.Logger;

import java.sql.Timestamp;
import java.util.List;

@Service
@Slf4j
public class TicketService {
    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    TicketMapper ticketMapper;
    @Autowired
    private ShowTimeRepository showTimeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DiscountRepository discountRepository;

    public TicketResponse createTicket(TicketRequest ticketRequest) {
//        if(ticketRepository.existsById(ticketRequest.getId())) {
//            throw new ApplicationContextException("Ticket already exists");
//        }

        Ticket ticket = ticketMapper.toTicket(ticketRequest);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        ticket.setCreatedAt(timestamp.toLocalDateTime());
        ticket.setUpdatedAt(timestamp.toLocalDateTime());

        try {
           ticket = ticketRepository.save(ticket);
           return ticketMapper.toTicketResponse(ticket);

        } catch (Exception e) {
            log.error("Error creating ticket", e);
            throw new ApplicationContextException("Error creating ticket", e);
        }
    }

    public TicketResponse updateTicket(long id,TicketRequest ticketRequest) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new ApplicationContextException("Ticket does not exist"));
         ticketMapper.updateTicket(ticket,ticketRequest);

         Timestamp timestamp = new Timestamp(System.currentTimeMillis());
         ticket.setUpdatedAt(timestamp.toLocalDateTime());

         return ticketMapper.toTicketResponse(ticketRepository.save(ticket));
    }

    public void deleteTicket(long id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new ApplicationContextException("Ticket does not exist"));
        ticketRepository.deleteById(id);
        showTimeRepository.deleteById(ticket.getId());
        userRepository.deleteById(Math.toIntExact(ticket.getId()));
        discountRepository.deleteById(ticket.getId());

    }

    public List<Ticket> getAllTicket() {
        return ticketRepository.findAll();
    }

    public TicketResponse getTicket(long id) {
        int n = 0;
        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new ApplicationContextException("Ticket does not exist"));

        return ticketMapper.toTicketResponse(ticket);
    }
}
