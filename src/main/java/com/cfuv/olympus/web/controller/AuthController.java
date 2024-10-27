package com.cfuv.olympus.web.controller;

import com.cfuv.olympus.service.AuthService;
import com.cfuv.olympus.web.dto.auth.JwtRequest;
import com.cfuv.olympus.web.dto.auth.JwtResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth controller", description = "Authenticate management")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "Check authenticate", description = "Check and return an authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @GetMapping("/checkAuth")
    public ResponseEntity<JwtResponse> checkAuth(HttpServletRequest servletRequest) {
        return ResponseEntity.ok(authService.checkAuth(servletRequest));
    }

    @Operation(summary = "Login user", description = "Return an authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Validated
                                             @RequestBody final JwtRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }


}
