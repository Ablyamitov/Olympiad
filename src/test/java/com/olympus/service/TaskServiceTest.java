package com.olympus.service;

import com.cfuv.olympus.repository.TasksRepository;
import com.cfuv.olympus.repository.UserRepository;
import com.cfuv.olympus.repository.UserTasksRepository;
import com.cfuv.olympus.service.TaskService;
import com.cfuv.olympus.service.UserService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @InjectMocks
    private TaskService taskService;

    @Mock
    private UserTasksRepository userTasksRepository;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TasksRepository tasksRepository;


//
//    @Test
//    void testHandleFile() throws IOException, RarException {
//        Path tempDir = Files.createTempDirectory("temp");
//        Path tempFile = tempDir.resolve("testFile.txt");
//        Files.createFile(tempFile);
//        MockMultipartFile multipartFile = new MockMultipartFile("file", "testFile.txt", "text/plain", Files.readAllBytes(tempFile));
//
//        taskService.handleFile(multipartFile.getInputStream(), "uploads", "testFile.txt");
//
//        Path savedFilePath = Paths.get("uploads", "testFile.txt");
//        assertNotNull(Files.exists(savedFilePath));
//
//        assertNotNull(Files.notExists(tempFile));
//
//        Files.deleteIfExists(savedFilePath);
//    }
//
//    @Test
//    public void testDownloadFile() throws Exception {
//        DownloadTaskRequest request = new DownloadTaskRequest();
//        request.setTaskId(1L);
//        request.setSession(1L);
//        request.setFileName("testFile.txt");
//        Path file = Paths.get("uploads", "tasks", request.getSession().toString(), request.getTaskId().toString(), request.getFileName());
//
//        // Создаем файл для теста
//        Files.createDirectories(file.getParent());
//        Files.createFile(file);
//
//        ResponseEntity<Resource> responseEntity = taskService.downloadFile(request);
//        assertNotNull(responseEntity);
//        assertEquals("attachment; filename=\"testFile.txt\"", responseEntity.getHeaders().get("Content-Disposition").get(0));
//
//        Files.deleteIfExists(file);
//    }
//
//
//    @Test
//    public void testGetJudgeTableBySession() {
//        Long session = 1L;
//
//        UserTasks ut1 = new UserTasks();
//        ut1.setId(1L);
//        ut1.setIdInSession(1L);
//        ut1.setSession(session);
//        ut1.setUserId(1L);
//        ut1.setTaskNumber(1L);
//        ut1.setPoints(100);
//        ut1.setComment("Хорошая работа");
//        ut1.setSentTime("2024-05-05T12:00:00");
//        ut1.setFileName("file1.txt");
//        ut1.setState(UserTaskState.ACCEPTED);
//
//        UserTasks ut2 = new UserTasks();
//        ut2.setId(2L);
//        ut2.setIdInSession(2L);
//        ut2.setSession(session);
//        ut2.setUserId(2L);
//        ut2.setTaskNumber(2L);
//        ut2.setPoints(80);
//        ut2.setComment("Молодец, все правильно");
//        ut2.setSentTime("2024-05-03T12:00:00");
//        ut2.setFileName("file2.txt");
//        ut2.setState(UserTaskState.ACCEPTED);
//
//        List<UserTasks> userTasks = Arrays.asList(ut1, ut2);
//
//        when(userTasksRepository.findAllBySessionOrderByIdInSession(session)).thenReturn(userTasks);
//
//
//
//
//        User user1 = new User();
//        user1.setId(1L);
//        user1.setUsername("user1");
//
//        User user2 = new User();
//        user2.setId(2L);
//        user2.setUsername("user2");
//
//        when(userService.getByUserId(1L)).thenReturn(user1);
//        when(userService.getByUserId(2L)).thenReturn(user2);
//
//        List<JudgeTableResponse> judgeTableResponses = taskService.getJudgeTableBySession(session);
//
//        assertNotNull(judgeTableResponses);
//        assertEquals(userTasks.size(), judgeTableResponses.size());
//
//
//    }
//
//
//    @Test
//    public void testGetResultTableResponse() {
//        Long session = 1L;
//        User user1 = new User();
//        user1.setId(1L);
//        user1.setSession(session);
//        user1.setUsername("user1");
//        user1.setEmail("user1@example.com");
//        user1.setName("User");
//        user1.setSurname("One");
//        user1.setRoles(Set.of(Role.ROLE_PARTICIPANT));
//
//        User user2 = new User();
//        user2.setId(2L);
//        user2.setSession(session);
//        user2.setUsername("user2");
//        user2.setEmail("user2@example.com");
//        user2.setName("User");
//        user2.setSurname("Two");
//        user2.setRoles(Set.of(Role.ROLE_PARTICIPANT));
//
//        List<User> users = Arrays.asList(user1, user2);
//        when(userRepository.findAllBySession(session)).thenReturn(users);
//
//        Tasks task1 = new Tasks();
//        Tasks task2 = new Tasks();
//        LinkedList<Tasks> tasks = new LinkedList<>();
//        tasks.add(task1);tasks.add(task2);
//        when(tasksRepository.findAllBySession(session)).thenReturn(tasks);
//
//        UserTasks ut1 = new UserTasks();
//        ut1.setId(1L);
//        ut1.setIdInSession(1L);
//        ut1.setSession(session);
//        ut1.setUserId(1L);
//        ut1.setTaskNumber(1L);
//        ut1.setPoints(100);
//        ut1.setState(UserTaskState.ACCEPTED);
//
//        UserTasks ut2 = new UserTasks();
//        ut2.setId(2L);
//        ut2.setIdInSession(2L);
//        ut2.setSession(session);
//        ut2.setUserId(2L);
//        ut2.setTaskNumber(2L);
//        ut2.setPoints(80);
//        ut2.setState(UserTaskState.ACCEPTED);
//
//        List<UserTasks> userTasks1 = Collections.singletonList(ut1);
//        List<UserTasks> userTasks2 = Collections.singletonList(ut2);
//        when(userTasksRepository.findAllLatestTasksBySessionAndUserId(session, 1L)).thenReturn(userTasks1);
//        when(userTasksRepository.findAllLatestTasksBySessionAndUserId(session, 2L)).thenReturn(userTasks2);
//
//        ResultTableResponse resultTableResponse = taskService.getResultTableResponse(session);
//        assertNotNull(resultTableResponse);
//        assertEquals(users.size(), resultTableResponse.getUsers().size());
//        assertEquals(tasks.size(), resultTableResponse.getTasksCount());
//    }
}
