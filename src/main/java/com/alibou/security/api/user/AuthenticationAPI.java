package com.alibou.security.api.user;

import com.alibou.security.exception.EmailAlreadyInUseException;
import com.alibou.security.exception.PhoneAlreadyInUseException;
import com.alibou.security.exception.UsernameAlreadyInUseException;
import com.alibou.security.model.request.AuthenticationRequest;
import com.alibou.security.model.response.AuthenticationResponse;
import com.alibou.security.service.AuthenticationService;
import com.alibou.security.model.request.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationAPI {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationAPI.class);
    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            AuthenticationResponse response = service.register(request);
            return ResponseEntity.ok(response); // 200 OK
        } catch (EmailAlreadyInUseException e) {
            logger.error("Error during registration: Email already in use");
            return ResponseEntity.status(409).body("Email is already in use"); // 409 Conflict
        } catch (UsernameAlreadyInUseException e) {
            logger.error("Error during registration: Username already in use");
            return ResponseEntity.status(409).body("Username is already in use"); // 409 Conflict
        } catch (PhoneAlreadyInUseException e) {
            logger.error("Error during registration: Phone number already in use");
            return ResponseEntity.status(409).body("Phone number is already in use"); // 409 Conflict
        } catch (Exception e) {
            logger.error("Unhandled error during registration: {}", e.getMessage());
            return ResponseEntity.status(500).body("Internal server error"); // 500 Internal Server Error
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {
        try {
            AuthenticationResponse response = service.authenticate(request);
            return ResponseEntity.ok(response); // 200 OK
        } catch (Exception e) {
            logger.error("Error during authentication: {}", e.getMessage());
            return ResponseEntity.status(401).body("Unauthorized"); // 401 Unauthorized
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        try {
            service.refreshToken(request, response);
            return ResponseEntity.ok().build(); // 200 OK
        } catch (IOException e) {
            logger.error("Error during token refresh: {}", e.getMessage());
            return ResponseEntity.status(401).body("Unauthorized"); // 401 Unauthorized
        }
    }

    // Global exception handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        logger.error("Unhandled exception: {}", e.getMessage());
        return ResponseEntity.status(500).body("Internal server error"); // 500 Internal Server Error
    }
}
