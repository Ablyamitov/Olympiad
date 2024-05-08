package com.example.olympiad.web.dto.task.Download;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DownloadUserTaskRequest {

    @Min(value = 0, message = "UserId cannot be less than 0")
    @Schema(description = "id пользователя")
    private Long userId;

    @Min(value = 0, message = "UserTasksId cannot be less than 0")
    @Schema(description = "id ответа участника")
    private Long userTasksId;

    @NotBlank(message = "FileName cannot be blank")
    @Schema(description = "Имя файла ответа участника", example = "index.js")
    private String fileName;
}
