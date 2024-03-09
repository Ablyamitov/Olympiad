package com.example.olympiad.web.dto.contest.CreateContest;

import com.example.olympiad.domain.contest.Contest;
import com.example.olympiad.domain.contest.Tasks;
import lombok.Data;

import java.io.File;
import java.util.List;

@Data
public class ContestResponse {
    private Contest contest;
    private File file;
    private List<Tasks> tasks;
}
