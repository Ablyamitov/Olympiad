package com.example.olympiad.web.dto.contest.createUsers;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class CreateUsersRequest {
    @Min(value = 0, message = "Сессия должна быть не меньше 0")
    @Schema(description = "Сессия")
    private Long session;

    @Min(value = 0, message = "Participant count must be at least 0")
    @Schema(description = "Количество участников")
    private int participantCount;

    @Min(value = 0, message = "Judge count must be at least 0")
    @Schema(description = "Количество жури")
    private int judgeCount;
}
