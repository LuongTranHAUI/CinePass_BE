package com.alibou.security.service;

import com.alibou.security.entity.Theater;
import com.alibou.security.model.request.TheaterRequest;
import com.alibou.security.model.response.TheaterResponse;
import com.alibou.security.repository.TheaterRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TheaterService {

    private static final Logger logger = LoggerFactory.getLogger(TheaterService.class);
    private final TheaterRepository repository;

    public TheaterResponse add(TheaterRequest request) {
        var theater = Theater.builder()
                .id(request.getId())
                .name(request.getName())
                .location(request.getLocation())
                .totalSeats(request.getTotalSeats())
                .build();
        repository.save(theater);
        logger.info("Theater added successfully: {}", theater);
        return null;
    }

    public TheaterResponse change(TheaterRequest request) {
        var existingTheater = repository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("Theater not found"));
        var theater = Theater.builder()
                .id(existingTheater.getId())
                .name(request.getName())
                .location(request.getLocation())
                .totalSeats(request.getTotalSeats())
                .build();
        repository.save(theater);
        logger.info("Theater updated successfully: {}", theater);
        return null;
    }

    public void delete(Integer id) {
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

    public Theater findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Theater not found"));
    }

    public TheaterResponse findByName(String name) {
        var theater = repository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Theater not found"));
        logger.info("Theater retrieved successfully: {}", name);
        return null;
    }
}