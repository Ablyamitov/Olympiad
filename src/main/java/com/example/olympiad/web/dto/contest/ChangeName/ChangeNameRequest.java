package com.example.olympiad.web.dto.contest.ChangeName;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ChangeNameRequest {
    @Min(value = 0, message = "Session cannot be less than 0")
    @Schema(description = "Сессия")
    private Long session;
    @NotBlank(message = "name cannot be blank")
    @Schema(description = "Новое имя олимпиады", example = "Web 2025")
    private String name;
}
