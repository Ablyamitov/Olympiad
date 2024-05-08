package com.example.olympiad.web.dto.contest.EditProblems;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class DeleteProblemRequest {

    @Min(value = 0, message = "Id cannot be less than 0")
    private Long id;

    @Min(value = 0, message = "Session cannot be less than 0")
    @Schema(description = "Сессия")
    private Long session;
}
