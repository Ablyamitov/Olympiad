package com.example.olympiad.service;

import com.example.olympiad.domain.contest.Contest;
import com.example.olympiad.domain.contest.UserTaskState;
import com.example.olympiad.domain.contest.UserTasks;
import com.example.olympiad.domain.exception.entity.ContestNotFoundException;
import com.example.olympiad.domain.user.User;
import com.example.olympiad.repository.ContestRepository;
import com.example.olympiad.repository.TasksRepository;
import com.example.olympiad.repository.UserRepository;
import com.example.olympiad.repository.UserTasksRepository;
import com.example.olympiad.web.dto.contest.JudgeTable.JudgeTableResponse;
import com.example.olympiad.web.dto.contest.ResultTable.AnswerStatus;
import com.example.olympiad.web.dto.contest.ResultTable.ResultTableResponse;
import com.example.olympiad.web.dto.contest.ResultTable.UserAnswers;
import com.example.olympiad.web.dto.contest.ResultTable.Users;
import com.example.olympiad.web.dto.task.Download.DownloadTaskRequest;
import com.example.olympiad.web.dto.task.Download.DownloadUserTaskRequest;
import com.example.olympiad.web.dto.task.GetAllTasks.GetAllTasksRequest;
import com.example.olympiad.web.dto.task.UploadFIle.UploadFileRequest;
import com.example.olympiad.web.dto.task.UploadFIle.UploadFileResponse;
import com.example.olympiad.web.dto.task.feedback.FeedbackRequest;
import com.github.junrar.exception.RarException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final Tika tika = new Tika();

    private final UserTasksRepository userTasksRepository;
    private final UserService userService;
    private final ContestRepository contestRepository;
    private final UserRepository userRepository;
    private final TasksRepository tasksRepository;
//    @Value("${storage.location}")
//    private String UPLOAD_DIR;


//    @Transactional
//    public List<JudgeTableResponse> uploadFile(UploadFileRequest uploadFileRequest) throws IOException {
//
//        Contest contest = contestRepository.findBySession(uploadFileRequest.getSession())
//                .orElseThrow(() -> new ContestNotFoundException("Contest not found"));
//        UploadFileResponse uploadFileResponse = new UploadFileResponse();
//        uploadFileResponse.setSession(uploadFileRequest.getSession());
//        uploadFileResponse.setUserId(uploadFileRequest.getUserId());
//        uploadFileResponse.setTaskNumber(uploadFileRequest.getTaskNumber());
//        String fileContent = Base64.getEncoder().encodeToString(uploadFileRequest.getFile().getBytes());
//        uploadFileResponse.setFileContent(fileContent);
//        uploadFileResponse.setSentTime(Instant.now());
//        uploadFileResponse.setComment(null);
//        uploadFileResponse.setPoints(null);
//
//        UserTasks userTask = new UserTasks();
//        Long idInSession = userTasksRepository.countBySession(uploadFileRequest.getSession()) + 1;
//        userTask.setIdInSession(idInSession);
//        userTask.setSession(uploadFileRequest.getSession());
//        userTask.setUserId(uploadFileRequest.getUserId());
//        userTask.setTaskNumber(uploadFileRequest.getTaskNumber());
//
//        userTask.setFileContent(fileContent);
//
//
//        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC+3"));
//        ZonedDateTime startTime = contest.getStartTime();
//        Duration duration = Duration.between(startTime, now);
//        long hours = duration.toHours();
//        long minutes = duration.minusHours(hours).toMinutes();
//        long seconds = duration.minusHours(hours).minusMinutes(minutes).getSeconds();
//
//        String timeDifference = String.format("%02d:%02d:%02d", hours, minutes, seconds);
//
//
//        userTask.setSentTime(timeDifference);
//
//
//        userTask.setFileName(uploadFileRequest.getFileName());
//        userTask.setFileExtension(uploadFileRequest.getFileExtension());
//
//        userTask.setComment(null);
//        userTask.setPoints(null);
//        userTask.setState(UserTaskState.NOT_EVALUATED);
//        userTasksRepository.save(userTask);
//
//
//        try {
//            String userDir = UPLOAD_DIR + "user-tasks" + "/" + uploadFileRequest.getUserId().toString() + "/" + userTask.getId().toString() + "/";
//            Path path = Paths.get(userDir);
//            if (!Files.exists(path)) {
//                Files.createDirectories(path);
//            }
//
//            handleFile(uploadFileRequest.getFile().getInputStream(),
//                    userDir,
//                    uploadFileRequest.getFile().getOriginalFilename());
//        } catch (IOException | RarException e) {
//            throw new IOException(e.getMessage());
//        }
//
//
//        return getJudgeTableResponses(uploadFileRequest.getUserId(), userTask.getTaskNumber());
//    }

//    List<JudgeTableResponse> getJudgeTableResponses(Long userId, Long taskNumber) {
//        List<UserTasks> userTasks = userTasksRepository
//                .findAllByUserIdAndTaskNumberOrderByIdInSession(userId, taskNumber);
//        List<JudgeTableResponse> judgeTableResponses = new ArrayList<>();
//        for (UserTasks ut : userTasks) {
//            JudgeTableResponse jtr = mapToJudgeTableResponse(ut);
//            judgeTableResponses.add(jtr);
//        }
//        return judgeTableResponses;
//    }

    public void handleFile(InputStream fileStream, String destDir, String fileName) throws IOException, RarException {

        Path tempPath = Files.createTempFile("temp", null);
        Files.copy(fileStream, tempPath, StandardCopyOption.REPLACE_EXISTING);

        saveFile(Files.newInputStream(tempPath), destDir, fileName);
        Files.delete(tempPath);

    }

    private void saveFile(InputStream fileStream, String destDir, String fileName) throws IOException {
        Path filePath = Paths.get(destDir, fileName);

        Files.copy(fileStream, filePath);
    }


