package com.example.olympiad.web.dto.task.feedback;

import lombok.Data;

@Data
public class FeedbackResponse {
    private Long session;
    private Long userId;
    private Long taskNumber;
    private Integer points;
    private String comment;
}
