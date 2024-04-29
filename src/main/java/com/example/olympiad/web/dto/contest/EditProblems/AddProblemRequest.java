package com.example.olympiad.web.dto.contest.EditProblems;

import com.example.olympiad.web.dto.contest.CreateContest.ProblemInfo;
import lombok.Data;

import java.util.List;

@Data
public class AddProblemRequest {
    private Long session;
    private String name;
    private String problem;
    private int points;
}
