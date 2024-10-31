package com.alibou.security.api.admin;

import com.alibou.security.api.user.InfoAPI;
import com.alibou.security.model.response.UserResponse;
import com.alibou.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AccountAPI {
    private static final Logger logger = LoggerFactory.getLogger(InfoAPI.class);
    private final UserService service;

    @GetMapping
    public ResponseEntity<List<UserResponse>> findAllUsers() {
        try {
            List<UserResponse> users = service.getAllUsers();
            logger.info("Retrieved all theaters successfully");
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            logger.error("Failed to retrieve notifications: {}", e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/{id}/role/{roleId}")
    public ResponseEntity<?> changeUserRole(@PathVariable Long id, @PathVariable Long roleId) {
        try {
            service.changeRole(id, roleId);
            logger.info("Updated user role successfully: {}", id);
            return ResponseEntity.status(200).body("User role updated successfully");
        } catch (IllegalArgumentException e) {
            logger.error("Failed to update user role with ID: {}", e.getMessage());
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Failed to update user role with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
