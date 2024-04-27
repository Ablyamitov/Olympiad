package com.example.olympiad.web.dto.contest.CreateContest;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ContestRequest {

    //private Long session;

    private String name;

    private int participantCount;

    private int judgeCount;

    private String usernamePrefix;

    private Long duration;

    List<String> problems;

}
