package com.alibou.security.api.user;

import com.alibou.security.entity.User;
import com.alibou.security.model.request.ChangePasswordRequest;
import com.alibou.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/user/info")
@RequiredArgsConstructor
public class InfoAPI {

    private static final Logger logger = LoggerFactory.getLogger(InfoAPI.class);

    private final UserService service;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserInfo(@PathVariable Long id) {
        try {
            User user = service.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            logger.error("Failed to retrieve user info", e);
            return ResponseEntity.status(500).body("Failed to retrieve user info");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            service.deleteUserById(id);
            return ResponseEntity.ok("User deactivated successfully");
        } catch (Exception e) {
            logger.error("Failed to deactivate user", e);
            return ResponseEntity.status(500).body("Failed to deactivate user");
        }
    }

    @PatchMapping
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal connectedUser
    ) {
        if (request == null) {
            logger.warn("ChangePasswordRequest is null for user: {}", connectedUser != null ? connectedUser.getName() : "unknown");
            return ResponseEntity.badRequest().body("ChangePasswordRequest cannot be null"); // 400 Bad Request
        }

        if (connectedUser == null) {
            logger.warn("Connected user is null");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized"); // 401 Unauthorized
        }

        try {
            service.changePassword(request, connectedUser);
            logger.info("Password change request processed for user: {}", connectedUser.getName());
            return ResponseEntity.ok("Password changed successfully"); // 200 OK
        } catch (IllegalStateException e) {
            logger.error("Error changing password for user: {}", connectedUser.getName(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // 400 Bad Request
        } catch (Exception e) {
            logger.error("Error changing password for user: {}", connectedUser.getName(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error changing password"); // 500 Internal Server Error
        }
    }
}