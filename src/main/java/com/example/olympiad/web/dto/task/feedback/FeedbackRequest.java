package com.example.olympiad.web.dto.task.feedback;


import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
@ValidFeedbackRequest
public class FeedbackRequest {

    @Min(value = 0, message = "UserTasksId must be at least 0")
    private Long userTasksId;

    boolean accepted;


    private Integer points;

    private String comment;

}


