package com.cfuv.olympus.web.dto.contest.GetAllContests;

import com.cfuv.olympus.domain.contest.ContestState;
import lombok.AllArgsConstructor;
import lombok.Data;

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
