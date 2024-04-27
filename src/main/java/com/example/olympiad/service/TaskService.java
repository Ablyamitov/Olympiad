package com.example.olympiad.service;

import com.example.olympiad.domain.contest.UserTasks;
import com.example.olympiad.repository.UserTasksRepository;
import com.example.olympiad.web.dto.task.GetAllTasks.GetAllTasksRequest;
import com.example.olympiad.web.dto.task.UploadFileRequest;
import com.example.olympiad.web.dto.task.UploadFileResponse;
import com.example.olympiad.web.dto.task.feedback.FeedbackRequest;
import com.example.olympiad.web.dto.task.feedback.FeedbackResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final UserTasksRepository userTasksRepository;
    private static String UPLOAD_DIR = "uploads/";



    @Transactional
    public List<UserTasks> uploadFile(UploadFileRequest uploadFileRequest) throws IOException {

        UploadFileResponse uploadFileResponse = new UploadFileResponse();
        uploadFileResponse.setSession(uploadFileRequest.getSession());
        uploadFileResponse.setUserId(uploadFileRequest.getUserId());
        uploadFileResponse.setTaskNumber(uploadFileRequest.getTaskNumber());
        String fileContent = Base64.getEncoder().encodeToString(uploadFileRequest.getFile().getBytes());
        uploadFileResponse.setFileContent(fileContent);
        uploadFileResponse.setSentTime(Instant.now());
        uploadFileResponse.setComment(null);
        uploadFileResponse.setPoints(null);

        UserTasks userTasks = new UserTasks();
        userTasks.setSession(uploadFileRequest.getSession());
        userTasks.setUserId(uploadFileRequest.getUserId());
        userTasks.setTaskNumber(uploadFileRequest.getTaskNumber());

        userTasks.setFileContent(fileContent);

        userTasks.setSentTime(ZonedDateTime.now(ZoneId.of("UTC+3")));

        userTasks.setComment(null);
        userTasks.setPoints(null);
        userTasksRepository.save(userTasks);



        try {
            String userDir = UPLOAD_DIR + uploadFileRequest.getUserId().toString() + "/";
            Path path = Paths.get(userDir);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }

            handleFile(uploadFileRequest.getFile().getInputStream(),
                    userDir,
                    uploadFileRequest.getFile().getOriginalFilename());
            //unzipFile(uploadFileRequest.getFile().getInputStream(), userDir);
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }


        return userTasksRepository.findAllByUserIdAndTaskNumber(uploadFileRequest.getUserId(), userTasks.getTaskNumber());
    }

    private void handleFile(InputStream fileStream, String destDir,String fileName) throws IOException {
        Tika tika = new Tika();
        String fileType = tika.detect(fileStream);
        if (fileType.equals("application/zip")) {
            unzipFile(fileStream, destDir);
        } else {
            saveFile(fileStream, destDir,fileName);
        }

    }

    private void saveFile(InputStream fileStream, String destDir, String fileName) throws IOException {
        // Создаем путь для сохранения файла
        Path filePath = Paths.get(destDir, fileName);

        // Сохраняем содержимое InputStream в файл
        Files.copy(fileStream, filePath, StandardCopyOption.REPLACE_EXISTING);
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
        } catch (IOException ignored) {}
    }


    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[104857610];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

    public List<UserTasks> getAllTasksByUserIdAndTaskNumber(GetAllTasksRequest getAllTasksRequest) {
        return userTasksRepository.findAllByUserIdAndTaskNumber(getAllTasksRequest.getUserId(), getAllTasksRequest.getTaskNumber());
    }

    @Transactional
    public FeedbackResponse feedback(FeedbackRequest feedbackRequest) {
        UserTasks userTasks = userTasksRepository.findTopByUserIdAndTaskNumberOrderByIdDesc(feedbackRequest.getUserId(),
                feedbackRequest.getTaskNumber());
        userTasks.setComment(feedbackRequest.getComment());
        userTasks.setPoints(feedbackRequest.getPoints());
        userTasksRepository.save(userTasks);

        FeedbackResponse feedbackResponse = new FeedbackResponse();
        feedbackResponse.setSession(userTasks.getSession());
        feedbackResponse.setUserId(userTasks.getUserId());
        feedbackResponse.setComment(userTasks.getComment());
        feedbackResponse.setPoints(userTasks.getPoints());
        return feedbackResponse;

    }
}
