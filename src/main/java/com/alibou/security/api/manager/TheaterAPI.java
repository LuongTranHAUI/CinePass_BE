package com.alibou.security.api.manager;

import com.alibou.security.entity.Theater;
import com.alibou.security.exception.MissingRequiredFieldsException;
import com.alibou.security.model.request.TheaterRequest;
import com.alibou.security.model.response.TheaterResponse;
import com.alibou.security.service.TheaterService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/management/theaters")
@RequiredArgsConstructor
public class TheaterAPI {

    private static final Logger logger = LoggerFactory.getLogger(TheaterAPI.class);
    private final TheaterService service;

    @GetMapping
    public ResponseEntity<List<Theater>> findAllTheaters() {
        try {
            List<Theater> theaters = service.findAll();
            logger.info("Retrieved all theaters successfully");
            return ResponseEntity.ok(theaters); // 200 OK
        } catch (Exception e) {
            logger.error("Failed to retrieve theaters: {}", e.getMessage());
            return ResponseEntity.status(500).body(null); // 500 Internal Server Error
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findTheaterById(@PathVariable Integer id) {
        try {
            Theater theater = service.findById(id);
            logger.info("Retrieved theater successfully with ID: {}", id);
            return ResponseEntity.ok(theater); // 200 OK
        } catch (Exception e) {
            logger.error("Failed to retrieve theater with ID: {}", e.getMessage());
            return ResponseEntity.status(500).body(null); // 500 Internal Server Error
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<?> findTheaterByName(@PathVariable String name) {
        try {
            TheaterResponse theater = service.findByName(name);
            logger.info("Retrieved theater successfully with name: {}", name);
            return ResponseEntity.ok(theater); // 200 OK
        } catch (Exception e) {
            logger.error("Failed to retrieve theater with name {}: {}", name, e.getMessage());
            return ResponseEntity.status(500).body(null); // 500 Internal Server Error
        }
    }

    @PostMapping
    public ResponseEntity<?> addTheater(@RequestBody TheaterRequest request) {
        try {
            if (request == null) {
                throw new MissingRequiredFieldsException("Request body is missing");
            }
            TheaterResponse response = service.add(request);
            logger.info("Theater saved successfully: {}", request);
            return ResponseEntity.status(201).body(response); // 201 Created
        } catch (MissingRequiredFieldsException e) {
            logger.error("Missing required fields in request: {}", e.getMessage());
            return ResponseEntity.status(400).body(e.getMessage()); // 400 Bad Request
        } catch (Exception e) {
            logger.error("Failed to save theater: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error saving theater"); // 500 Internal Server Error
        }
    }

    @PutMapping
    public ResponseEntity<?> changeTheater(@RequestBody TheaterRequest request) {
        try {
            if (request == null || request.getId() == null) {
                throw new MissingRequiredFieldsException("Request body or id is missing");
            }
            TheaterResponse response = service.change(request);
            logger.info("Updated theater successfully: {}", request);
            return ResponseEntity.status(200).body(response); // 200 OK
        } catch (MissingRequiredFieldsException e) {
            logger.error("Missing required fields or ID in request: {}", e.getMessage());
            return ResponseEntity.status(400).body(e.getMessage()); // 400 Bad Request
        } catch (Exception e) {
            logger.error("Failed to update theater: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error updating theater"); // 500 Internal Server Error
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTheater(@PathVariable Integer id) {
        try {
            service.delete(id);
            logger.info("Deleted theater successfully with ID: {}", id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (Exception e) {
            logger.error("Failed to delete theater with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(500).body("Error deleting theater"); // 500 Internal Server Error
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        logger.error("Unhandled exception occurred: {}", e.getMessage());
        return ResponseEntity.status(500).body("Internal server error"); // 500 Internal Server Error
    }
}
