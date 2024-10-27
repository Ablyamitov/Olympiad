package com.cfuv.olympus.web.dto.user.UserInfo;

import lombok.Data;

@Data
public class ChangeUserInfoResponse {

    private Long id;

    private String name;
    private String surname;
    private String username;
    private String email;
    private Long session;

    private String role;
}
