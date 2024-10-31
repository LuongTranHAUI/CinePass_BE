package com.alibou.security.api.admin;

import com.alibou.security.model.request.PaymentMethodRequest;
import com.alibou.security.model.response.PaymentMethodResponse;
import com.alibou.security.service.PaymentMethodService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/management/payment-methods")
@RequiredArgsConstructor
public class PaymentMethodAPI {
    private static final Logger logger = LoggerFactory.getLogger(DiscountAPI.class);
    private final PaymentMethodService service;

    @GetMapping
    public ResponseEntity<?> findAllPaymentMethods() {
        try {
            List<PaymentMethodResponse> paymentMethods = service.findAll();
            logger.info("Retrieved all payment methods successfully");
            return ResponseEntity.ok(paymentMethods);
        } catch (Exception e) {
            logger.error("Failed to retrieve payment methods: {}", e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<?> addPaymentMethod(@RequestBody PaymentMethodRequest request) {
        try {
            PaymentMethodResponse response = service.add(request);
            logger.info("Payment method added successfully: {}", response);
            return ResponseEntity.status(201).body(response);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to add payment method: {}", e.getMessage());
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Failed to add payment method: {}", e.getMessage());
            return ResponseEntity.status(500).body("Internal server error");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> changePaymentMethod(@RequestBody PaymentMethodRequest request, @PathVariable Long id) {
        try {
            PaymentMethodResponse response = service.change(request, id);
            logger.info("Payment method changed successfully: {}", request);
            return ResponseEntity.ok(null);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to update payment method with ID: {}", e.getMessage());
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Failed to update payment method with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePaymentMethod(@PathVariable Long id) {
        try {
            service.delete(id);
            logger.info("Payment method deleted successfully with id: {}", id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            logger.error("Failed to delete payment method with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Failed to delete payment method with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(500).body("Internal server error");
    }
}
