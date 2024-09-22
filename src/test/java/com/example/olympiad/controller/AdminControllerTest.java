package com.example.olympiad.controller;

import com.example.olympiad.domain.contest.Contest;
import com.example.olympiad.domain.contest.ContestState;
import com.example.olympiad.domain.contest.Tasks;
import com.example.olympiad.service.ContestService;
import com.example.olympiad.service.TaskService;
import com.example.olympiad.service.UserTaskService;
import com.example.olympiad.web.controller.AdminController;
import com.example.olympiad.web.dto.contest.ChangeDuration.ChangeDurationRequest;
import com.example.olympiad.web.dto.contest.ChangeName.ChangeNameRequest;
import com.example.olympiad.web.dto.contest.CreateContest.ContestAndFileResponse;
import com.example.olympiad.web.dto.contest.CreateContest.ContestRequest;
import com.example.olympiad.web.dto.contest.DeleteContestRequest;
import com.example.olympiad.web.dto.contest.EditProblems.AddProblemRequest;
import com.example.olympiad.web.dto.contest.EditProblems.DeleteProblemRequest;
import com.example.olympiad.web.dto.contest.GetAllContests.ContestsInfo;
import com.example.olympiad.web.dto.contest.GetAllContests.GetAllContestsRequest;
import com.example.olympiad.web.dto.contest.GetAllContests.GetAllContestsResponse;
import com.example.olympiad.web.dto.contest.GetAllContestsContainingName.GetAllContestsContainingNameRequest;
import com.example.olympiad.web.dto.contest.GetStartAndEndContestTime.GetStartAndEndContestTimeRequest;
import com.example.olympiad.web.dto.contest.GetStartAndEndContestTime.GetStartAndEndContestTimeResponse;
import com.example.olympiad.web.dto.contest.JudgeTable.JudgeTableResponse;
import com.example.olympiad.web.dto.contest.ResultTable.ResultTableResponse;
import com.example.olympiad.web.dto.contest.ResultTable.Users;
import com.example.olympiad.web.dto.contest.createUsers.CreateUsersRequest;
import com.example.olympiad.web.dto.contest.createUsers.FileResponse;
import com.example.olympiad.web.dto.task.Download.DownloadTaskRequest;
import com.example.olympiad.web.dto.task.Download.DownloadUserTaskRequest;
import com.example.olympiad.web.security.JwtTokenFilter;
import com.example.olympiad.web.security.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.Filter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@WebMvcTest(AdminController.class)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContestService contestService;

    @MockBean
    private TaskService taskService;

    @MockBean
    private UserTaskService userTaskService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilters((Filter) new JwtTokenFilter(jwtTokenProvider))
                .build();
    }

//    @Test
//    public void testCreateContest() throws Exception {
//        ContestRequest contestRequest = new ContestRequest();
//        contestRequest.setName("Web 2024");
//        contestRequest.setParticipantCount(100);
//        contestRequest.setJudgeCount(5);
//        contestRequest.setUsernamePrefix("cweb");
//        contestRequest.setDuration("01:20");
//
//        ContestAndFileResponse contestAndFileResponse = new ContestAndFileResponse();
//        contestAndFileResponse.setContest(new Contest());
//        contestAndFileResponse.setFileContent(new byte[0]);
//
//        Mockito.when(contestService.create(any(ContestRequest.class))).thenReturn(contestAndFileResponse);
//
//        mockMvc.perform(post("/api/v1/admin/createContest")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(contestRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.contest").exists())
//                .andExpect(jsonPath("$.fileContent").exists());
//    }

//    @Test
//    public void testCreateUsers() throws Exception {
//        CreateUsersRequest createUsersRequest = new CreateUsersRequest();
//        createUsersRequest.setSession(1L);
//        createUsersRequest.setParticipantCount(100);
//        createUsersRequest.setJudgeCount(5);
//
//        FileResponse fileResponse = new FileResponse();
//        fileResponse.setFileContent(new byte[0]);
//
//        Mockito.when(contestService.createUsers(any(CreateUsersRequest.class))).thenReturn(fileResponse);
//
//        mockMvc.perform(post("/api/v1/admin/createUsers")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(createUsersRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.fileContent").exists());
//    }
//
//    @Test
//    public void testGetAllContests() throws Exception {
//        GetAllContestsRequest getAllContestsRequest = new GetAllContestsRequest();
//        getAllContestsRequest.setPage(1);
//
//        GetAllContestsResponse getAllContestsResponse = new GetAllContestsResponse();
//        getAllContestsResponse.setCount(1L);
//        getAllContestsResponse.setContestsInfos(List.of(new ContestsInfo("Web 2024",
//                1L,
//                ContestState.NOT_STARTED,
//                "01:20",
//                "2024-01-01T00:00:00",
//                "2024-01-01T01:20:00")));
//
//        Mockito.when(contestService.getAllContests(eq(1))).thenReturn(getAllContestsResponse);
//
//        mockMvc.perform(post("/api/v1/admin/contests")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(getAllContestsRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.count").value(1L))
//                .andExpect(jsonPath("$.contestsInfos[0].name").value("Web 2024"));
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
//        Mockito.when(contestService.getContestBySession(session)).thenReturn(contest);
//
//        mockMvc.perform(get("/api/v1/admin/contest/{session}", session))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(session))
//                .andExpect(jsonPath("$.name").value("Web 2024"));
//    }

