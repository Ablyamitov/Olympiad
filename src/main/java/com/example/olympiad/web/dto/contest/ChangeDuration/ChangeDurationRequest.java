package com.example.olympiad.web.dto.contest.ChangeDuration;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ChangeDurationRequest {
    @Min(value = 0, message = "Session cannot be less than 0")
    @Schema(description = "Сессия")
    private Long session;
    @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", message = "newDuration должен быть в формате 'HH:mm'")
    @Schema(description = "Новая длительность олимпиады", example = "02:30")
    private String newDuration;
}
