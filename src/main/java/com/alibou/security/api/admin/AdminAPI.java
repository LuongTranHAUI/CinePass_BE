package com.alibou.security.api.admin;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminAPI {

    @Operation(
            description = "Get endpoint for admin",
            summary = "This is a summary for admin get endpoint",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "Unauthorized / Invalid Token", responseCode = "401")
            }
    )
    @GetMapping
    public String get() {
        return "GET:: admin controller";
    }

    @Operation(
            description = "Post endpoint for admin",
            summary = "This is a summary for admin post endpoint",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "Unauthorized / Invalid Token", responseCode = "401")
            }
    )
    @PostMapping
    @Hidden
    public String post() {
        return "POST:: admin controller";
    }

    @Operation(
            description = "Put endpoint for admin",
            summary = "This is a summary for admin put endpoint",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "Unauthorized / Invalid Token", responseCode = "401")
            }
    )
    @PutMapping
    @Hidden
    public String put() {
        return "PUT:: admin controller";
    }

    @Operation(
            description = "Delete endpoint for admin",
            summary = "This is a summary for admin delete endpoint",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "Unauthorized / Invalid Token", responseCode = "401")
            }
    )
    @DeleteMapping
    @Hidden
    public String delete() {
        return "DELETE:: admin controller";
    }
}