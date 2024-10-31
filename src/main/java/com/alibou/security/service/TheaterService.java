package com.alibou.security.service;

import com.alibou.security.config.GeneralMapper;
import com.alibou.security.entity.Theater;
import com.alibou.security.model.request.TheaterRequest;
import com.alibou.security.model.response.TheaterResponse;
import com.alibou.security.repository.TheaterRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TheaterService {

    private static final Logger logger = LoggerFactory.getLogger(TheaterService.class);
    private final TheaterRepository repository;
    private final UserService userService;
    private final GeneralMapper generalMapper;

    public TheaterResponse add(TheaterRequest request) {
        var existingTheater = repository.findByName(request.getName());
        if (existingTheater.isPresent()) {
            throw new IllegalArgumentException("Theater's name was exist");
        }
        var theater = generalMapper.mapToEntity(request, Theater.class);
        theater.setCreatedBy(userService.getCurrentUserId());
        theater.setCreatedAt(LocalDateTime.now());
        var theater = Theater.builder()
                .name(request.getName())
                .location(request.getLocation())
                .totalSeats(request.getTotalSeats())
                .build();
        repository.save(theater);
        logger.info("Theater added successfully: {}", theater);
        return generalMapper.mapToDTO(theater, TheaterResponse.class);
    }

    public TheaterResponse change(TheaterRequest request, Long id) {
        var existingTheater = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Theater not found"));
        generalMapper.mapToEntity(request, existingTheater);
        existingTheater.setUpdatedBy(userService.getCurrentUserId());
        existingTheater.setUpdatedAt(LocalDateTime.now());
        repository.save(existingTheater);
        logger.info("Theater updated successfully: {}", existingTheater);
        return generalMapper.mapToDTO(existingTheater, TheaterResponse.class);
    }

    public void delete(Long id) {
        var existingTheater = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Theater not found"));
        repository.deleteById(existingTheater.getId());
        logger.info("Theater deleted successfully: {}", id);
    }

    public List<TheaterResponse> findAll() {
        List<Theater> theaters = repository.findAll();
        logger.info("Theaters retrieved successfully");
        return theaters.stream()
                .map(theater -> generalMapper.mapToDTO(theater, TheaterResponse.class))
                .collect(Collectors.toList());
    }
}