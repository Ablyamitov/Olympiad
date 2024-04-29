package com.example.olympiad.web.dto.contest;

import com.example.olympiad.domain.contest.ContestState;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AllContestsNameSessionResponse {
    private String name;
    private Long session;
    private ContestState contestState;
}
