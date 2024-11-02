package com.cfuv.olympus.web.dto.contest.ChangeName;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ChangeNameRequest {
    @NotBlank(message = "Новое имя не должно быть пустым")
    @Schema(description = "Новое имя олимпиады", example = "Web 2025")
    private String name;
}
