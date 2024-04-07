package com.example.olympiad.web.dto.contest.GetStartAndEndContestTime;

import lombok.Data;

import java.time.Instant;
import java.time.ZonedDateTime;

@Data
public class GetStartAndEndContestTimeResponse {
    ZonedDateTime startTime;
    ZonedDateTime endTime;
}
