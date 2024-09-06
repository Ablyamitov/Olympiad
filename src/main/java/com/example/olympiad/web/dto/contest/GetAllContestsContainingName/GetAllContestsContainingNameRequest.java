package com.example.olympiad.web.dto.contest.GetAllContestsContainingName;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class GetAllContestsContainingNameRequest {
    @Min(value = 1, message = "Страница должна быть не меньше 1")
    @Schema(description = "Страница")
    private Integer page;
    @Schema(description = "Подстрока имени олимпиады", example = "2024")
    private String name;
    @Schema(description = "Состояние олимпиады", example = "NOT_STARTED, IN_PROGRESS, FINISHED")
    private String states;
}
