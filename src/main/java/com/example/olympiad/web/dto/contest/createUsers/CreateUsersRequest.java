package com.example.olympiad.web.dto.contest.createUsers;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class CreateUsersRequest {
    private Long session;
    private int participantCount;
    private int judgeCount;
}
