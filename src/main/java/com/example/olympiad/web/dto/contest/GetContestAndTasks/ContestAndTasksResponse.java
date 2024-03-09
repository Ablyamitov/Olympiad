package com.example.olympiad.web.dto.contest.GetContestAndTasks;

import com.example.olympiad.domain.contest.Contest;
import lombok.Data;

import java.util.List;

@Data
public class ContestAndTasksResponse {
    private Contest contest;
}
