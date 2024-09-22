package com.example.olympiad.controller;

import com.example.olympiad.service.TaskService;
import com.example.olympiad.service.UserTaskService;
import com.example.olympiad.web.controller.JudgeController;
import com.example.olympiad.web.dto.contest.JudgeTable.JudgeTableResponse;
import com.example.olympiad.web.dto.contest.ResultTable.ResultTableResponse;
import com.example.olympiad.web.dto.task.Download.DownloadUserTaskRequest;
import com.example.olympiad.web.dto.task.feedback.FeedbackRequest;
import com.example.olympiad.web.security.JwtTokenFilter;
import com.example.olympiad.web.security.JwtTokenProvider;
import com.example.olympiad.web.security.JwtUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.Filter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JudgeController.class)
public class JudgeControllerTest {
    @Autowired
    private MockMvc mockMvc;

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

//    @BeforeEach
//    public void setUp() {
//        mockMvc = MockMvcBuilders
//                .webAppContextSetup(context)
//                .addFilters((Filter) new JwtTokenFilter(jwtTokenProvider))
//                .build();
//    }
//
//    @Test
//    public void testGetContestTableBySession() throws Exception {
//        Long session = 1L;
//        List<JudgeTableResponse> responseList = List.of(new JudgeTableResponse());
//        Mockito.when(taskService.getJudgeTableBySession(session)).thenReturn(responseList);
//
//        mockMvc.perform(get("/api/v1/judge/contest/{session}", session)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0]").exists());
//    }
//
//    @Test
//    public void testDownload() throws Exception {
//        DownloadUserTaskRequest downloadRequest = new DownloadUserTaskRequest();
//        downloadRequest.setFileName("file");
//        downloadRequest.setUserId(1L);
//        downloadRequest.setUserTasksId(1L);
//        Mockito.when(userTaskService.downloadFile(any(DownloadUserTaskRequest.class))).thenReturn(ResponseEntity.ok().build());
//
//        mockMvc.perform(post("/api/v1/judge/download")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(downloadRequest)))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void testFeedback() throws Exception {
//        FeedbackRequest request = new FeedbackRequest();
//        request.setUserTasksId(1L);
//        request.setAccepted(true);
//        request.setPoints(10);
//        request.setComment("Норм работа");
//
//        JudgeTableResponse response = new JudgeTableResponse();
//        response.setComment("Норм работа");
//        Mockito.when(userTaskService.feedback(any(FeedbackRequest.class))).thenReturn(response);
//
//        mockMvc.perform(post("/api/v1/judge/feedback")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.comment").value("Норм работа"));
//    }
//
//    @Test
//    public void testGetContestResultTableBySession() throws Exception {
//        Long session = 1L;
//        ResultTableResponse response = new ResultTableResponse();
//        Mockito.when(taskService.getResultTableResponse(session)).thenReturn(response);
//
//        mockMvc.perform(get("/api/v1/judge/contest/user-problems/result/{session}", session)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").doesNotExist());
//    }
}
