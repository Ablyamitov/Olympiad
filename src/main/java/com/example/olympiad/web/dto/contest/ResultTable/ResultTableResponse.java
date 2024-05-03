package com.example.olympiad.web.dto.contest.ResultTable;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
public class ResultTableResponse {
    private List<Users> users;
    private int tasksCount;
}
