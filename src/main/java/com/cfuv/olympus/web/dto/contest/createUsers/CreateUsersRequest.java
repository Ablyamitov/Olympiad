package com.cfuv.olympus.web.dto.contest.createUsers;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class CreateUsersRequest {
    @Min(value = 0, message = "Сессия должна быть не меньше 0")
    @Schema(description = "Сессия")
    private Long session;

    @Min(value = 0, message = "Количество участников не должно быть меньше 0")
    @Max(value = 25, message = "Количество участников не должно превышать 25")
    @Schema(description = "Количество участников")
    private int participantCount;

    @Min(value = 0, message = "Количество жури не должно быть меньше 0")
    @Max(value = 25, message = "Количество жури не должно превышать 25")
    @Schema(description = "Количество жури")
    private int judgeCount;
}