//    public List<JudgeTableResponse> getAllTasksByUserIdAndTaskNumber(GetAllTasksRequest getAllTasksRequest) {
//        return getJudgeTableResponses(getAllTasksRequest.getUserId(), getAllTasksRequest.getTaskNumber());
//    }

    @Transactional
    public JudgeTableResponse feedback(FeedbackRequest feedbackRequest) {
        UserTasks ut = userTasksRepository.findById(feedbackRequest.getUserTasksId())
                .orElseThrow(() -> new EntityNotFoundException("User task not found"));
        if (feedbackRequest.isAccepted()) {
            ut.setPoints(feedbackRequest.getPoints());
            ut.setComment(feedbackRequest.getComment());
            ut.setState(UserTaskState.ACCEPTED);
        } else {
            ut.setPoints(0);
            ut.setComment(feedbackRequest.getComment());
            ut.setState(UserTaskState.REJECTED);
        }


        userTasksRepository.save(ut);


        return mapToJudgeTableResponse(ut);
    }

//    public ResponseEntity<Resource> downloadFile(DownloadUserTaskRequest downloadRequest) throws Exception {
//        Path file = Paths.get("uploads", "user-tasks", downloadRequest.getUserId().toString(), downloadRequest.getUserTasksId().toString(), downloadRequest.getFileName());
//        return getResourceResponseEntity(file);
//    }

    public ResponseEntity<Resource> downloadFile(DownloadTaskRequest downloadTaskRequest) throws Exception {
        Path file = Paths.get("uploads", "tasks", downloadTaskRequest.getSession().toString(), downloadTaskRequest.getTaskId().toString(), downloadTaskRequest.getFileName());
        return getResourceResponseEntity(file);
    }

    ResponseEntity<Resource> getResourceResponseEntity(Path file) throws IOException {
        Resource resource = new UrlResource(file.toUri());

        if (resource.exists() || resource.isReadable()) {
            String mimeType = tika.detect(file);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mimeType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } else {
            throw new RuntimeException("Could not read the file!");
        }
    }

    public List<JudgeTableResponse> getJudgeTableBySession(Long session) {
        List<UserTasks> userTasks = userTasksRepository.findAllBySessionOrderByIdInSession(session);
        List<JudgeTableResponse> judgeTableResponses = new ArrayList<>();
        for (UserTasks ut : userTasks) {
            JudgeTableResponse jtr = mapToJudgeTableResponse(ut);
            judgeTableResponses.add(jtr);
        }

        return judgeTableResponses;

    }

    JudgeTableResponse mapToJudgeTableResponse(UserTasks ut) {
        JudgeTableResponse jtr = new JudgeTableResponse();
        jtr.setId(ut.getId());
        jtr.setAnswerId(ut.getIdInSession());
        jtr.setSession(ut.getSession());
        jtr.setUserId(ut.getUserId());
        jtr.setUserName(userService.getByUserId(ut.getUserId()).getUsername());
        jtr.setTaskNumber(ut.getTaskNumber());
        jtr.setPoints(ut.getPoints());
        jtr.setComment(ut.getComment());
        jtr.setSentTime(ut.getSentTime());
        jtr.setFileName(ut.getFileName());
        jtr.setState(ut.getState().name());
        return jtr;
    }

    public ResultTableResponse getResultTableResponse(Long session) {
        ResultTableResponse resultTableResponse = new ResultTableResponse();
        List<User> fullUsers = userRepository.findAllBySession(session);
        int tasksCount = tasksRepository.findAllBySession(session).size();
        List<Users> users = new ArrayList<>();
        for (User u : fullUsers) {
            Users userInfo = new Users();
            userInfo.setName(u.getName());
            userInfo.setSurname(u.getSurname());
            userInfo.setEmail(u.getEmail());

            List<UserTasks> userTasks = userTasksRepository.findAllLatestTasksBySessionAndUserId(u.getSession(), u.getId());
            List<UserAnswers> userAnswers = new ArrayList<>();

            int totalPoints = 0;
            for (int taskNumber = 0; taskNumber < tasksCount; taskNumber++) {
                UserAnswers userAnswer = new UserAnswers();
                if (taskNumber + 1 != userTasks.get(taskNumber).getTaskNumber()) {
                    userAnswer.setTaskNumber((long) (taskNumber + 1));
                    userAnswer.setAnswerStatus(AnswerStatus.NOT_SENT.name());
                    userAnswer.setPoints(null);
                } else {
                    userAnswer.setTaskNumber(userTasks.get(taskNumber).getTaskNumber());
                    if (userTasks.get(taskNumber).getState() == UserTaskState.NOT_EVALUATED) {
                        userAnswer.setAnswerStatus(AnswerStatus.IN_PROGRESS.name());
                        userAnswer.setPoints(null);
                    } else {
                        userAnswer.setAnswerStatus(AnswerStatus.CHECKED.name());
                        userAnswer.setPoints(userTasks.get(taskNumber).getPoints());
                        totalPoints += userAnswer.getPoints();
                    }
                }

                userAnswers.add(userAnswer);
            }
            userInfo.setUserAnswers(userAnswers);


            userInfo.setSolvedTasksCount(userTasks.size());
            userInfo.setTotalPoints(totalPoints);


            users.add(userInfo);
        }
        resultTableResponse.setUsers(users);
        resultTableResponse.setTasksCount(tasksCount);

        return resultTableResponse;
    }
}
