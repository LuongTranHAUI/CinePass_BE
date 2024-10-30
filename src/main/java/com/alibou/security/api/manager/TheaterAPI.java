package com.alibou.security.api.manager;

import com.alibou.security.model.request.TheaterRequest;
import com.alibou.security.model.response.TheaterResponse;
import com.alibou.security.service.TheaterService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/management/theaters")
@RequiredArgsConstructor
public class TheaterAPI {

    private static final Logger logger = LoggerFactory.getLogger(TheaterAPI.class);
    private final TheaterService service;

    @GetMapping
    public ResponseEntity<List<TheaterResponse>> findAllTheaters() {
        try {
            List<TheaterResponse> theaters = service.findAll();
            logger.info("Retrieved all theaters successfully");
            return ResponseEntity.ok(theaters);
        } catch (Exception e) {
            logger.error("Failed to retrieve theaters: {}", e.getMessage());
            return ResponseEntity.status(400).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<?> addTheater(@RequestBody TheaterRequest request) {
        try {
            TheaterResponse response = service.add(request);
            logger.info("Theater saved successfully: {}", request);
            return ResponseEntity.status(201).body(response);
        } catch (Exception e) {
            logger.error("Failed to save theater: {}", e.getMessage());
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> changeTheater(@RequestBody TheaterRequest request, @PathVariable Long id) {
        try {
            TheaterResponse response = service.change(request, id);
            logger.info("Updated theater successfully: {}", request);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            logger.error("Failed to update theater: {}", e.getMessage());
            return ResponseEntity.status(400).body("Missing or invalid request body");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTheater(@PathVariable Long id) {
        try {
            service.delete(id);
            logger.info("Deleted theater successfully with ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Failed to delete theater with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(400).body("Error deleting theater");
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        logger.error("Unhandled exception occurred: {}", e.getMessage());
        return ResponseEntity.status(500).body("Internal server error");
    }
}
