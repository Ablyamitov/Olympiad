package com.example.olympiad.web.dto.task.feedback;


import lombok.Data;

@Data
public class FeedbackRequest {
    private Long userTasksId;
    boolean accepted;
    private Integer points;
    private String comment;


}
