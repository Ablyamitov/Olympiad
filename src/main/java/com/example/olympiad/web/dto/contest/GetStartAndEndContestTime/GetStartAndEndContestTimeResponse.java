package com.example.olympiad.web.dto.contest.GetStartAndEndContestTime;

import lombok.Data;

import java.time.Instant;

@Data
public class GetStartAndEndContestTimeResponse {
    Instant startTime;
    Instant endTime;
}
