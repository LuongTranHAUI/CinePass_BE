package com.alibou.security.api.user;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/demo-controller")
@Hidden
public class DemoAPI {

  private static final Logger logger = LoggerFactory.getLogger(DemoAPI.class);

  @Operation(
          description = "Get endpoint for demo",
          summary = "This is a summary for demo get endpoint",
          responses = {
                  @ApiResponse(description = "Success", responseCode = "200"),
                  @ApiResponse(description = "Unauthorized / Invalid Token", responseCode = "401"),
                  @ApiResponse(description = "Internal Server Error", responseCode = "500")
          }
  )
  @GetMapping
  public ResponseEntity<String> sayHello() {
    try {
      return ResponseEntity.ok("Hello from secured endpoint"); // 200 OK
    } catch (Exception e) {
      logger.error("Error in sayHello: {}", e.getMessage());
      return ResponseEntity.status(500).body("Internal server error"); // 500 Internal Server Error
    }
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleException(Exception e) {
    logger.error("Unhandled exception: {}", e.getMessage());
    return ResponseEntity.status(500).body("Internal server error"); // 500 Internal Server Error
  }
}
