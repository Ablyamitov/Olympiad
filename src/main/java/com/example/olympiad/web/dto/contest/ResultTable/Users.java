package com.example.olympiad.web.dto.contest.ResultTable;

import lombok.Data;

import java.util.List;

@Data
public class Users {
    private String name;
    //private String surname;
    private String username;
    private String email;
    private List<UserAnswers> userAnswers;
    private int solvedTasksCount;
    private Integer totalPoints;
    private int place;
}
