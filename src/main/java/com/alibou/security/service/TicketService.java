package com.alibou.security.service;

import com.alibou.security.entity.DiscountApplication;
import com.alibou.security.entity.Showtime;
import com.alibou.security.entity.Ticket;
import com.alibou.security.entity.User;
import com.alibou.security.enums.TicketStatus;
import com.alibou.security.mapper.TicketMapper;
import com.alibou.security.model.request.TicketRequest;
import com.alibou.security.model.response.TicketResponse;
import com.alibou.security.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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

    @Autowired
    DiscountApplicationRepository discountApplicationRepository;

    private static final Logger logger = LoggerFactory.getLogger(TicketService.class);
    @Autowired
    private UserService userService;

    public TicketResponse createTicket(TicketRequest ticketRequest) {
        if(ticketRepository.existsByShowtimeIdAndSeatNumber(ticketRequest.getShowtime_id(), ticketRequest.getSeatNumber())) {
            throw new ApplicationContextException("Ticket already exists");
        }

        Ticket ticket = ticketMapper.toTicket(ticketRequest);
        ticket.setId(null);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        ticket.setCreatedAt(timestamp.toLocalDateTime());
        ticket.setUpdatedAt(timestamp.toLocalDateTime());
        ticket.setCreatedBy(userService.getCurrentUserId());

        User user = userRepository.findById(Math.toIntExact(ticketRequest.getUser_id())).orElseThrow(() -> new ApplicationContextException("User not found"));
        ticket.setUser(user);

        Showtime showtime = showTimeRepository.findById(ticketRequest.getShowtime_id()).orElseThrow(() -> new ApplicationContextException("Showtime not found"));
        ticket.setShowtime(showtime);

        DiscountApplication discountApplication = discountApplicationRepository.findById(ticketRequest.getDiscount_application_id())
                .orElse(null);
        ticket.setDiscountApplication(discountApplication);
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
         ticket.setUpdatedBy(userService.getCurrentUserId());
         return ticketMapper.toTicketResponse(ticketRepository.save(ticket));
    }

    public void deleteTicket(long id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new ApplicationContextException("Ticket does not exist"));
        ticketRepository.deleteById(id);
        showTimeRepository.deleteById(ticket.getId());
        userRepository.deleteById(Math.toIntExact(ticket.getId()));
        discountRepository.deleteById(ticket.getId());

    }

    public List<TicketResponse> getAllTicket() {
        return ticketRepository.findAllTickets();
    }

    public TicketResponse getTicket(long id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new ApplicationContextException("Ticket does not exist"));

        return ticketMapper.toTicketResponse(ticket);
    }

    @Transactional
    @Scheduled(cron = "* * * * * ?")
    public ResponseEntity<List<TicketResponse>> CheckExpiredTicket() {
        List<TicketResponse> ticketResponses = ticketRepository.findAllTickets();
        List<TicketResponse> expiredTickets = new ArrayList<>();

        if(ticketResponses.isEmpty()) {
            logger.info("Tickets list is empty");
            return ResponseEntity.ok(Collections.emptyList());
        }

        LocalDateTime now = LocalDateTime.now();

        for(TicketResponse ticketResponse : ticketResponses) {
            if(ticketResponse == null || ticketResponse.getShowTime() == null){
                logger.warn("Ticket or Showtime is null, skipping ...");
               continue;
            }

            LocalDateTime showtime = ticketResponse.getShowTime();

            if(now.isBefore(showtime)){
                if(ticketResponse.getStatus() == TicketStatus.USED){
                    logger.info("Ticket is used");
                }else {
                    return ResponseEntity.ok(Collections.emptyList());
                }
            } else if (now.isAfter(showtime) && ticketResponse.getStatus() != TicketStatus.EXPIRED) {
                if(ticketResponse.getStatus() != TicketStatus.USED){

                    Ticket ticket = ticketRepository.findById(ticketResponse.getId()).orElseThrow(() -> new ApplicationContextException("Ticket does not exist"));
                     ticket.setStatus(TicketStatus.EXPIRED);
                     ticket.setUpdatedAt(LocalDateTime.now());
                     ticketRepository.save(ticket);
                     logger.info("Ticket with ID" + ticket.getId()+ " has been mark as EXPIRED");
                     expiredTickets.add(ticketMapper.toTicketResponse(ticket));

                }
            }
        }

        if(expiredTickets.isEmpty()){
            logger.info("Tickets list is empty");
            return ResponseEntity.ok(Collections.emptyList());
        }

        return ResponseEntity.ok(expiredTickets);
    }

    public TicketResponse CheckAndUpdateTicketStatus(long ticketId, TicketStatus status, long userId) {

       Ticket  ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new ApplicationContextException("Ticket does not exist"));

        if (status == TicketStatus.CANCELED || status == TicketStatus.REFUNDED) {
            if(ticket.getStatus() == TicketStatus.USED || ticket.getStatus() == TicketStatus.EXPIRED){

                    throw new IllegalStateException("Cannot cancel or refund ticket that is used or expired");
            }

            ticket.setStatus(status);
        } else if (status == TicketStatus.USED) {

            if (ticket.getStatus() != TicketStatus.PAID) {
                throw new IllegalStateException("Only pending ticket can be marked as used.");
            }
            ticket.setStatus(TicketStatus.USED);

        } else if (status == TicketStatus.PAID) {
            if (ticket.getStatus() != TicketStatus.PENDING) {
                throw new IllegalStateException("Only pending ticket can be marked as paid.");
            }
            ticket.setStatus(TicketStatus.PAID);
        }

        ticket.setUpdatedAt(LocalDateTime.now());
        ticket.setUpdatedBy(userId);
        ticketRepository.save(ticket);

        return ticketMapper.toTicketResponse(ticket);

    }

    public List<TicketResponse> getTicketByUserId(long userId) {
        return ticketRepository.findAllByUserId(userId);
    }
}
