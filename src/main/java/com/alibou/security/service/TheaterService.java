package com.alibou.security.service;

import com.alibou.security.model.request.TheaterRequest;
import com.alibou.security.entity.Theater;
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

    public void save(TheaterRequest request) {
        try {
            var book = Theater.builder()
                    .id(request.getId())
                    .name(request.getName())
                    .location(request.getLocation())
                    .totalSeats(request.getTotalSeats())
                    .build();
            repository.save(book);
            logger.info("Book saved successfully: {}", book);
        } catch (Exception e) {
            logger.error("Error saving book: {}", e.getMessage());
            throw e;
        }
    }

    public List<Theater> findAll() {
        try {
            List<Theater> books = repository.findAll();
            logger.info("Books retrieved successfully");
            return books;
        } catch (Exception e) {
            logger.error("Error retrieving books: {}", e.getMessage());
            throw e;
        }
    }
}
