package com.example.olympiad.web.dto.task.Download;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DownloadTaskRequest {
    @Min(value = 0, message = "session cannot be less than 0")
    @Schema(description = "Сессия")
    private Long session;

    @Min(value = 0, message = "taskId cannot be less than 0")
    @Schema(description = "Номер задания")
    private Long taskId;

    @NotBlank(message = "FileName cannot be blank")
    @Schema(description = "Название файла", example = "work1.zip")
    private String fileName;
}
