package com.alibou.security.api.authentication;

import com.alibou.security.model.request.AuthenticationRequest;
import com.alibou.security.model.request.RegisterRequest;
import com.alibou.security.model.response.AuthenticationResponse;
import com.alibou.security.model.response.UserResponse;
import com.alibou.security.service.AuthenticationService;
import com.alibou.security.service.LogoutService;
import com.alibou.security.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationAPI {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationAPI.class);
    private final AuthenticationService service;
    private final LogoutService logoutService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            AuthenticationResponse response = service.register(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.error("Error during registration: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {
        try {
            AuthenticationResponse response = service.authenticate(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error during authentication: {}", e.getMessage());
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        try {
            service.refreshToken(request, response);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            logger.error("Error during token refresh: {}", e.getMessage());
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        logoutService.logout(request, response, authentication);
        logger.info("User logged out successfully");
    }

    @PutMapping("/block/{id}")
    public ResponseEntity<?> updateStatusUser(@PathVariable("id") Long id){
        try {
            UserResponse userResponse = userService.blockUser(id);
            logger.info("Block user: {}", userResponse);
            return ResponseEntity.ok().body(userResponse);
        }catch (Exception e) {
            logger.error("Error blocking user: {}", e);
            return ResponseEntity.status(500).body("Error blocking user");
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        logger.error("Unhandled exception occurred: {}", e.getMessage());
        return ResponseEntity.status(500).body("Internal server error");
    }
}
