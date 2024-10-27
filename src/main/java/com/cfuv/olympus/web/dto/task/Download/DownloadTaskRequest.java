package com.cfuv.olympus.web.dto.task.Download;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DownloadTaskRequest {
    @Min(value = 0, message = "Сессия должна быть не меньше 0")
    @Schema(description = "Сессия")
    private Long session;

    @Min(value = 0, message = "Номер задания должен быть не меньше 0")
    @Schema(description = "Номер задания")
    private Long taskId;

    @NotBlank(message = "Имя файла не должно быть пустым")
    @Schema(description = "Название файла", example = "work1.zip")
    private String fileName;
}
