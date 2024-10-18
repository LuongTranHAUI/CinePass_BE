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

@Service
@RequiredArgsConstructor
public class NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(TheaterService.class);
    private final NotificationRepository repository;
    private final UserService userService;

    public void add(NotificationRequest request) {
        var notification = Notification.builder()
                .type(request.getType())
                .message(request.getMessage())
                .status(NotificationStatus.valueOf("UNREAD"))
                .createdBy(userService.getCurrentUserId())
                .createdAt(new Timestamp(System.currentTimeMillis()).toLocalDateTime())
                .build();
        repository.save(notification);
        logger.info("Notification added successfully: {}", notification);
    }

    public void change(NotificationRequest request) {
        var existingNotification = repository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
        var notification = Notification.builder()
                .id(existingNotification.getId())
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

    public void getNotificationById(Long id) {
        var notification = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
        logger.info("Retrieved notification successfully with ID: {}", id);
    }
}
