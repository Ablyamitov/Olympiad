package com.example.olympiad.web.dto.contest.CreateContest;

import com.example.olympiad.domain.contest.Contest;
import com.example.olympiad.domain.contest.Tasks;
import lombok.Data;

import java.util.List;

@Data
public class ContestAndFileResponse {
    private Contest contest;
    private byte[] fileContent;
}
