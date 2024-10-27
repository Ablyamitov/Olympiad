package com.cfuv.olympus.web.dto.contest.EditProblems;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class DeleteProblemRequest {

    @Min(value = 0, message = "Id должно быть не меньше 0")
    private Long id;

    @Min(value = 0, message = "Сессия должна быть не меньше 0")
    @Schema(description = "Сессия")
    private Long session;
}
