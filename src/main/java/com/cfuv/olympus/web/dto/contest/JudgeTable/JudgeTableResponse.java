package com.cfuv.olympus.web.dto.contest.JudgeTable;

import lombok.Data;

@Data
public class JudgeTableResponse {

    private Long id;

    private Long answerId;

    private Long session;

    private Long userId;

    private String userName;

    private Long taskNumber;


    private Integer points;

    private Integer maxPoints;

    private String comment;

    private String sentTime;

    private String fileName;


    private Integer state;

}
