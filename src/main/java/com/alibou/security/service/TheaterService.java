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
        try {
            var theater = Theater.builder()
                    .id(request.getId())
                    .name(request.getName())
                    .location(request.getLocation())
                    .totalSeats(request.getTotalSeats())
                    .build();
            repository.save(theater);
            logger.info("Theater added successfully: {}", theater);
        } catch (Exception e) {
            logger.error("Error saving theater: {}", e.getMessage());
            throw e;
        }
        return null;
    }


    public TheaterResponse change(TheaterRequest request) {
        try {
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
        } catch (Exception e) {
            logger.error("Error updating theater: {}", e.getMessage());
            throw e;
        }
        return null;
    }

    public void delete(Integer id) {
        try {
            var existingTheater = repository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Theater not found"));
            repository.deleteById(existingTheater.getId());
            logger.info("Theater deleted successfully: {}", id);
        } catch (Exception e) {
            logger.error("Error deleting theater: {}", e.getMessage());
            throw e;
        }
    }

    public List<Theater> findAll() {
        try {
            List<Theater> theaters = repository.findAll();
            logger.info("Theaters retrieved successfully");
            return theaters;
        } catch (Exception e) {
            logger.error("Error retrieving theaters: {}", e.getMessage());
            throw e;
        }
    }
}
