package com.example.olympiad.web.dto.user.UserInfo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserInfo {

    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Surname cannot be blank")
    private String surname;

    @NotBlank(message = "Email cannot be blank")
    private String email;
}
