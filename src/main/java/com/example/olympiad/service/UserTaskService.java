package com.example.olympiad.service;

import com.example.olympiad.domain.contest.Contest;
import com.example.olympiad.domain.contest.UserTaskState;
import com.example.olympiad.domain.contest.UserTasks;
import com.example.olympiad.domain.exception.entity.ContestNotFoundException;
import com.example.olympiad.repository.ContestRepository;
import com.example.olympiad.repository.UserTasksRepository;
import com.example.olympiad.web.dto.contest.JudgeTable.JudgeTableResponse;
import com.example.olympiad.web.dto.task.Download.DownloadUserTaskRequest;
import com.example.olympiad.web.dto.task.GetAllTasks.GetAllTasksRequest;
import com.example.olympiad.web.dto.task.UploadFIle.UploadFileRequest;
import com.example.olympiad.web.dto.task.UploadFIle.UploadFileResponse;
import com.github.junrar.exception.RarException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserTaskService {
    private final TaskService taskService;
    private final ContestRepository contestRepository;
    private final UserTasksRepository userTasksRepository;

    @Value("${storage.location}")
    private String UPLOAD_DIR;

    @Transactional
    public List<JudgeTableResponse> uploadFile(UploadFileRequest uploadFileRequest) throws IOException {

        Contest contest = contestRepository.findBySession(uploadFileRequest.getSession())
                .orElseThrow(() -> new ContestNotFoundException("Contest not found"));
        UploadFileResponse uploadFileResponse = new UploadFileResponse();
        uploadFileResponse.setSession(uploadFileRequest.getSession());
        uploadFileResponse.setUserId(uploadFileRequest.getUserId());
        uploadFileResponse.setTaskNumber(uploadFileRequest.getTaskNumber());
        String fileContent = Base64.getEncoder().encodeToString(uploadFileRequest.getFile().getBytes());
        uploadFileResponse.setFileContent(fileContent);
        uploadFileResponse.setSentTime(Instant.now());
        uploadFileResponse.setComment(null);
        uploadFileResponse.setPoints(null);

        UserTasks userTask = new UserTasks();
        Long idInSession = userTasksRepository.countBySession(uploadFileRequest.getSession()) + 1;
        userTask.setIdInSession(idInSession);
        userTask.setSession(uploadFileRequest.getSession());
        userTask.setUserId(uploadFileRequest.getUserId());
        userTask.setTaskNumber(uploadFileRequest.getTaskNumber());

        userTask.setFileContent(fileContent);


        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC+3"));
        ZonedDateTime startTime = contest.getStartTime();
        Duration duration = Duration.between(startTime, now);
        long hours = duration.toHours();
        long minutes = duration.minusHours(hours).toMinutes();
        long seconds = duration.minusHours(hours).minusMinutes(minutes).getSeconds();

        String timeDifference = String.format("%02d:%02d:%02d", hours, minutes, seconds);


        userTask.setSentTime(timeDifference);


        userTask.setFileName(uploadFileRequest.getFileName());
        userTask.setFileExtension(uploadFileRequest.getFileExtension());

        userTask.setComment(null);
        userTask.setPoints(null);
        userTask.setState(UserTaskState.NOT_EVALUATED);
        userTasksRepository.save(userTask);


        try {
            String userDir = UPLOAD_DIR + "user-tasks" + "/" + uploadFileRequest.getUserId().toString() + "/" + userTask.getId().toString() + "/";
            Path path = Paths.get(userDir);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }

            taskService.handleFile(uploadFileRequest.getFile().getInputStream(),
                    userDir,
                    uploadFileRequest.getFile().getOriginalFilename());
        } catch (IOException | RarException e) {
            throw new IOException(e.getMessage());
        }


        return getJudgeTableResponses(uploadFileRequest.getUserId(), userTask.getTaskNumber());
    }

    public List<JudgeTableResponse> getAllTasksByUserIdAndTaskNumber(GetAllTasksRequest getAllTasksRequest) {
        return getJudgeTableResponses(getAllTasksRequest.getUserId(), getAllTasksRequest.getTaskNumber());
    }

    private List<JudgeTableResponse> getJudgeTableResponses(Long userId, Long taskNumber) {
        List<UserTasks> userTasks = userTasksRepository
                .findAllByUserIdAndTaskNumberOrderByIdInSession(userId, taskNumber);
        List<JudgeTableResponse> judgeTableResponses = new ArrayList<>();
        for (UserTasks ut : userTasks) {
            JudgeTableResponse jtr = taskService.mapToJudgeTableResponse(ut);
            judgeTableResponses.add(jtr);
        }
        return judgeTableResponses;
    }

    public ResponseEntity<Resource> downloadFile(DownloadUserTaskRequest downloadRequest) throws Exception {
        Path file = Paths.get("uploads", "user-tasks", downloadRequest.getUserId().toString(), downloadRequest.getUserTasksId().toString(), downloadRequest.getFileName());
        return taskService.getResourceResponseEntity(file);
    }

}
