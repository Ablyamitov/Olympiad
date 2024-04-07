package com.example.olympiad.web.dto.task;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

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
