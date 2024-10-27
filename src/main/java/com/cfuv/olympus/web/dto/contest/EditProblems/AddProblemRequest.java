package com.cfuv.olympus.web.dto.contest.EditProblems;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AddProblemRequest {

    @Min(value = 0, message = "Сессия должна быть не меньше 0")
    private Long session;

    @NotBlank(message = "Имя html файла не должно быть пустым")
    private String htmlName;

    private String htmlContent;

    @NotBlank(message = "Имя задания не должно быть пустым")
    private String name;

    private MultipartFile problem;

    @Min(value = 0, message = "Количество очков должно быть не меньше 0")
    private int points;
}
