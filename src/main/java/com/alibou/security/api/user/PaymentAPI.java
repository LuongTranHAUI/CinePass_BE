package com.alibou.security.api.user;

import com.alibou.security.entity.Payment;
import com.alibou.security.entity.User;
import com.alibou.security.model.request.PaymentRequest;
import com.alibou.security.service.PaymentService;
import com.alibou.security.service.UserService;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/payments")
@RequiredArgsConstructor
public class PaymentAPI {
    private static final Logger logger = LoggerFactory.getLogger(PaymentAPI.class);
    private final PaymentService paymentService;
    private final UserService userService;

    @PostMapping("/vnpay-process")
    public ResponseEntity<String> processPayment(@RequestBody PaymentRequest paymentRequest) {
        try {
            User user = userService.getUserById(paymentRequest.getUserId());
            JsonObject response = paymentService.processVNPayment(paymentRequest,user);
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

    @GetMapping("/{user_id}")
    public ResponseEntity<?> getPayments(@PathVariable("user_id") Long userId) {
        try {
            List<Payment> payments = paymentService.findByUserId(userId);
            return ResponseEntity.ok(payments);
        } catch (Exception e) {
            logger.error("Failed to retrieve payments: {}", e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }
}