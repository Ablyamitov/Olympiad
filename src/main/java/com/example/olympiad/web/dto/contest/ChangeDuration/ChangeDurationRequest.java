package com.example.olympiad.web.dto.contest.ChangeDuration;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class ChangeDurationRequest {
    private Long session;
    private String newDuration;
}
