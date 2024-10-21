package com.alibou.security.api.user;

import com.alibou.security.config.VnPayConfig;
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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

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
        Map<String, String> fields = new HashMap<>();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements(); ) {
            String fieldName = params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if (fieldValue != null && fieldValue.length() > 0) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        if (fields.containsKey("vnp_SecureHashType")) {
            fields.remove("vnp_SecureHashType");
        }
        if (fields.containsKey("vnp_SecureHash")) {
            fields.remove("vnp_SecureHash");
        }
        String signValue = VnPayConfig.hashAllFields(fields);

        JsonObject response = new JsonObject();
//        if (signValue.equals(vnp_SecureHash)) {
            if ("00".equals(request.getParameter("vnp_ResponseCode"))) {
                response.addProperty("message", "GD Thanh cong");
            } else {
                response.addProperty("message", "GD Khong thanh cong");
            }
//        } else {
//            response.addProperty("message", "Chu ky khong hop le");
//        }
        return ResponseEntity.ok(response.toString());
    }
}