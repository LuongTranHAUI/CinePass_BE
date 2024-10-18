package com.alibou.security.service;

import com.alibou.security.entity.Snack;
import com.alibou.security.model.request.SnackRequest;
import com.alibou.security.repository.SnackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class SnackService {
    private static final Logger LOGGER = Logger.getLogger(SnackService.class.getName());
    private final SnackRepository repository;
    private final UserService userService;

    public Snack add(SnackRequest request) {
        var existingSnack = repository.findByName(request.getName());
        if (existingSnack.isPresent()) {
            throw new IllegalArgumentException("Snack available");
        }
        var snack = Snack.builder()
                .name(request.getName())
                .price(request.getPrice())
                .description(request.getDescription())
                .type(request.getType())
                .createdBy(userService.getCurrentUserId())
                .createdAt(new Timestamp(System.currentTimeMillis()).toLocalDateTime())
                .build();
        repository.save(snack);
        LOGGER.info("Snack added successfully: " + snack);
        return snack;
    }
    public Snack change(SnackRequest request) {
        var existingSnack = repository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("Snack not found"));
        var snack = Snack.builder()
                .id(existingSnack.getId())
                .name(request.getName())
                .price(request.getPrice())
                .description(request.getDescription())
                .type(request.getType())
                .updatedBy(userService.getCurrentUserId())
                .updatedAt(new Timestamp(System.currentTimeMillis()).toLocalDateTime())
                .build();
        repository.save(snack);
        LOGGER.info("Snack updated successfully: " + snack);
        return snack;
    }

    public void delete(Long id) {
        var existingSnack = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Snack not found"));
        repository.deleteById(existingSnack.getId());
        LOGGER.info("Snack deleted successfully: " + id);
    }

    public List<Snack> findAll() {
        return repository.findAll();
    }
}
