package com.alibou.security.api.manager;

import com.alibou.security.entity.Seat;
import com.alibou.security.model.request.SeatRequest;
import com.alibou.security.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/management/seats")
@RequiredArgsConstructor
public class SeatAPI {
    private static final Logger logger = LoggerFactory.getLogger(SeatAPI.class);
    private final SeatService service;

    @GetMapping
    public ResponseEntity<List<Seat>> findAllSeats() {
        try {
            List<Seat> seats = service.findAll();
            logger.info("Retrieved all seats successfully");
            return ResponseEntity.ok(seats); // 200 OK
        } catch (Exception e) {
            logger.error("Failed to retrieve seats: {}", e.getMessage());
            return ResponseEntity.status(500).body(null); // 500 Internal Server Error
        }
    }

    @PostMapping
    public ResponseEntity<?> generateSeats(@RequestBody SeatRequest request) {
        try {
            service.generate(request.getHallId(), request.getSeatPerRow());
            logger.info("Seats generated successfully: {}", request);
            return ResponseEntity.ok(request); // 200 OK
        } catch (Exception e) {
            logger.error("Failed to generate seats: {}", e.getMessage());
            return ResponseEntity.status(500).body(null); // 500 Internal Server Error
        }
    }

    @PutMapping
    public ResponseEntity<?> changeSeat(@RequestBody SeatRequest request) {
        try {
            service.change(request);
            logger.info("Seat changed successfully: {}", request);
            return ResponseEntity.ok(request); // 200 OK
        } catch (Exception e) {
            logger.error("Failed to change seat: {}", e.getMessage());
            return ResponseEntity.status(500).body(null); // 500 Internal Server Error
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSeat(@PathVariable Long id) {
        try {
            service.delete(id);
            logger.info("Seat deleted successfully with ID: {}", id);
            return ResponseEntity.ok(id); // 200 OK
        } catch (Exception e) {
            logger.error("Failed to delete seat with ID: {}", e.getMessage());
            return ResponseEntity.status(500).body(null); // 500 Internal Server Error
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAllSeats() {
        try {
            service.deleteAll();
            logger.info("All seats deleted successfully");
            return ResponseEntity.ok(null); // 200 OK
        } catch (Exception e) {
            logger.error("Failed to delete all seats: {}", e.getMessage());
            return ResponseEntity.status(500).body(null); // 500 Internal Server Error
        }
    }
}
