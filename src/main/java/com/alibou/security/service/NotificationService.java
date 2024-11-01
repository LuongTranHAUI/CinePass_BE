package com.alibou.security.service;

import com.alibou.security.config.GeneralMapper;
import com.alibou.security.entity.Notification;
import com.alibou.security.enums.NotificationStatus;
import com.alibou.security.model.request.NotificationRequest;
import com.alibou.security.model.response.NotificationResponse;
import com.alibou.security.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(TheaterService.class);
    private final NotificationRepository repository;
    private final UserService userService;
    private final GeneralMapper generalMapper;

    public NotificationResponse add(NotificationRequest request) {
        var notification = generalMapper.mapToEntity(request, Notification.class);
        notification.setStatus(NotificationStatus.valueOf("UNREAD"));
        notification.setUser(UserService.getCurrentUser());
        notification.setCreatedBy(userService.getCurrentUserId());
        notification.setCreatedAt(LocalDateTime.now());
        repository.save(notification);
        logger.info("Notification added successfully: {}", notification);
        return generalMapper.mapToDTO(notification, NotificationResponse.class);
    }

    public NotificationResponse change(NotificationRequest request, Long id) {
        var existingNotification = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
        generalMapper.mapToEntity(request, existingNotification);
        existingNotification.setStatus(NotificationStatus.valueOf("UNREAD"));
        existingNotification.setUser(UserService.getCurrentUser());
        existingNotification.setCreatedBy(userService.getCurrentUserId());
        existingNotification.setCreatedAt(LocalDateTime.now());
        repository.save(existingNotification);
        logger.info("Notification updated successfully: {}", existingNotification);
        return generalMapper.mapToDTO(existingNotification, NotificationResponse.class);
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

    public List<NotificationResponse> findAll() {
        List<Notification> notifications = repository.findAll();
        logger.info("Notifications retrieved successfully: {}", notifications);
        return notifications.stream()
                .map(notification -> generalMapper.mapToDTO(notification, NotificationResponse.class))
                .toList();
    }

    public List<NotificationResponse> findByUserId(Long userId) {
        List<Notification> notifications = repository.findByUserId(userId);
        logger.info("Notifications retrieved successfully for user ID: {}", userId);
        return notifications.stream()
                .map(notification -> generalMapper.mapToDTO(notification, NotificationResponse.class))
                .toList();
    }
}
