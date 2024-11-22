package com.alibou.security.api.user;

import com.alibou.security.service.QRCodeValidationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/management")
public class TicketQRCodeAPI {

    private final QRCodeValidationService qrCodeValidationService;

    public TicketQRCodeAPI(QRCodeValidationService qrCodeValidationService) {
        this.qrCodeValidationService = qrCodeValidationService;
    }

    @PostMapping("/scan-and-validate")
    public ResponseEntity<String> scanAndValidateTicket(@RequestParam String filePath, @RequestParam Long userId) {
        try {
            String result = qrCodeValidationService.scanAndValidateTicket(filePath, userId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error scanning and validating ticket: " + e.getMessage());
        }
    }
}
