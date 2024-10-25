package com.alibou.security.api.manager;

import com.alibou.security.model.request.NotificationRequest;
import com.alibou.security.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/management/notifications")
@RequiredArgsConstructor
public class NotificationAPI {
    private static final Logger logger = LoggerFactory.getLogger(TheaterAPI.class);
    private final NotificationService service;

    @GetMapping
    public ResponseEntity<?> getNotifications() {
        try {
            service.findAll();
            return ResponseEntity.ok().build(); // 200 OK
        } catch (Exception e) {
            logger.error("Failed to retrieve notifications: {}", e.getMessage());
            return ResponseEntity.status(500).body(null); // 500 Internal Server Error
        }
    }

    @PostMapping
    public ResponseEntity<?> addNotification(@RequestBody NotificationRequest request) {
        try {
            service.add(request);
            logger.info("Notification added successfully: {}", request);
            return ResponseEntity.ok().build(); // 200 OK
        } catch (Exception e) {
            logger.error("Failed to add notification: {}", e.getMessage());
            return ResponseEntity.status(500).body(null); // 500 Internal Server Error
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> changeNotification(@RequestBody NotificationRequest request, @PathVariable Long id) {
        try {
            service.change(request, id);
            logger.info("Notification updated successfully: {}", request);
            return ResponseEntity.ok().build(); // 200 OK
        } catch (Exception e) {
            logger.error("Failed to update notification: {}", e.getMessage());
            return ResponseEntity.status(500).body(null); // 500 Internal Server Error
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotification(@PathVariable Long id) {
        try {
            service.delete(id);
            logger.info("Notification deleted successfully with ID: {}", id);
            return ResponseEntity.ok().build(); // 200 OK
        } catch (Exception e) {
            logger.error("Failed to delete notification with ID: {}", e.getMessage());
            return ResponseEntity.status(500).body(null); // 500 Internal Server Error
        }
    }
}
