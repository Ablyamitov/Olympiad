package com.cfuv.olympus.web.dto.contest.ResultTable;

import lombok.Data;

import java.util.List;

@Data
public class ResultTableResponse {
    private List<Users> users;
    private int tasksCount;
}
