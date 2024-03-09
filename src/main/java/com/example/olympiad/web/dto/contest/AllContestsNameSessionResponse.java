package com.example.olympiad.web.dto.contest;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AllContestsNameSessionResponse {
    private String name;
    private Long session;
}
