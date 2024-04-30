package com.example.olympiad.service;

import com.example.olympiad.domain.contest.UserTaskState;
import com.example.olympiad.domain.contest.UserTasks;
import com.example.olympiad.domain.user.Role;
import com.example.olympiad.repository.UserTasksRepository;
import com.example.olympiad.web.dto.contest.JudgeTable.JudgeTableResponse;
import com.example.olympiad.web.dto.task.GetAllTasks.GetAllTasksRequest;
import com.example.olympiad.web.dto.task.UploadFileRequest;
import com.example.olympiad.web.dto.task.UploadFileResponse;
import com.example.olympiad.web.dto.task.feedback.FeedbackRequest;
import com.example.olympiad.web.dto.task.feedback.FeedbackResponse;
import com.github.junrar.Junrar;
import com.github.junrar.exception.RarException;
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
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final UserTasksRepository userTasksRepository;
    private final UserService userService;
    private static final String UPLOAD_DIR = "uploads/";



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

        userTasks.setFileName(uploadFileRequest.getFileName());
        userTasks.setFileExtension(uploadFileRequest.getFileExtension());

        userTasks.setComment(null);
        userTasks.setPoints(null);
        userTasks.setState(UserTaskState.NOT_EVALUATED);
        userTasksRepository.save(userTasks);



        try {
            String userDir = UPLOAD_DIR + uploadFileRequest.getUserId().toString() + "/" + userTasks.getId().toString() + "/";
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


        return userTasksRepository.findAllByUserIdAndTaskNumber(uploadFileRequest.getUserId(), userTasks.getTaskNumber());
    }

    private void handleFile(InputStream fileStream, String destDir,String fileName) throws IOException, RarException {
//        Tika tika = new Tika();
//        String fileType = tika.detect(fileStream);
//        if (fileType.equals("application/zip")) {
//            unzipFile(fileStream, destDir);
////        } else if (fileType.startsWith("application/x-rar-compressed")) {
////            extractRar(fileStream, destDir);
//        } else {
//            saveFile(fileStream, destDir,fileName);
//        }
        // Сохраняем InputStream во временный файл
        Path tempPath = Files.createTempFile("temp", null);
        Files.copy(fileStream, tempPath, StandardCopyOption.REPLACE_EXISTING);

        // Определяем тип файла
        Tika tika = new Tika();
        String fileType = tika.detect(tempPath);

        // Обрабатываем файл в зависимости от его типа
        if (fileType.equals("application/zip")) {
            unzipFile(Files.newInputStream(tempPath), destDir);
        } else {
            saveFile(Files.newInputStream(tempPath), destDir, fileName);
        }

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
        } catch (IOException ignored) {}
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

    public List<JudgeTableResponse> getJudgeTableBySession(Long session) {
        List<UserTasks> userTasks = userTasksRepository.findAllBySession(session);
        List<JudgeTableResponse> judgeTableResponses = new ArrayList<>();
        for (UserTasks ut: userTasks) {
            JudgeTableResponse jtr = new JudgeTableResponse();
            jtr.setId(ut.getId());
            jtr.setSession(ut.getSession());
            jtr.setUserName(userService.getByUserId(ut.getUserId()).getUsername());
            jtr.setTaskNumber(ut.getTaskNumber());
            //jtr.setFileContent(ut.getFileContent());
            jtr.setPoints(ut.getPoints());
            jtr.setComment(ut.getComment());
            jtr.setSentTime(ut.getSentTime());
            jtr.setFileName(ut.getFileName());
            jtr.setFileExtension(ut.getFileExtension());
            jtr.setState(ut.getState().name());

            judgeTableResponses.add(jtr);
        }

        return judgeTableResponses;

    }
}
