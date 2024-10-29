package com.alibou.security.service;

import com.alibou.security.entity.Theater;
import com.alibou.security.model.request.TheaterRequest;
import com.alibou.security.model.response.TheaterResponse;
import com.alibou.security.repository.TheaterRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TheaterService {

    private static final Logger logger = LoggerFactory.getLogger(TheaterService.class);
    private final TheaterRepository repository;
    private final UserService userService;

    public TheaterResponse add(TheaterRequest request) {
        var existingTheater = repository.findByName(request.getName());
        if (existingTheater.isPresent()) {
            throw new IllegalArgumentException("Theater available");
        }
        var theater = Theater.builder()
                .name(request.getName())
                .location(request.getLocation())
                .phone(request.getPhone())
                .createdBy(userService.getCurrentUserId())
                .createdAt(new Timestamp(System.currentTimeMillis()).toLocalDateTime())
                .build();
        repository.save(theater);
        logger.info("Theater added successfully: {}", theater);
        return null;
    }

    public TheaterResponse change(TheaterRequest request, Long id) {
        var existingTheater = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Theater not found"));
        var theater = Theater.builder()
                .name(request.getName())
                .location(request.getLocation())
                .phone(request.getPhone())
                .updatedBy(userService.getCurrentUserId())
                .updatedAt(new Timestamp(System.currentTimeMillis()).toLocalDateTime())
                .build();
        repository.save(theater);
        logger.info("Theater updated successfully: {}", theater);
        return null;
    }

    public void delete(Long id) {
        var existingTheater = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Theater not found"));
        repository.deleteById(existingTheater.getId());
        logger.info("Theater deleted successfully: {}", id);
    }

    public List<Theater> findAll() {
        List<Theater> theaters = repository.findAll();
        logger.info("Theaters retrieved successfully");
        return theaters;
    }

    public Theater findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Theater not found"));
    }
}