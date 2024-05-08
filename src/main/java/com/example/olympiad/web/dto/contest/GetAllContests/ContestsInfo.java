package com.example.olympiad.web.dto.contest.GetAllContests;

import com.example.olympiad.domain.contest.ContestState;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.ZonedDateTime;

@AllArgsConstructor
@Data
public class ContestsInfo {
    private String name;
    private Long session;
    private ContestState contestState;
    private String duration;
    private String startTime;
    private String endTime;
}
