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
    private static final Logger logger = LoggerFactory.getLogger(SnackAPI.class);
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
            Snack addedSnack = service.add(request);
            logger.info("Added snack successfully: {}", addedSnack);
            return ResponseEntity.ok(addedSnack); // 200 OK
        } catch (Exception e) {
            logger.error("Failed to add snack: {}", e.getMessage());
            return ResponseEntity.status(500).body(null); // 500 Internal Server Error
        }
    }

    @PutMapping
    public ResponseEntity<?> changeSnack(@RequestBody SnackRequest request) {
        try {
            Snack changedSnack = service.change(request);
            logger.info("Changed snack successfully: {}", changedSnack);
            return ResponseEntity.ok(changedSnack); // 200 OK
        } catch (Exception e) {
            logger.error("Failed to change snack: {}", e.getMessage());
            return ResponseEntity.status(500).body(null); // 500 Internal Server Error
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSnack(@PathVariable Long id) {
        try {
            service.delete(id);
            logger.info("Deleted snack successfully with ID: {}", id);
            return ResponseEntity.ok(null); // 200 OK
        } catch (Exception e) {
            logger.error("Failed to delete snack with ID: {}", e.getMessage());
            return ResponseEntity.status(500).body(null); // 500 Internal Server Error
        }
    }
}
