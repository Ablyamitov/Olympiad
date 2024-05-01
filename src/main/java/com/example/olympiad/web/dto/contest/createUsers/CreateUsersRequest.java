package com.example.olympiad.web.dto.contest.createUsers;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class CreateUsersRequest {
    @Min(value = 0, message = "Session count must be at least 0")
    private Long session;

    @Min(value = 0, message = "Participant count must be at least 0")
    private int participantCount;

    @Min(value = 0, message = "Judge count must be at least 0")
    private int judgeCount;
}
