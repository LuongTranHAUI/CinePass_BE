package com.alibou.security.service;

import com.alibou.security.config.GeneralMapper;
import com.alibou.security.entity.Hall;
import com.alibou.security.model.request.HallRequest;
import com.alibou.security.model.response.HallResponse;
import com.alibou.security.repository.HallRepository;
import com.alibou.security.repository.TheaterRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HallService {
    private static final Logger logger = LoggerFactory.getLogger(HallService.class);
    private final HallRepository repository;
    private final UserService userService;
    private final TheaterRepository theaterRepository;
    private final GeneralMapper generalMapper;

    public HallResponse add(HallRequest request) {
        var existingHall = repository.findByName(request.getName());
        if (existingHall.isPresent()) {
            throw new IllegalArgumentException("Hall's name was exist");
        }
        var hall = generalMapper.mapToEntity(request, Hall.class);
        hall.setCreatedBy(userService.getCurrentUserId());
        hall.setCreatedAt(LocalDateTime.now());
        hall.setTheater(theaterRepository.findById(request.getTheaterId()).orElseThrow(() -> new IllegalArgumentException("Invalid theater ID")));
        repository.save(hall);
        logger.info("Hall added successfully: {}", hall);
        return generalMapper.mapToDTO(hall, HallResponse.class);
    }

    public HallResponse change(HallRequest request, Long id) {
        var existingHall = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hall not found"));
        generalMapper.mapToEntity(request, existingHall);
        existingHall.setUpdatedAt(LocalDateTime.now());
        existingHall.setUpdatedBy(userService.getCurrentUserId());
        existingHall.setTheater(theaterRepository.findById(request.getTheaterId()).orElseThrow(() -> new IllegalArgumentException("Invalid theater ID")));
        repository.save(existingHall);
        logger.info("Hall updated successfully: {}", existingHall);
        return generalMapper.mapToDTO(existingHall, HallResponse.class);
    }

    public void delete(Long id) {
        var existingHall = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hall not found"));
        repository.deleteById(existingHall.getId());
        logger.info("Hall deleted successfully: {}", id);
    }

    public List<HallResponse> findAll() {
        List<Hall> halls = repository.findAll();
        logger.info("Halls retrieved successfully");
        return halls.stream()
                .map(hall -> generalMapper.mapToDTO(hall, HallResponse.class))
                .toList();
    }
}
