package com.cfuv.olympus.web.dto.contest.GetStartAndEndContestTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.Instant;
import java.time.ZonedDateTime;

@Data
public class GetStartAndEndContestTimeResponse {
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX")
    private ZonedDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX")
    private ZonedDateTime endTime;
}
