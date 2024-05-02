package com.example.olympiad.service;

import com.example.olympiad.domain.contest.UserTaskState;
import com.example.olympiad.domain.contest.UserTasks;
import com.example.olympiad.repository.UserTasksRepository;
import com.example.olympiad.web.dto.contest.JudgeTable.JudgeTableResponse;
import com.example.olympiad.web.dto.task.Download.AdminDownloadProblemRequest;
import com.example.olympiad.web.dto.task.Download.DownloadRequest;
import com.example.olympiad.web.dto.task.GetAllTasks.GetAllTasksRequest;
import com.example.olympiad.web.dto.task.UploadFileRequest;
import com.example.olympiad.web.dto.task.UploadFileResponse;
import com.example.olympiad.web.dto.task.feedback.FeedbackRequest;
import com.github.junrar.Junrar;
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

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final Tika tika = new Tika();

    private final UserTasksRepository userTasksRepository;
    private final UserService userService;
    //private static final String UPLOAD_DIR = "uploads/";
    @Value("${storage.location}")
    private String UPLOAD_DIR;


    @Transactional
    public List<JudgeTableResponse> uploadFile(UploadFileRequest uploadFileRequest) throws IOException {

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
        userTask.setSession(uploadFileRequest.getSession());
        userTask.setUserId(uploadFileRequest.getUserId());
        userTask.setTaskNumber(uploadFileRequest.getTaskNumber());

        userTask.setFileContent(fileContent);

        userTask.setSentTime(ZonedDateTime.now(ZoneId.of("UTC+3")));

        userTask.setFileName(uploadFileRequest.getFileName());
        userTask.setFileExtension(uploadFileRequest.getFileExtension());

        userTask.setComment(null);
        userTask.setPoints(null);
        userTask.setState(UserTaskState.NOT_EVALUATED);
        userTasksRepository.save(userTask);


        try {
            String userDir = UPLOAD_DIR + uploadFileRequest.getUserId().toString() + "/" + userTask.getId().toString() + "/";
            Path path = Paths.get(userDir);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }

            handleFile(uploadFileRequest.getFile().getInputStream(),
                    userDir,
                    uploadFileRequest.getFile().getOriginalFilename());
            //unzipFile(uploadFileRequest.getFile().getInputStream(), userDir);
        } catch (IOException | RarException e) {
            throw new IOException(e.getMessage());
        }


        return getJudgeTableResponses(uploadFileRequest.getUserId(), userTask.getTaskNumber());
    }

    private List<JudgeTableResponse> getJudgeTableResponses(Long userId, Long taskNumber) {
        List<UserTasks> userTasks = userTasksRepository
                .findAllByUserIdAndTaskNumber(userId, taskNumber);
        List<JudgeTableResponse> judgeTableResponses = new ArrayList<>();
        for (UserTasks ut : userTasks) {
            JudgeTableResponse jtr = mapToJudgeTableResponse(ut);
            judgeTableResponses.add(jtr);
        }
        return judgeTableResponses;
    }

    public void handleFile(InputStream fileStream, String destDir, String fileName) throws IOException, RarException {

        // Сохраняем InputStream во временный файл
        Path tempPath = Files.createTempFile("temp", null);
        Files.copy(fileStream, tempPath, StandardCopyOption.REPLACE_EXISTING);

        // Определяем тип файла
        Tika tika = new Tika();
        String fileType = tika.detect(tempPath);

        // Обрабатываем файл в зависимости от его типа
//        if (fileType.equals("application/zip")) {
//            unzipFile(Files.newInputStream(tempPath), destDir);
//        } else {
//            saveFile(Files.newInputStream(tempPath), destDir, fileName);
//        }
        saveFile(Files.newInputStream(tempPath), destDir, fileName);

        // Удаляем временный файл
        Files.delete(tempPath);

    }

    private void saveFile(InputStream fileStream, String destDir, String fileName) throws IOException {
        // Создаем путь для сохранения файла
        Path filePath = Paths.get(destDir, fileName);

        // Сохраняем содержимое InputStream в файл
        Files.copy(fileStream, filePath);
    }

    private void extractRar(InputStream rarStream, String destDir) throws IOException, RarException {
        File destinationFolder = new File(destDir);
        Junrar.extract(rarStream, destinationFolder);
    }

    private void unzipFile(InputStream zipStream, String destDir) {
        try (ZipInputStream zis = new ZipInputStream(zipStream)) {


            Files.walk(Paths.get(destDir))
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .forEach(File::delete);


            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                String filePath = destDir + File.separator + zipEntry.getName();
                if (!zipEntry.isDirectory()) {
                    extractFile(zis, filePath);
                } else {
                    Files.createDirectories(Paths.get(filePath));
                }
                zipEntry = zis.getNextEntry();
            }
        } catch (IOException ignored) {
        }
    }


    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[104857610];
        int read;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

    public List<JudgeTableResponse> getAllTasksByUserIdAndTaskNumber(GetAllTasksRequest getAllTasksRequest) {
        return getJudgeTableResponses(getAllTasksRequest.getUserId(), getAllTasksRequest.getTaskNumber());
    }

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

    public ResponseEntity<Resource> downloadFile(DownloadRequest downloadRequest) throws Exception {
        Path file = Paths.get("uploads", downloadRequest.getUserId().toString(), downloadRequest.getUserTasksId().toString(), downloadRequest.getFileName());
        return getResourceResponseEntity(file);
    }

    public ResponseEntity<Resource> downloadFile(AdminDownloadProblemRequest adminDownloadProblemRequest) throws Exception {
        Path file = Paths.get("uploads", adminDownloadProblemRequest.getSession().toString(), adminDownloadProblemRequest.getTaskId().toString(), adminDownloadProblemRequest.getFileName());
        return getResourceResponseEntity(file);
    }

    private ResponseEntity<Resource> getResourceResponseEntity(Path file) throws IOException {
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
        List<UserTasks> userTasks = userTasksRepository.findAllBySession(session);
        List<JudgeTableResponse> judgeTableResponses = new ArrayList<>();
        for (UserTasks ut : userTasks) {
            JudgeTableResponse jtr = mapToJudgeTableResponse(ut);
            judgeTableResponses.add(jtr);
        }

        return judgeTableResponses;

    }

    private JudgeTableResponse mapToJudgeTableResponse(UserTasks ut) {
        JudgeTableResponse jtr = new JudgeTableResponse();
        jtr.setId(ut.getId());
        jtr.setSession(ut.getSession());
        jtr.setUserId(ut.getUserId());
        jtr.setUserName(userService.getByUserId(ut.getUserId()).getUsername());
        jtr.setTaskNumber(ut.getTaskNumber());
        jtr.setPoints(ut.getPoints());
        jtr.setComment(ut.getComment());
        jtr.setSentTime(ut.getSentTime());
        jtr.setFileName(ut.getFileName());
        //jtr.setFileExtension(ut.getFileExtension());
        jtr.setState(ut.getState().name());
        return jtr;
    }
}
