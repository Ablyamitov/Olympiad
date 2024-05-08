package com.example.olympiad.web.dto.task.UploadFIle;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadFileRequest {
    private Long session;
    private Long userId;
    private Long taskNumber;
    private MultipartFile file;
    private String fileName;
    private String fileExtension;
}
