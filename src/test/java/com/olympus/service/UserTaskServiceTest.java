package com.olympus.service;

import com.cfuv.olympus.repository.ContestRepository;
import com.cfuv.olympus.repository.UserTasksRepository;
import com.cfuv.olympus.service.TaskService;
import com.cfuv.olympus.service.UserService;
import com.cfuv.olympus.service.UserTaskService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class UserTaskServiceTest {
    @InjectMocks
    private UserTaskService userTaskService;

    @Mock
    private TaskService taskService;

    @Mock
    private UserService userService;
    @Mock
    private ContestRepository contestRepository;

    @Mock
    private UserTasksRepository userTasksRepository;

    @Value("${storage.location}")
    private String UPLOAD_DIR;

//    @Test
//    public void testUploadFile() throws Exception {
//        UploadFileRequest uploadFileRequest = new UploadFileRequest();
//        uploadFileRequest.setSession(1L);
//        uploadFileRequest.setUserId(1L);
//        uploadFileRequest.setTaskNumber(1L);
//        uploadFileRequest.setFile(new MockMultipartFile("file", "file.txt", MediaType.TEXT_PLAIN_VALUE, "file content".getBytes()));
//
//        Contest contest = new Contest();
//        contest.setStartTime(ZonedDateTime.now());
//
//        when(contestRepository.findBySession(uploadFileRequest.getSession())).thenReturn(Optional.of(contest));
//        when(userTasksRepository.countBySession(uploadFileRequest.getSession())).thenReturn(0L);
//
//        List<JudgeTableResponse> judgeTableResponses = userTaskService.uploadFile(uploadFileRequest);
//
//        assertNotNull(judgeTableResponses);
//    }
//    @Test
//    public void testGetAllTasksByUserIdAndTaskNumber() {
//        // Создайте объект GetAllTasksRequest и настройте его по вашему усмотрению
//        GetAllTasksRequest getAllTasksRequest = new GetAllTasksRequest();
//        getAllTasksRequest.setUserId(1L);
//        getAllTasksRequest.setTaskNumber(1L);
//
//        // Создайте моки объектов и настройте их поведение
//        List<UserTasks> userTasks = Arrays.asList(createMockUserTask(), createMockUserTask());
//        when(userTasksRepository.findAllByUserIdAndTaskNumberOrderByIdInSession(anyLong(), anyLong())).thenReturn(userTasks);
//
//        // Вызываем метод, который тестируем
//        List<JudgeTableResponse> judgeTableResponses = userTaskService.getAllTasksByUserIdAndTaskNumber(getAllTasksRequest);
//
//        // Проверяем результат
//        assertNotNull(judgeTableResponses);
//        // Добавьте здесь другие проверки возвращаемых значений, если нужно
//    }
//
//    @Test
//    public void testDownloadFile() throws Exception {
//        DownloadUserTaskRequest downloadRequest = new DownloadUserTaskRequest();
//
//        downloadRequest.setUserTasksId(1L);
//        downloadRequest.setFileName("file.txt");
//        downloadRequest.setUserId(1L);
//
//        Path filePath = Paths.get("uploads", "user-tasks", "1", "1", "file.txt");
//        Resource resource = new UrlResource(filePath.toUri());
//        when(taskService.getResourceResponseEntity(any(Path.class))).thenReturn(ResponseEntity.ok()
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
//                .body(resource));
//
//        ResponseEntity<Resource> response = userTaskService.downloadFile(downloadRequest);
//
//        assertNotNull(response);
//    }
//
//    @Test
//    public void testFeedback() {
//        FeedbackRequest feedbackRequest = new FeedbackRequest();
//        feedbackRequest.setUserTasksId(1L);
//        feedbackRequest.setPoints(100);
//        feedbackRequest.setAccepted(true);
//        feedbackRequest.setComment("Хорошая работа");
//
//
//        UserTasks userTask = createMockUserTask();
//        when(userTasksRepository.findById(anyLong())).thenReturn(Optional.of(userTask));
//        when(taskService.mapToJudgeTableResponse(any(UserTasks.class))).thenReturn(new JudgeTableResponse());
//
//        JudgeTableResponse judgeTableResponse = userTaskService.feedback(feedbackRequest);
//
//        assertNotNull(judgeTableResponse);
//    }
//
//    private UserTasks createMockUserTask() {
//        UserTasks ut1 = new UserTasks();
//        ut1.setId(1L);
//        ut1.setIdInSession(1L);
//        ut1.setSession(1L);
//        ut1.setUserId(1L);
//        ut1.setTaskNumber(1L);
//        ut1.setPoints(100);
//        ut1.setComment("Хорошая работа");
//        ut1.setSentTime("2024-05-05T12:00:00");
//        ut1.setFileName("file1.txt");
//        ut1.setState(UserTaskState.ACCEPTED);
//        return ut1;
//    }
}
