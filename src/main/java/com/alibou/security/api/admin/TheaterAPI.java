package com.alibou.security.api.admin;

import com.alibou.security.model.request.TheaterRequest;
import com.alibou.security.service.TheaterService;
import com.alibou.security.entity.Theater;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/theaters")
@RequiredArgsConstructor
public class TheaterAPI {

    private static final Logger logger = LoggerFactory.getLogger(TheaterAPI.class);
    private final TheaterService service;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody TheaterRequest request) {
        try {
            service.save(request);
            logger.info("Theater saved successfully: {}", request);
            return ResponseEntity.status(201).build(); // 201 Created
        } catch (Exception e) {
            logger.error("Error saving Theater: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error saving Theater"); // 500 Internal Server Error
        }
    }

    @GetMapping
    public ResponseEntity<List<Theater>> findAllBooks() {
        try {
            List<Theater> theaters = service.findAll();
            logger.info("theaters retrieved successfully");
            return ResponseEntity.ok(theaters); // 200 OK
        } catch (Exception e) {
            logger.error("Error retrieving theaters: {}", e.getMessage());
            return ResponseEntity.status(500).body(null); // 500 Internal Server Error
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        logger.error("Unhandled exception: {}", e.getMessage());
        return ResponseEntity.status(500).body("Internal server error"); // 500 Internal Server Error
    }
}
