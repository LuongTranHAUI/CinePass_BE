package com.alibou.security.api.manager;

import com.alibou.security.entity.Snack;
import com.alibou.security.model.request.SnackRequest;
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
    public ResponseEntity<List<Snack>> findAllSnacks() {
        try {
            List<Snack> snacks = service.findAll();
            logger.info("Retrieved all snacks successfully");
            return ResponseEntity.ok(snacks); // 200 OK
        } catch (Exception e) {
            logger.error("Failed to retrieve snacks: {}", e.getMessage());
            return ResponseEntity.status(500).body(null); // 500 Internal Server Error
        }
    }

    @PostMapping
    public ResponseEntity<?> addSnack(@RequestBody SnackRequest request) {
        try {
            service.add(request);
            logger.info("Snack added successfully: {}", request);
            return ResponseEntity.status(201).body(request);
        } catch (Exception e) {
            logger.error("Failed to add snack: {}", e.getMessage());
            return ResponseEntity.status(500).body(null); // 500 Internal Server Error
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> changeSnack(@RequestBody SnackRequest request, @PathVariable Long id) {
        try {
            service.change(request, id);
            logger.info("Snack updated successfully: {}", request);
            return ResponseEntity.ok(request); // 200 OK
        } catch (Exception e) {
            logger.error("Failed to update snack: {}", e.getMessage());
            return ResponseEntity.status(500).body(null); // 500 Internal Server Error
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSnack(@PathVariable Long id) {
        try {
            service.delete(id);
            logger.info("Snack deleted successfully: {}", id);
            return ResponseEntity.ok(null); // 200 OK
        } catch (Exception e) {
            logger.error("Failed to delete snack: {}", e.getMessage());
            return ResponseEntity.status(500).body(null); // 500 Internal Server Error
        }
    }

}
