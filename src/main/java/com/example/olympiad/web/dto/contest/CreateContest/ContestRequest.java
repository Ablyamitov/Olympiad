package com.example.olympiad.web.dto.contest.CreateContest;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ContestRequest {


    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Min(value = 0, message = "Participant count must be at least 0")
    private int participantCount;

    @Min(value = 0, message = "Judge count must be at least 0")
    private int judgeCount;

    @NotBlank(message = "Username prefix cannot be blank")
    private String usernamePrefix;

    @NotBlank(message = "Duration cannot be blank")
    private String duration;

    //List<ProblemInfo> problemInfos;

}
