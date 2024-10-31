package com.alibou.security.api.admin;

import com.alibou.security.model.request.DiscountRequest;
import com.alibou.security.model.response.DiscountResponse;
import com.alibou.security.service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/discounts")
@RequiredArgsConstructor
public class DiscountAPI {
    private static final Logger logger = LoggerFactory.getLogger(DiscountAPI.class);
    private final DiscountService service;

    @GetMapping
    public ResponseEntity<?> findAllDiscounts() {
        try {
            List<DiscountResponse> discounts = service.findAll();
            logger.info("Retrieved all discounts successfully");
            return ResponseEntity.ok(discounts);
        } catch (Exception e) {
            logger.error("Failed to retrieve discounts: {}", e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<?> addDiscount(@RequestBody DiscountRequest request) {
        try {
            DiscountResponse response = service.add(request);
            logger.info("Discount added successfully: {}", response);
            return ResponseEntity.status(201).body(response); // 200 OK
        } catch (IllegalArgumentException e) {
            logger.error("Failed to add discount: {}", e.getMessage());
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Failed to add discount: {}", e.getMessage());
            return ResponseEntity.status(500).body("Internal server error");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDiscount(@RequestBody DiscountRequest request, @PathVariable Long id) {
        try {
            DiscountResponse response = service.update(request,id);
            logger.info("Discount updated successfully: {}", response);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to update discount with ID: {}", e.getMessage());
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Failed to update discount with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDiscount(@PathVariable Long id) {
        try {
            service.delete(id);
            logger.info("Discount deleted successfully with ID: {}", id);
            return ResponseEntity.ok(null);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to delete discount with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Failed to delete discount with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(500).body("Internal server error");
    }
}
