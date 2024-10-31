package com.alibou.security.api.manager;

import com.alibou.security.model.request.SnackRequest;
import com.alibou.security.model.response.SnackResponse;
import com.alibou.security.service.SnackService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/management/snacks")
@RequiredArgsConstructor
public class SnackAPI {
    private static final Logger logger = LoggerFactory.getLogger(TheaterAPI.class);
    private final SnackService service;

    @GetMapping
    public ResponseEntity<List<SnackResponse>> findAllSnacks() {
        try {
            List<SnackResponse> snacks = service.findAll();
            logger.info("Retrieved all snacks successfully");
            return ResponseEntity.ok(snacks);
        } catch (Exception e) {
            logger.error("Failed to retrieve snacks: {}", e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<?> addSnack(@RequestBody SnackRequest request) {
        try {
            SnackResponse response = service.add(request);
            logger.info("Snack added successfully: {}", request);
            return ResponseEntity.status(201).body(response);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to delete hall with ID: {}", e.getMessage());
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Failed to add snack: {}", e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> changeSnack(@RequestBody SnackRequest request, @PathVariable Long id) {
        try {
            SnackResponse response = service.change(request, id);
            logger.info("Snack updated successfully: {}", response);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to delete hall with ID: {}", e.getMessage());
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Failed to delete theater with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSnack(@PathVariable Long id) {
        try {
            service.delete(id);
            logger.info("Snack deleted successfully: {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Failed to delete snack: {}", e.getMessage());
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
