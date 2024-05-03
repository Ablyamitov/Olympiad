package com.example.olympiad.web.dto.contest.ResultTable;

import lombok.Data;

@Data
public class UserAnswers {
    private Long taskNumber;
    private String answerStatus;
    private Integer points;
}
