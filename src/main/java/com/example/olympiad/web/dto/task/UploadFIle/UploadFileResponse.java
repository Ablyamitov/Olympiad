package com.example.olympiad.web.dto.task.UploadFIle;

import lombok.Data;

import java.time.Instant;

@Data
public class UploadFileResponse {

    private Long session;
    private Long userId;
    private Long taskNumber;
    private String fileContent;
    private Instant sentTime;
    private String comment;
    private Integer points;
}
