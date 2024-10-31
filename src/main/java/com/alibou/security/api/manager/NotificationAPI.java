package com.alibou.security.api.manager;

import com.alibou.security.model.request.NotificationRequest;
import com.alibou.security.model.response.NotificationResponse;
import com.alibou.security.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/management/notifications")
@RequiredArgsConstructor
public class NotificationAPI {
    private static final Logger logger = LoggerFactory.getLogger(TheaterAPI.class);
    private final NotificationService service;

    @GetMapping
    public ResponseEntity<?> findAllNotifications() {
        try {
            List<NotificationResponse> notifications = service.findAll();
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            logger.error("Failed to retrieve notifications: {}", e.getMessage());
            return ResponseEntity.status(400).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<?> addNotification(@RequestBody NotificationRequest request) {
        try {
            NotificationResponse response = service.add(request);
            logger.info("Notification added successfully: {}", request);
            return ResponseEntity.status(201).body(response);
        } catch (Exception e) {
            logger.error("Failed to add notification: {}", e.getMessage());
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> changeNotification(@RequestBody NotificationRequest request, @PathVariable Long id) {
        try {
            NotificationResponse response = service.change(request, id);
            logger.info("Notification updated successfully: {}", request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to update notification: {}", e.getMessage());
            return ResponseEntity.status(400).body("Missing or invalid request body");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotification(@PathVariable Long id) {
        try {
            service.delete(id);
            logger.info("Notification deleted successfully with ID: {}", id);
            return ResponseEntity.status(204).body(null);
        } catch (Exception e) {
            logger.error("Failed to delete notification with ID: {}", e.getMessage());
            return ResponseEntity.status(400).body("Error deleting notification");
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        return ResponseEntity.status(500).body(e.getMessage());
    }
}
