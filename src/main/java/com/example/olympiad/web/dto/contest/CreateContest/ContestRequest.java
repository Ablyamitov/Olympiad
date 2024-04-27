package com.example.olympiad.web.dto.contest.CreateContest;

import lombok.Data;

import java.util.List;

@Data
public class ContestRequest {

    //private Long session;

    private String name;

    private int participantCount;

    private int judgeCount;

    private String usernamePrefix;

    private String duration;

    List<ProblemInfo> problemInfos;

}
