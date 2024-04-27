package com.example.olympiad.web.dto.contest.AddProblems;

import com.example.olympiad.web.dto.contest.CreateContest.ProblemInfo;
import lombok.Data;

import java.util.List;
@Data
public class AddProblemsRequest {
    private Long session;
    List<ProblemInfo> problemInfos;
}
