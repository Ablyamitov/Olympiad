package com.cfuv.olympus.web.dto.contest.CreateContest;

import com.cfuv.olympus.domain.contest.Contest;
import lombok.Data;

@Data
public class ContestAndFileResponse {
    private Contest contest;
    private byte[] fileContent;
}
