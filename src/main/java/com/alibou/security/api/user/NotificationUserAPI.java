package com.alibou.security.api.user;

import com.alibou.security.entity.Notification;
import com.alibou.security.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/notifications")
@RequiredArgsConstructor
public class NotificationUserAPI {

    private static final Logger logger = LoggerFactory.getLogger(InfoAPI.class);
    private final NotificationService service;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getNotificationsByUserId(@PathVariable Long userId) {
        try {
            List<Notification> notifications = service.findByUserId(userId);
            return ResponseEntity.ok(notifications); // 200 OK
        } catch (Exception e) {
            logger.error("Failed to retrieve notifications for user ID {}: {}", userId, e.getMessage());
            return ResponseEntity.status(500).body(null); // 500 Internal Server Error
        }
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(@PathVariable Long id) {
        try {
            service.markAsRead(id);
            return ResponseEntity.ok("Notification marked as read successfully");
        } catch (Exception e) {
            logger.error("Failed to mark notification as read", e);
            return ResponseEntity.status(500).body("Failed to mark notification as read");
        }
    }
}
