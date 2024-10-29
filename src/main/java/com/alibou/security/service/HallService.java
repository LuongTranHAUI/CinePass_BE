package com.alibou.security.service;

import com.alibou.security.entity.Hall;
import com.alibou.security.model.request.HallRequest;
import com.alibou.security.repository.HallRepository;
import com.alibou.security.repository.TheaterRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HallService {
    private static final Logger logger = LoggerFactory.getLogger(HallService.class);
    private final HallRepository repository;
    private final UserService userService;
    private final TheaterRepository theaterRepository;

    public void add(HallRequest request) {
        var existingHall = repository.findByName(request.getName());
        if (existingHall.isPresent()) {
            throw new IllegalArgumentException("Hall available");
        }
        var hall = Hall.builder()
                .name(request.getName())
                .seatCapacity(request.getSeatCapacity())
                .status(request.getStatus())
                .createdAt(new Timestamp(System.currentTimeMillis()).toLocalDateTime())
                .createdBy(userService.getCurrentUserId())
                .theater(theaterRepository.findById(request.getTheaterId()).orElseThrow(() -> new IllegalArgumentException("Invalid theater ID")))
                .build();
        repository.save(hall);
        logger.info("Hall added successfully: {}", hall);
    }

    public void change(HallRequest request, Long id) {
        var existingHall = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hall not found"));
        var hall = Hall.builder()
                .id(existingHall.getId())
                .name(request.getName())
                .seatCapacity(request.getSeatCapacity())
                .status(request.getStatus())
                .updatedAt(new Timestamp(System.currentTimeMillis()).toLocalDateTime())
                .updatedBy(userService.getCurrentUserId())
                .theater(theaterRepository.findById(request.getTheaterId()).orElseThrow(() -> new IllegalArgumentException("Invalid theater ID")));
    }

    public void delete(Long id) {
        var existingHall = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hall not found"));
        repository.deleteById(existingHall.getId());
    }

    public List<Hall> findAll() {
        List<Hall> halls = repository.findAll();
        logger.info("Halls retrieved successfully");
        return halls;
    }
}
