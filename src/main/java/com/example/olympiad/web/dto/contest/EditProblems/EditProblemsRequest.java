package com.example.olympiad.web.dto.contest.EditProblems;

import com.example.olympiad.web.dto.contest.CreateContest.ProblemInfo;
import lombok.Data;

import java.util.List;
@Data
public class EditProblemsRequest {
    private Long session;
    List<ProblemInfo> problemInfos;
}
