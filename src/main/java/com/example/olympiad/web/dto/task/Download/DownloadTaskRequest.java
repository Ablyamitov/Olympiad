package com.example.olympiad.web.dto.task.Download;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DownloadTaskRequest {
    @Min(value = 0, message = "session cannot be less than 0")
    private Long session;

    @Min(value = 0, message = "taskId cannot be less than 0")
    private Long taskId;

    @NotBlank(message = "FileName cannot be blank")
    private String fileName;
}
