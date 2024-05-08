package com.example.olympiad.web.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JwtRequest {
    @NotNull(message = "Username must be not null.")
    @Schema(description = "Ник пользователя", example = "cweb_1_1")
    private String username;
    @NotNull(message = "Password must be not null.")
    @Schema(description = "Пароль пользователя")
    private String password;
}
