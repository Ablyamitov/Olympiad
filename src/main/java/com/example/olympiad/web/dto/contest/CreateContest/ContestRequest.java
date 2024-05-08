package com.example.olympiad.web.dto.contest.CreateContest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ContestRequest {


    @NotBlank(message = "Name cannot be blank")
    @Schema(description = "Имя олимпиады", example = "Web 2024")
    private String name;

    @Min(value = 0, message = "Participant count must be at least 0")
    @Schema(description = "Количество участников")
    private int participantCount;

    @Min(value = 0, message = "Judge count must be at least 0")
    @Schema(description = "Количество жури")
    private int judgeCount;

    @NotBlank(message = "Username prefix cannot be blank")
    @Schema(description = "Префикс пользователей олимпиады", example = "cweb")
    private String usernamePrefix;

    @NotBlank(message = "Duration cannot be blank")
    @Schema(description = "Длительность олимпиады", example = "01:20")
    private String duration;


}
