package com.example.olympiad.web.dto.auth;

import lombok.Data;

@Data
public class JwtResponse {
    private Long id;
    private String username;
    private Long session;
    private String Role;
    private String accessToken;
    //private String refreshToken;

}
