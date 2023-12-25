package com.example.olympiad.service;

import com.example.olympiad.web.dto.auth.JwtRequest;
import com.example.olympiad.web.dto.auth.JwtResponse;

public interface AuthService {
    JwtResponse login(JwtRequest loginRequest);

    JwtResponse refresh(String refreshToken);
}
