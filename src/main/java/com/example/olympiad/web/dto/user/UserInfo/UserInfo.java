package com.example.olympiad.web.dto.user.UserInfo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserInfo {

    @NotBlank(message = "Username cannot be blank")
    @Schema(description = "Ник участника", example = "cweb_1_1")
    private String username;

    @NotBlank(message = "Name cannot be blank")
    @Schema(description = "Имя участника", example = "Иван")
    private String name;

    @Schema(description = "Фамилия участника", example = "Иванов")
    @NotBlank(message = "Surname cannot be blank")
    private String surname;

    @Schema(description = "Почта участника", example = "ivanov@mail.ru")
    @NotBlank(message = "Email cannot be blank")
    private String email;
}
