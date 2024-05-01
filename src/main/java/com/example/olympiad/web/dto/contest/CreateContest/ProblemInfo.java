package com.example.olympiad.web.dto.contest.CreateContest;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProblemInfo {
    private String name;
    private MultipartFile problem;
    private int points;
}
