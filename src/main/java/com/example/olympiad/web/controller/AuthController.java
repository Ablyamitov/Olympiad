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
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
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
    public JwtResponse checkAuth(HttpServletRequest servletRequest) {

        Cookie[] cookies = servletRequest.getCookies();
        String accessToken = null;
        JwtResponse jwtResponse = new JwtResponse();
        if (cookies != null) {
            accessToken = Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals("access"))
                    .findFirst()
                    .map(Cookie::getValue).orElse(null);
        }
        if (cookies != null && jwtTokenProvider.validateToken(accessToken)) {
            try {
                String username = jwtTokenProvider.getUsername(accessToken);
                User user = userService.getByUsername(username);
                jwtResponse.setId(user.getId());
                jwtResponse.setUsername(user.getUsername());
                jwtResponse.setSession(user.getSession());
                jwtResponse.setRole(user.getRoles().stream()
                        .map(Role::name)
                        .collect(Collectors.joining(", ")));
            } catch (ResourceNotFoundException ignored) {}
        }
        return jwtResponse;
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
        Cookie cookie = new Cookie("access",
                jwtTokenProvider.createAccessToken(
                        user.getId(),
                        user.getUsername(),
                        user.getRoles()));
        cookie.setHttpOnly(true);

        //cookie.setSecure(true);
        response.addCookie(cookie);
        //return authService.login(loginRequest);
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
