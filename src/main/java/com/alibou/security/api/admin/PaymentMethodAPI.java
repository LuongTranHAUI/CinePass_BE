package com.alibou.security.api.admin;

import com.alibou.security.entity.PaymentMethod;
import com.alibou.security.model.request.PaymentMethodRequest;
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
            List<PaymentMethod> paymentMethods = service.findAll();
            logger.info("Retrieved all payment methods successfully");
            return ResponseEntity.ok(paymentMethods); // 200 OK
        } catch (Exception e) {
            logger.error("Failed to retrieve payment methods: {}", e.getMessage());
            return ResponseEntity.status(500).body(null); // 500 Internal Server Error
        }
    }

    @PostMapping
    public ResponseEntity<?> addPaymentMethod(@RequestBody PaymentMethodRequest request) {
        try {
            service.add(request);
            logger.info("Payment method added successfully: {}", request);
            return ResponseEntity.ok(null); // 200 OK
        } catch (Exception e) {
            logger.error("Failed to add payment method: {}", e.getMessage());
            return ResponseEntity.status(500).body(null); // 500 Internal Server Error
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> changePaymentMethod(@RequestBody PaymentMethodRequest request, @PathVariable Long id) {
        try {
            service.change(request, id);
            logger.info("Payment method changed successfully: {}", request);
            return ResponseEntity.ok(null); // 200 OK
        } catch (Exception e) {
            logger.error("Failed to change payment method: {}", e.getMessage());
            return ResponseEntity.status(500).body(null); // 500 Internal Server Error
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePaymentMethod(@PathVariable Long id) {
        try {
            service.delete(id);
            logger.info("Payment method deleted successfully with id: {}", id);
            return ResponseEntity.ok(null); // 200 OK
        } catch (Exception e) {
            logger.error("Failed to delete payment method with id: {}", e.getMessage());
            return ResponseEntity.status(500).body(null); // 500 Internal Server Error
        }
    }
}
