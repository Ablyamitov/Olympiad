package com.example.olympiad.web.dto.task.GetAllTasks;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class GetAllTasksRequest {

    @Min(value = 0, message = "Сессия должна быть не меньше 0")
    @Schema(description = "Сессия")
    private Long session;

    @Min(value = 0, message = "id участника должен быть не меньше 0")
    @Schema(description = "id участника")
    private Long userId;

    @Min(value = 0, message = "Номер задания должен быть не меньше 0")
    @Schema(description = "Номер задания")
    private Long taskNumber;
}
