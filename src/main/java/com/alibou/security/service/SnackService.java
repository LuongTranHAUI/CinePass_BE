package com.alibou.security.service;

import com.alibou.security.config.GeneralMapper;
import com.alibou.security.entity.Snack;
import com.alibou.security.model.request.SnackRequest;
import com.alibou.security.model.response.SnackResponse;
import com.alibou.security.repository.SnackRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SnackService {
    private static final Logger logger = LoggerFactory.getLogger(TheaterService.class);
    private final SnackRepository repository;
    private final UserService userService;
    private final GeneralMapper generalMapper;

    public SnackResponse add(SnackRequest request) {
        var existingSnack = repository.findByName(request.getName());
        if (existingSnack.isPresent()) {
            throw new IllegalArgumentException("Snack's name was exist");
        }
        var snack = generalMapper.mapToEntity(request, Snack.class);
        snack.setCreatedBy(userService.getCurrentUserId());
        snack.setCreatedAt(LocalDateTime.now());
        repository.save(snack);
        logger.info("Snack added successfully: {}", snack);
        return generalMapper.mapToDTO(snack, SnackResponse.class);
    }

    public SnackResponse change(SnackRequest request, Long id) {
        var existingSnack = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Snack not found"));
        generalMapper.mapToEntity(request, existingSnack);
        existingSnack.setUpdatedBy(userService.getCurrentUserId());
        existingSnack.setUpdatedAt(LocalDateTime.now());
        repository.save(existingSnack);
        logger.info("Snack updated successfully: {}", existingSnack);
        return generalMapper.mapToDTO(existingSnack, SnackResponse.class);
    }

    public void delete(Long id) {
        var existingSnack = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Snack not found"));
        repository.deleteById(existingSnack.getId());
        logger.info("Snack deleted successfully: {}", id);
    }

    public List<SnackResponse> findAll() {
        List<Snack> snacks = repository.findAll();
        logger.info("Retrieved all snacks successfully");
        return snacks.stream()
                .map(snack -> generalMapper.mapToDTO(snack, SnackResponse.class))
                .collect(Collectors.toList());
    }
}
