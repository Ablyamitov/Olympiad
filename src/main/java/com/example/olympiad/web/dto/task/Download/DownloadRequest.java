package com.example.olympiad.web.dto.task.Download;

import lombok.Data;

@Data
public class DownloadRequest {
    private Long userId;
    private Long userTasksId;
    private String fileName;
}
