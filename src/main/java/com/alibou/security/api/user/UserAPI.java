package com.alibou.security.api.user;

import com.alibou.security.model.request.ChangePasswordRequest;
import com.alibou.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserAPI {

    private static final Logger logger = LoggerFactory.getLogger(UserAPI.class);

    private final UserService service;

    @PatchMapping
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal connectedUser
    ) {
        if (request == null) {
            logger.warn("ChangePasswordRequest is null for user: {}", connectedUser.getName());
            return ResponseEntity.badRequest().body("ChangePasswordRequest cannot be null"); // 400 Bad Request
        }

        try {
            service.changePassword(request, connectedUser);
            logger.info("Password change request processed for user: {}", connectedUser.getName());
            return ResponseEntity.ok().build(); // 200 OK
        } catch (Exception e) {
            logger.error("Error changing password for user: {}", connectedUser.getName(), e);
            return ResponseEntity.status(500).body("Error changing password"); // 500 Internal Server Error
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        logger.error("Unhandled exception: {}", e.getMessage());
        return ResponseEntity.status(500).body("Internal server error"); // 500 Internal Server Error
    }
}
