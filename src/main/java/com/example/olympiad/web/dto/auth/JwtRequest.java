package com.example.olympiad.web.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JwtRequest {
    @NotNull(message = "Имя пользователя не должно быть пустым")
    @Schema(description = "Ник пользователя", example = "cweb_1_1")
    private String username;
    @NotNull(message = "Пароль не должен быть пустым.")
    @Schema(description = "Пароль пользователя")
    private String password;
}
