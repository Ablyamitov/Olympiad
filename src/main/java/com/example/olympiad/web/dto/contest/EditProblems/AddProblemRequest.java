package com.example.olympiad.web.dto.contest.EditProblems;

import com.example.olympiad.web.dto.contest.CreateContest.ProblemInfo;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class AddProblemRequest {

    @Min(value = 0, message = "Session cannot be less than 0")
    private Long session;

    @NotBlank(message = "htmlName cannot be blank")
    private String htmlName;

    private String htmlContent;

    @NotBlank(message = "Name cannot be blank")
    private String name;

//    @NotBlank(message = "Problem cannot be blank")
//    private String problem;
    private MultipartFile problem;
//

    @Min(value = 0, message = "Points cannot be less than 0")
    private int points;
}
