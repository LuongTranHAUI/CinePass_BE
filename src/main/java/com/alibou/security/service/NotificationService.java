package com.alibou.security.service;

import com.alibou.security.entity.Notification;
import com.alibou.security.enums.NotificationStatus;
import com.alibou.security.model.request.NotificationRequest;
import com.alibou.security.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(TheaterService.class);
    private final NotificationRepository repository;
    private final UserService userService;

    public void findAll() {
        var notifications = repository.findAll();
        logger.info("Notifications retrieved successfully: {}", notifications);
    }

    public List<Notification> findByUserId(Long userId) {
        var notifications = repository.findByUserId(userId);
        if (notifications.isEmpty()) {
            throw new IllegalArgumentException("No notifications found for user ID: " + userId);
        }
        logger.info("Notifications retrieved successfully for user ID {}: {}", userId, notifications);
        return notifications;
    }

    public void add(NotificationRequest request) {
        var notification = Notification.builder()
                .type(request.getType())
                .message(request.getMessage())
                .status(NotificationStatus.valueOf("UNREAD"))
                .user(userService.getUserById(request.getUserId()))
                .createdBy(userService.getCurrentUserId())
                .createdAt(new Timestamp(System.currentTimeMillis()).toLocalDateTime())
                .build();
        repository.save(notification);
        logger.info("Notification added successfully: {}", notification);
    }

    public void change(NotificationRequest request, Long id) {
        var existingNotification = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
        var notification = Notification.builder()
                .type(request.getType())
                .status(request.getStatus())
                .message(request.getMessage())
                .updatedBy(userService.getCurrentUserId())
                .updatedAt(new Timestamp(System.currentTimeMillis()).toLocalDateTime())
                .build();
        repository.save(notification);
        logger.info("Notification updated successfully: {}", notification);
    }

    public void delete(Long id) {
        var existingNotification = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
        repository.deleteById(existingNotification.getId());
        logger.info("Notification deleted successfully: {}", id);
    }

    public void markAsRead(Long id) {
        var existingNotification = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
        existingNotification.setStatus(NotificationStatus.valueOf("READ"));
        repository.save(existingNotification);
        logger.info("Notification marked as read successfully: {}", id);
    }
}
