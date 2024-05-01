package com.example.olympiad.web.dto.contest.GetStartAndEndContestTime;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class GetStartAndEndContestTimeRequest {
    @Min(value = 0, message = "Session cannot be less than 0")
    private Long session;
}
