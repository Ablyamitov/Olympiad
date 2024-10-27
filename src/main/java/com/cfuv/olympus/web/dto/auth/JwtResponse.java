package com.cfuv.olympus.web.dto.auth;

import lombok.Data;

@Data
public class JwtResponse {
    private Long id;
    private String name;
    private String surname;
    private String username;
    private String email;
    private Long session;
    private String Role;
    private String accessToken;

}
