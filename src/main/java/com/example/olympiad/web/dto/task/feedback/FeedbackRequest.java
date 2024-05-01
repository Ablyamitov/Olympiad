package com.example.olympiad.web.dto.task.feedback;


import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class FeedbackRequest {

    @Min(value = 0, message = "UserTasksId must be at least 0")
    private Long userTasksId;

    boolean accepted;

    @Min(value = 0, message = "Points must be at least 0")
    private Integer points;

    private String comment;

}
