package com.alibou.security.api.manager;

import com.alibou.security.entity.Discount;
import com.alibou.security.model.request.DiscountRequest;
import com.alibou.security.service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/management/discounts")
@RequiredArgsConstructor
public class DiscountAPI {
    private static final Logger logger = LoggerFactory.getLogger(DiscountAPI.class);
    private final DiscountService service;

    @GetMapping
    public ResponseEntity<?> findAllDiscounts() {
        try {
            List<Discount> discounts = service.findAll();
            logger.info("Retrieved all discounts successfully");
            return ResponseEntity.ok(discounts); // 200 OK
        } catch (Exception e) {
            logger.error("Failed to retrieve discounts: {}", e.getMessage());
            return ResponseEntity.status(500).body(null); // 500 Internal Server Error
        }
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> findDiscountByCode(@PathVariable String code) {
        try {
            Discount discount = service.findByCode(code);
            logger.info("Retrieved discount successfully with code: {}", code);
            return ResponseEntity.ok(discount); // 200 OK
        } catch (Exception e) {
            logger.error("Failed to retrieve discount with code: {}", e.getMessage());
            return ResponseEntity.status(500).body(null); // 500 Internal Server Error
        }
    }

    @PostMapping
    public ResponseEntity<?> addDiscount(@RequestBody DiscountRequest request) {
        try {
            service.add(request);
            logger.info("Discount added successfully: {}", request);
            return ResponseEntity.ok(null); // 200 OK
        } catch (Exception e) {
            logger.error("Failed to add discount: {}", e.getMessage());
            return ResponseEntity.status(500).body(null); // 500 Internal Server Error
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteDiscount(@PathVariable Long id) {
        try {
            service.delete(id);
            logger.info("Discount deleted successfully with ID: {}", id);
            return ResponseEntity.ok(null); // 200 OK
        } catch (Exception e) {
            logger.error("Failed to delete discount with ID: {}", e.getMessage());
            return ResponseEntity.status(500).body(null); // 500 Internal Server Error
        }
    }

}