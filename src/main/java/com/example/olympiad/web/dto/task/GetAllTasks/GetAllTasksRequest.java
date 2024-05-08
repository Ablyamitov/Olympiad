package com.example.olympiad.web.dto.task.GetAllTasks;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class GetAllTasksRequest {

    @Min(value = 0, message = "Session must be at least 0")
    @Schema(description = "Сессия")
    private Long session;

    @Min(value = 0, message = "UserId must be at least 0")
    @Schema(description = "id участника")
    private Long userId;

    @Min(value = 0, message = "TaskNumber must be at least 0")
    @Schema(description = "Номер задания")
    private Long taskNumber;
}
