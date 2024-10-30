package com.cfuv.olympus.service;

import com.cfuv.olympus.domain.contest.UserTasks;
import com.cfuv.olympus.repository.UserTasksRepository;
import com.cfuv.olympus.domain.contest.UserTaskState;
import com.cfuv.olympus.domain.user.Role;
import com.cfuv.olympus.domain.user.User;
import com.cfuv.olympus.repository.TasksRepository;
import com.cfuv.olympus.repository.UserRepository;
import com.cfuv.olympus.web.dto.contest.JudgeTable.JudgeTableResponse;
import com.cfuv.olympus.web.dto.contest.ResultTable.AnswerStatus;
import com.cfuv.olympus.web.dto.contest.ResultTable.ResultTableResponse;
import com.cfuv.olympus.web.dto.contest.ResultTable.UserAnswers;
import com.cfuv.olympus.web.dto.contest.ResultTable.Users;
import com.cfuv.olympus.web.dto.task.Download.DownloadTaskRequest;
import com.github.junrar.exception.RarException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.tika.Tika;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final Tika tika = new Tika();

    private final UserTasksRepository userTasksRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final TasksRepository tasksRepository;

    public void handleFile(InputStream fileStream, String destDir, String fileName) throws IOException, RarException {

        Path tempPath = Files.createTempFile("temp", null);
        Files.copy(fileStream, tempPath, StandardCopyOption.REPLACE_EXISTING);

        saveFile(Files.newInputStream(tempPath), destDir, fileName);
        Files.delete(tempPath);

    }

    public void handleAddProblemFile(InputStream fileStream, String photoDir, String fileName) throws IOException, RarException {

        Path tempPath = Files.createTempFile("temp", ".zip");
        Files.copy(fileStream, tempPath, StandardCopyOption.REPLACE_EXISTING);

        try (ZipFile zipFile = new ZipFile(tempPath.toFile())) {
            Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();
            while (entries.hasMoreElements()) {
                ZipArchiveEntry entry = entries.nextElement();
                Path filePath;
                if (isPhoto(entry.getName())) {
                    filePath = Paths.get(photoDir, entry.getName());
                } else {
                    //filePath = Paths.get(tasksDir, entry.getName());
                    throw new IOException(String.format("Expected file type png or jpg, received - %s", entry.getName()));
                }

                if (entry.isDirectory()) {
                    Files.createDirectories(filePath);
                } else {
                    Files.createDirectories(filePath.getParent());
                    try (InputStream entryStream = zipFile.getInputStream(entry);
                         FileOutputStream outputStream = new FileOutputStream(filePath.toFile())) {
                        IOUtils.copy(entryStream, outputStream);
                    }
                }

            }
        } finally {
            Files.delete(tempPath);
        }



    }

    private boolean isPhoto(String fileName) {
        String lowerCaseName = fileName.toLowerCase();
        return lowerCaseName.endsWith(".jpg") || lowerCaseName.endsWith(".jpeg") || lowerCaseName.endsWith(".png") || lowerCaseName.endsWith(".gif");
    }



    private void saveFile(InputStream fileStream, String destDir, String fileName) throws IOException {
        Path filePath = Paths.get(destDir, fileName);
        Files.copy(fileStream, filePath);
    }




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
            throw new RuntimeException("Не удалось прочитать файл!");
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
            if (u.getRoles().contains(Role.ROLE_JUDGE)) continue;
            Users userInfo = new Users();
            if(u.getName()==null || u.getSurname()==null) userInfo.setName("Аноним");
            else userInfo.setName(u.getName() + " "  + u.getSurname());
            userInfo.setUsername(u.getUsername());
            userInfo.setEmail(u.getEmail());

            List<UserTasks> userTasks = userTasksRepository.findAllLatestTasksBySessionAndUserId(u.getSession(), u.getId());
            userTasks.sort(Comparator.comparing(UserTasks::getTaskNumber));
            List<UserAnswers> userAnswers = new ArrayList<>();

            int totalPoints = 0;
            for (int taskNumber = 0; taskNumber < tasksCount; taskNumber++) {
                UserAnswers userAnswer = new UserAnswers();
                //что-то с балами
                //if (taskNumber + 1 != userTasks.get(taskNumber).getTaskNumber()) {
                if (userTasks.isEmpty() || taskNumber >= userTasks.size() || taskNumber + 1 != userTasks.get(taskNumber).getTaskNumber()) {
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
        /**/
        users.sort(Comparator.comparingInt(Users::getTotalPoints).reversed());

        int currentPlace = 0;
        int previousTotalPoints = Integer.MAX_VALUE;

        for (Users user : users) {
            if (user.getTotalPoints() < previousTotalPoints) {
                currentPlace++;
                previousTotalPoints = user.getTotalPoints();
            }
            user.setPlace(currentPlace);
        }


        /**/

        resultTableResponse.setUsers(users);
        resultTableResponse.setTasksCount(tasksCount);

        return resultTableResponse;
    }
}
