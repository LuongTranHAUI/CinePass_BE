package com.alibou.security.service;

import com.alibou.security.entity.Snack;
import com.alibou.security.model.request.SnackRequest;
import com.alibou.security.repository.SnackRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SnackService {
    private static final Logger logger = LoggerFactory.getLogger(TheaterService.class);
    private final SnackRepository repository;
    private final UserService userService;

    public List<Snack> findAll() {
        var snacks = repository.findAll();
        logger.info("Retrieved all snacks successfully");
        return snacks;
    }

    public void add(SnackRequest request) {
        var snack = Snack.builder()
                .name(request.getName())
                .description(request.getDescription())
                .type(request.getType())
                .price(request.getPrice())
                .createdBy(userService.getCurrentUserId())
                .createdAt(new Timestamp(System.currentTimeMillis()).toLocalDateTime())
                .build();
        repository.save(snack);
        logger.info("Snack added successfully: {}", snack);
    }

    public void change(SnackRequest request, Long id) {
        var existingSnack = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Snack not found"));
        var snack = Snack.builder()
                .name(request.getName())
                .description(request.getDescription())
                .type(request.getType())
                .price(request.getPrice())
                .updatedBy(userService.getCurrentUserId())
                .updatedAt(new Timestamp(System.currentTimeMillis()).toLocalDateTime())
                .build();
        repository.save(snack);
        logger.info("Snack updated successfully: {}", snack);
    }

    public void delete(Long id) {
        var existingSnack = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Snack not found"));
        repository.deleteById(existingSnack.getId());
        logger.info("Snack deleted successfully: {}", id);
    }
}
