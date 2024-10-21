package com.alibou.security.api.user;

import com.alibou.security.model.request.PaymentRequest;
import com.alibou.security.service.PaymentService;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/payments")
@RequiredArgsConstructor
public class PaymentAPI {
    private static final Logger logger = LoggerFactory.getLogger(PaymentAPI.class);
    private final PaymentService paymentService;

    @PostMapping("/vnpay-process")
    public ResponseEntity<String> processPayment(@RequestBody PaymentRequest paymentRequest) {
        try {
            JsonObject response = paymentService.processPayment(paymentRequest);
            return ResponseEntity.ok(response.toString());
        } catch (Exception e) {
            logger.error("Payment processing failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Payment processing failed");
        }
    }

    @GetMapping("/vnpay-return")
    public ResponseEntity<String> handleVnPayReturn(HttpServletRequest request) {
        JsonObject response = paymentService.handleVnPayReturn(request);
        return ResponseEntity.ok(response.toString());
    }
}