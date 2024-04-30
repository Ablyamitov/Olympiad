package com.example.olympiad.web.dto.task;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;

@Data
public class UploadFileRequest {
    private Long session;
    private Long userId;
    private Long taskNumber;
    private MultipartFile file;
    private String fileName;
    private String fileExtension;
}
