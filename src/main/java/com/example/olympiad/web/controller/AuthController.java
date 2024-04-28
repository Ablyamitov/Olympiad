package com.example.olympiad.web.controller;

import com.example.olympiad.service.AuthService;
import com.example.olympiad.web.dto.auth.JwtRequest;
import com.example.olympiad.web.dto.auth.JwtResponse;
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
//        JwtResponse jwtResponse = new JwtResponse();
//
//        String bearerToken = servletRequest.getHeader("Authorization");
//        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
//            bearerToken = bearerToken.substring(7);
//        }
//        if (bearerToken != null && jwtTokenProvider.validateToken(bearerToken)) {
//            try {
//                String username = jwtTokenProvider.getUsername(bearerToken);
//                User user = userService.getByUsername(username);
//                jwtResponse.setId(user.getId());
//                jwtResponse.setUsername(user.getUsername());
//                jwtResponse.setSession(user.getSession());
//                jwtResponse.setRole(user.getRoles().stream()
//                        .map(Role::name)
//                        .collect(Collectors.joining(", ")));
//
//                jwtResponse.setAccessToken(bearerToken);
//            } catch (UserNotFoundException ignored) {
//
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
//            }
//
//
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
//        }

        //return ResponseEntity.ok(jwtResponse);
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

//        JwtResponse jwtResponse = new JwtResponse();
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        loginRequest.getUsername(), loginRequest.getPassword())
//        );
//        User user = userService.getByUsername(loginRequest.getUsername());
//        jwtResponse.setId(user.getId());
//        jwtResponse.setUsername(user.getUsername());
//        jwtResponse.setSession(user.getSession());
//        jwtResponse.setRole(user.getRoles().stream()
//                .map(Role::name)
//                .collect(Collectors.joining(", ")));
//
//
//        jwtResponse.setAccessToken(jwtTokenProvider.createAccessToken(
//                user.getId(), user.getUsername(), user.getRoles())
//        );
//
//        return jwtResponse;
        return ResponseEntity.ok(authService.login(loginRequest));
    }


}
