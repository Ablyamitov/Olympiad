package com.example.olympiad.web.dto.contest.CreateContest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ContestRequest {


    @NotBlank(message = "Имя не должно быть пустым")
    @Schema(description = "Имя олимпиады", example = "Web 2024")
    private String name;

    @Min(value = 0, message = "Количество участников не должно быть меньше 0")
    @Schema(description = "Количество участников")
    private int participantCount;

    @Min(value = 0, message = "Количество жюри не должно быть меньше 0")
    @Schema(description = "Количество жури")
    private int judgeCount;

    @NotBlank(message = "Префикс пользователей не должен быть пустым")
    @Schema(description = "Префикс пользователей олимпиады", example = "cweb")
    private String usernamePrefix;

    @NotBlank(message = "Длительность не должна быть пустой")
    @Schema(description = "Длительность олимпиады", example = "01:20")
    private String duration;


}
