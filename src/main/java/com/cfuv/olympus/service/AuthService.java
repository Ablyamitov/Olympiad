package com.cfuv.olympus.service;

import com.cfuv.olympus.domain.exception.entity.user.UserNotFoundException;
import com.cfuv.olympus.domain.user.Role;
import com.cfuv.olympus.domain.user.User;
import com.cfuv.olympus.web.dto.auth.JwtRequest;
import com.cfuv.olympus.web.dto.auth.JwtResponse;
import com.cfuv.olympus.web.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public JwtResponse login(final JwtRequest loginRequest) {

        JwtResponse jwtResponse = new JwtResponse();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(), loginRequest.getPassword())
        );
        User user = userService.getByUsername(loginRequest.getUsername());
        mapToJwtResponse(jwtResponse, user);


        jwtResponse.setAccessToken(jwtTokenProvider.createAccessToken(
                user.getId(), user.getUsername(), user.getRoles())
        );

        return jwtResponse;
    }

    private void mapToJwtResponse(JwtResponse jwtResponse, User user) {
        jwtResponse.setId(user.getId());
        jwtResponse.setName(user.getName());
        jwtResponse.setSurname(user.getSurname());
        jwtResponse.setUsername(user.getUsername());

        jwtResponse.setEmail(user.getEmail());

        jwtResponse.setSession(user.getSession());
        jwtResponse.setRole(user.getRoles().stream()
                .map(Role::name)
                .collect(Collectors.joining(", ")));
    }


    public JwtResponse checkAuth(HttpServletRequest servletRequest) {
        JwtResponse jwtResponse = new JwtResponse();

        String bearerToken = servletRequest.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken = bearerToken.substring(7);
        }
        if (bearerToken != null && jwtTokenProvider.validateToken(bearerToken)) {
            try {
                String username = jwtTokenProvider.getUsername(bearerToken);
                User user = userService.getByUsername(username);
                mapToJwtResponse(jwtResponse, user);

                jwtResponse.setAccessToken(bearerToken);
            } catch (UserNotFoundException ignored) {

                return null;
            }


        } else {
            return null;
        }
        return jwtResponse;
    }


}


