package com.example.olympiad.web.dto.task.GetAllTasks;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class GetAllTasksRequest {

    @Min(value = 0, message = "Session must be at least 0")
    private Long session;

    @Min(value = 0, message = "UserId must be at least 0")
    private Long userId;

    @Min(value = 0, message = "TaskNumber must be at least 0")
    private Long taskNumber;
}
