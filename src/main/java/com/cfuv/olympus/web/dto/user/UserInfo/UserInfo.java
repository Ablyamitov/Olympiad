package com.cfuv.olympus.web.dto.user.UserInfo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserInfo {

    @NotBlank(message = "Имя пользователя не должно быть пустым")
    @Schema(description = "Ник участника", example = "cweb_1_1")
    private String username;

    @NotBlank(message = "Имя участника не должно быть пустым")
    @Schema(description = "Имя участника", example = "Иван")
    private String name;

    @Schema(description = "Фамилия участника", example = "Иванов")
    @NotBlank(message = "Фамилия участника не должна быть пустой")
    private String surname;

    @Schema(description = "Почта участника", example = "ivanov@mail.ru")
    @NotBlank(message = "Почта участника не должна быть пустой")
    private String email;
}