//    @Test
//    public void testStartContest() throws Exception {
//        GetStartAndEndContestTimeRequest request = new GetStartAndEndContestTimeRequest();
//        request.setSession(1L);
//
//        GetStartAndEndContestTimeResponse response = new GetStartAndEndContestTimeResponse();
//        response.setStartTime(ZonedDateTime.now());
//        response.setEndTime(ZonedDateTime.now().plusHours(1));
//
//        Mockito.when(contestService.start(eq(1L))).thenReturn(response);
//
//        mockMvc.perform(post("/api/v1/admin/startContest")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.startTime").exists())
//                .andExpect(jsonPath("$.endTime").exists());
//    }
//
//    @Test
//    public void testGetContestTableBySession() throws Exception {
//        Long session = 1L;
//
//        JudgeTableResponse judgeTableResponse = new JudgeTableResponse();
//        judgeTableResponse.setId(1L);
//        judgeTableResponse.setSession(session);
//        judgeTableResponse.setUserName("user1");
//        judgeTableResponse.setPoints(10);
//
//        Mockito.when(taskService.getJudgeTableBySession(session)).thenReturn(List.of(judgeTableResponse));
//
//        mockMvc.perform(get("/api/v1/admin/contest/user-problems/{session}", session))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].id").value(1L))
//                .andExpect(jsonPath("$[0].session").value(session))
//                .andExpect(jsonPath("$[0].userName").value("user1"))
//                .andExpect(jsonPath("$[0].points").value(10));
//    }
//
//    @Test
//    public void testGetContestResultTableBySession() throws Exception {
//        Long session = 1L;
//
//        ResultTableResponse resultTableResponse = new ResultTableResponse();
//        Users user = new Users();
//        user.setUsername("user1");
//        user.setTotalPoints(100);
//        resultTableResponse.setUsers(List.of(user));
//
//        Mockito.when(taskService.getResultTableResponse(session)).thenReturn(resultTableResponse);
//
//        mockMvc.perform(get("/api/v1/admin/contest/user-problems/result/{session}", session))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.users[0].username").value("user1"))
//                .andExpect(jsonPath("$.users[0].totalPoints").value(100));
//    }
//
//    @Test
//    public void testChangeDuration() throws Exception {
//        ChangeDurationRequest request = new ChangeDurationRequest();
//        request.setSession(1L);
//        request.setNewDuration("02:00");
//
//        Mockito.when(contestService.changeDuration(any(ChangeDurationRequest.class))).thenReturn("Duration changed");
//
//        mockMvc.perform(post("/api/v1/admin/changeDuration")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").value("Duration changed"));
//    }
//
//    @Test
//    public void testChangeName() throws Exception {
//        ChangeNameRequest request = new ChangeNameRequest();
//        request.setSession(1L);
//        request.setName("Web 2025");
//
//        Mockito.when(contestService.changeName(any(ChangeNameRequest.class))).thenReturn("Name changed");
//
//        mockMvc.perform(post("/api/v1/admin/changeName")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").value("Name changed"));
//    }
//
//    @Test
//    public void testAddProblems() throws Exception {
//        Long session = 1L;
//        String htmlName = "Сортировка.html";
//        String htmlContent = "<!DOCTYPE html>";
//        String name = "Work1.zip";
//        int points = 20;
//
//        MockMultipartFile file = new MockMultipartFile("problem", "Work1.zip", "application/zip", new byte[0]);
//
//        Mockito.when(contestService.addProblems(any(AddProblemRequest.class))).thenReturn(List.of(new Tasks()));
//
//        mockMvc.perform(multipart("/api/v1/admin/addProblems")
//                        .file("problem", file.getBytes())
//                        .param("session", String.valueOf(session))
//                        .param("htmlName", htmlName)
//                        .param("htmlContent", htmlContent)
//                        .param("name", name)
//                        .param("points", String.valueOf(points)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0]").exists());
//    }
//
//    @Test
//    public void testDownload() throws Exception {
//        DownloadTaskRequest request = new DownloadTaskRequest();
//        request.setSession(1L);
//        request.setTaskId(1L);
//        request.setFileName("work1.zip");
//
//        Resource resource = Mockito.mock(Resource.class);
//
//        Mockito.when(taskService.downloadFile(any(DownloadTaskRequest.class))).thenReturn(ResponseEntity.ok(resource));
//
//        mockMvc.perform(post("/api/v1/admin/download")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void testDownloadUserTask() throws Exception {
//        DownloadUserTaskRequest request = new DownloadUserTaskRequest();
//        request.setUserId(1L);
//        request.setUserTasksId(1L);
//        request.setFileName("index.js");
//
//        Resource resource = Mockito.mock(Resource.class);
//
//        Mockito.when(userTaskService.downloadFile(any(DownloadUserTaskRequest.class))).thenReturn(ResponseEntity.ok(resource));
//
//        mockMvc.perform(post("/api/v1/admin/download-user-task")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void testDeleteProblems() throws Exception {
//        DeleteProblemRequest request = new DeleteProblemRequest();
//        request.setId(1L);
//        request.setSession(1L);
//
//        LinkedList<Tasks> tasksList = new LinkedList<>();
//        tasksList.add(new Tasks());
//
//        Mockito.when(contestService.deleteProblem(any(DeleteProblemRequest.class))).thenReturn(tasksList);
//
//        mockMvc.perform(post("/api/v1/admin/deleteProblems")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0]").exists());
//    }
//
//    @Test
//    public void testDeleteContest() throws Exception {
//        DeleteContestRequest request = new DeleteContestRequest();
//        request.setContestSession(1L);
//
//        Mockito.doNothing().when(contestService).deleteContest(any(Long.class));
//
//        mockMvc.perform(post("/api/v1/admin/deleteContest")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk());
//    }
}
