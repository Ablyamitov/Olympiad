package com.example.olympiad.web.dto.task.Download;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DownloadRequest {

    @Min(value = 0, message = "UserId cannot be less than 0")
    private Long userId;

    @Min(value = 0, message = "UserTasksId cannot be less than 0")
    private Long userTasksId;

    @NotBlank(message = "FileName cannot be blank")
    private String fileName;
}
