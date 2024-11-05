package com.alibou.security.api.user;

import com.alibou.security.entity.Ticket;
import com.alibou.security.enums.TicketStatus;
import com.alibou.security.model.request.TicketRequest;
import com.alibou.security.model.response.TicketResponse;
import com.alibou.security.repository.TicketRepository;
import com.alibou.security.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/ticket")
@RequiredArgsConstructor
@Slf4j
public class TicketAPI {

    private final TicketService ticketService;
    private final Logger logger = LoggerFactory.getLogger(TicketAPI.class);
    private final TicketRepository ticketRepository;

    @PostMapping
    public ResponseEntity<?> createTick(@RequestBody TicketRequest ticketRequest) {
        try {

            TicketResponse ticketResponse = ticketService.createTicket(ticketRequest);
            logger.info("Ticket created: {}", ticketResponse);
            return ResponseEntity.ok(ticketResponse);

        } catch (Throwable e) {
            logger.error("Error saving ticket.", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets() {
        try {
            List<Ticket> tickets = ticketService.getAllTicket();
            return ResponseEntity.ok(tickets);
        } catch (Throwable e) {
            logger.error("Error getting all tickets.", e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTicket(@PathVariable long id) {
        try {

            TicketResponse ticketResponse = ticketService.getTicket(id);
            logger.info("Ticket found: {}", ticketResponse);
            return ResponseEntity.status(200).body(ticketResponse);

        } catch (Throwable e) {
            logger.error("Error getting ticket with id: {}", e.getMessage(), id);
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTicket(@PathVariable long id, @RequestBody TicketRequest ticketRequest) {
        try {
            if (ticketRequest == null) {
                throw new IllegalArgumentException("Missing required fields");
            }

            TicketResponse ticketResponse = ticketService.updateTicket(id, ticketRequest);
            logger.info("Ticket updated: {}", ticketResponse);
            return ResponseEntity.ok(ticketResponse);
        } catch (Throwable e) {
            logger.error("Error saving ticket with id: {}", e.getMessage(), id);
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTicket(@PathVariable long id) {
        try {
            ticketService.deleteTicket(id);
            logger.info("Ticket deleted: {}", id);
            return ResponseEntity.status(200).body(null);
        } catch (Exception e) {
            logger.error("Delete ticket failed.", e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/expired")
    public ResponseEntity<?> getExpiredTicket( @RequestHeader("UserId") Long userId) {
//        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new ApplicationContextException("Ticket not found"));
            List<Ticket> tickets = ticketService.getTicketByUserId(userId);
        for (Ticket ticket : tickets) {
            ticketService.CheckExpiredTicket(ticket, userId);
        }
        return ResponseEntity.ok(tickets);
//        if (ticket.getStatus() == TicketStatus.EXPIRED) {
//            return ResponseEntity.ok("Ticket expired");
//        } else if (ticket.getStatus() == TicketStatus.USED) {
//            return ResponseEntity.ok("Ticket used");
//        } else {
//            return ResponseEntity.ok("Ticket is available");
//        }

//        return ResponseEntity.ok(ticketMapper.toTicketResponse(ticket));
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateTicketStatus(@PathVariable long id, @RequestBody TicketStatus status, @RequestHeader("UserId") Long userId) {

        TicketResponse response = ticketService.CheckAndUpdateTicketStatus(id, status, userId);

        return ResponseEntity.ok("Update ticket status is successful");
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<?> getTicketHistory(@PathVariable long userId) {
        List<Ticket> tickets = ticketService.getTicketByUserId(userId);
        if (tickets.isEmpty()) {
            return ResponseEntity.ok("Ticket not found");
        }
        return ResponseEntity.ok(tickets);
    }

}
