package com.olympus.controller;

import com.cfuv.olympus.service.ContestService;
import com.cfuv.olympus.service.TaskService;
import com.cfuv.olympus.service.UserService;
import com.cfuv.olympus.service.UserTaskService;
import com.cfuv.olympus.web.controller.UserController;
import com.cfuv.olympus.web.security.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContestService contestService;

    @MockBean
    private TaskService taskService;

    @MockBean
    private UserService userService;

    @MockBean
    private UserTaskService userTaskService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

//    @BeforeEach
//    public void setUp() {
//        mockMvc = MockMvcBuilders
//                .webAppContextSetup(context)
//                .addFilters((Filter) new JwtTokenFilter(jwtTokenProvider))
//                .build();
//    }
//
//    @Test
//    public void testWelcome() throws Exception {
//        UserInfo userInfo = new UserInfo();
//        userInfo.setUsername("cweb_1_1");
//        userInfo.setName("Иван");
//        userInfo.setSurname("Иванов");
//        userInfo.setEmail("ivanov@mail.ru");
//
//        ChangeUserInfoResponse response = new ChangeUserInfoResponse();
//        response.setUsername("cweb_1_1");
//        response.setName("Иван");
//        response.setSurname("Иванов");
//        response.setEmail("ivanov@mail.ru");
//
//        Mockito.when(userService.changeUserInfo(any(UserInfo.class))).thenReturn(response);
//
//        mockMvc.perform(post("/api/v1/users/welcome")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(userInfo)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.username").value("cweb_1_1"))
//                .andExpect(jsonPath("$.name").value("Иван"))
//                .andExpect(jsonPath("$.surname").value("Иванов"))
//                .andExpect(jsonPath("$.email").value("ivanov@mail.ru"));
//    }
//
//    @Test
//    public void testGetContestBySession() throws Exception {
//        Long session = 1L;
//
//        Contest contest = new Contest();
//        contest.setId(session);
//        contest.setName("Web 2024");
//
//        Mockito.when(contestService.getContestOptionalBySession(session)).thenReturn(contest);
//
//        mockMvc.perform(get("/api/v1/users/contest/{session}", session))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(session))
//                .andExpect(jsonPath("$.name").value("Web 2024"));
//    }
//
//    @Test
//    public void testUploadFile() throws Exception {
//        Long session = 1L;
//        Long userId = 1L;
//        Long taskNumber = 1L;
//        String fileName = "test.txt";
//        MockMultipartFile file = new MockMultipartFile("file", fileName, "text/plain", "test content".getBytes());
//
//        JudgeTableResponse judgeTableResponse = new JudgeTableResponse();
//        judgeTableResponse.setId(1L);
//
//        Mockito.when(userTaskService.uploadFile(any(UploadFileRequest.class))).thenReturn(List.of(judgeTableResponse));
//
//        mockMvc.perform(multipart("/api/v1/users/contest/uploadFile")
//                        .file(file)
//                        .param("session", String.valueOf(session))
//                        .param("userId", String.valueOf(userId))
//                        .param("taskNumber", String.valueOf(taskNumber))
//                        .param("fileName", fileName))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].id").value(1L));
//    }
//
//    @Test
//    public void testGetAllTasks() throws Exception {
//        GetAllTasksRequest request = new GetAllTasksRequest();
//        request.setSession(1L);
//        request.setUserId(1L);
//        request.setTaskNumber(1L);
//
//        JudgeTableResponse response = new JudgeTableResponse();
//        response.setId(1L);
//
//        Mockito.when(userTaskService.getAllTasksByUserIdAndTaskNumber(any(GetAllTasksRequest.class))).thenReturn(List.of(response));
//
//        mockMvc.perform(post("/api/v1/users/contest/answers")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].id").value(1L));
//    }
//
//    @Test
//    public void testDownloadUserTask() throws Exception {
//        DownloadUserTaskRequest request = new DownloadUserTaskRequest();
//        request.setUserId(1L);
//        request.setUserTasksId(1L);
//        request.setFileName("test.txt");
//
//        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "test content".getBytes());
//
//        Mockito.when(userTaskService.downloadFile(any(DownloadUserTaskRequest.class))).thenReturn(ResponseEntity.ok().body(file.getResource()));
//
//        mockMvc.perform(post("/api/v1/users/download")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void testDownloadTask() throws Exception {
//        DownloadTaskRequest request = new DownloadTaskRequest();
//        request.setSession(1L);
//        request.setTaskId(1L);
//        request.setFileName("task.txt");
//
//        MockMultipartFile file = new MockMultipartFile("file", "task.txt", "text/plain", "task content".getBytes());
//
//        Mockito.when(taskService.downloadFile(any(DownloadTaskRequest.class))).thenReturn(ResponseEntity.ok().body(file.getResource()));
//
//        mockMvc.perform(post("/api/v1/users/download-task")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk());
//    }
}
