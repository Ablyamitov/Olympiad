package com.example.olympiad.web.controller;

import com.example.olympiad.domain.exception.ResourceNotFoundException;
import com.example.olympiad.domain.user.Role;
import com.example.olympiad.domain.user.User;
import com.example.olympiad.service.AuthService;
import com.example.olympiad.service.UserService;
import com.example.olympiad.web.dto.auth.JwtRequest;
import com.example.olympiad.web.dto.auth.JwtResponse;
import com.example.olympiad.web.dto.user.UserDto;
import com.example.olympiad.web.dto.validation.OnCreate;
import com.example.olympiad.web.mappers.UserMapper;
import com.example.olympiad.web.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    private final UserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticationManager authenticationManager;

    @GetMapping("/checkAuth")
    public ResponseEntity<JwtResponse> checkAuth(HttpServletRequest servletRequest) {
        JwtResponse jwtResponse = new JwtResponse();

        String bearerToken = ((HttpServletRequest)servletRequest).getHeader("Authorization");
        if (bearerToken!=null && bearerToken.startsWith("Bearer ") ){
            bearerToken = bearerToken.substring(7);
        }
        if (bearerToken!=null && jwtTokenProvider.validateToken(bearerToken)){
            try {
                String username = jwtTokenProvider.getUsername(bearerToken);
                User user = userService.getByUsername(username);
                jwtResponse.setId(user.getId());
                jwtResponse.setUsername(user.getUsername());
                jwtResponse.setSession(user.getSession());
                jwtResponse.setRole(user.getRoles().stream()
                        .map(Role::name)
                        .collect(Collectors.joining(", ")));

                jwtResponse.setAccessToken(bearerToken);
            } catch (ResourceNotFoundException ignored) {

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }


        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/login")
    public JwtResponse login(@Validated
                             @RequestBody final JwtRequest loginRequest, HttpServletResponse response) {

        JwtResponse jwtResponse = new JwtResponse();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(), loginRequest.getPassword())
        );
        User user = userService.getByUsername(loginRequest.getUsername());
        jwtResponse.setId(user.getId());
        jwtResponse.setUsername(user.getUsername());
        jwtResponse.setSession(user.getSession());
        jwtResponse.setRole(user.getRoles().stream()
                .map(Role::name)
                .collect(Collectors.joining(", ")));


        jwtResponse.setAccessToken(jwtTokenProvider.createAccessToken(
                user.getId(), user.getUsername(), user.getRoles())
        );

        return jwtResponse;
    }





    @PostMapping("/register")
    public UserDto register(@Validated(OnCreate.class)
                            @RequestBody final UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        User createdUser = userService.create(user);


        return userMapper.toDto(createdUser);
    }


}
