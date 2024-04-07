package com.example.olympiad.web.dto.task.GetAllTasks;

import lombok.Data;

@Data
public class GetAllTasksRequest {
    private Long session;
    private Long userId;
    private Long taskNumber;
}
