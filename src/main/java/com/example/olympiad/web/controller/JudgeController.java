package com.example.olympiad.web.controller;

import com.example.olympiad.domain.contest.Contest;
import com.example.olympiad.service.ContestService;
import com.example.olympiad.service.TaskService;
import com.example.olympiad.service.UserTaskService;
import com.example.olympiad.service.contest.checker.aspect.CheckContestState;
import com.example.olympiad.web.dto.contest.JudgeTable.JudgeTableResponse;
import com.example.olympiad.web.dto.contest.ResultTable.ResultTableResponse;
import com.example.olympiad.web.dto.task.Download.DownloadUserTaskRequest;
import com.example.olympiad.web.dto.task.feedback.FeedbackRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.ErrorMessage;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Judge controller", description = "Judge management ")
@RestController
@RequestMapping("/api/v1/judge")
@RequiredArgsConstructor
@Validated

public class JudgeController {

    private final TaskService taskService;
    private final UserTaskService userTaskService;
    private final ContestService contestService;



    @CheckContestState
    @Operation(summary = "Get contest by session", description = "Return contest by session")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Bad request - Contest does not exists",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @GetMapping("/contest/{session}")
    public ResponseEntity<Contest> getContestBySession(@PathVariable @Min(value = 0, message = "Session cannot be less than 0") Long session) {
        Contest contest = contestService.getContestBySession(session);
        return ResponseEntity.ok(contest);
    }

    //Judge
    @CheckContestState
    @Operation(summary = "Get contest user tasks table", description = "Returns a contest user tasks table for judge")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved")
    })
    @GetMapping("/contest/{session}/table")
    public ResponseEntity<List<JudgeTableResponse>> getContestTableBySession(@PathVariable @Min(value = 0, message = "Session cannot be less than 0") Long session) {
        return ResponseEntity.ok(taskService.getJudgeTableBySession(session));

    }


    @CheckContestState
    @Operation(summary = "Get user tasks file content", description = "Returns a file content user tasks for judge from localstorage")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found - File not found",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping("/download")
    public ResponseEntity<Resource> download(@Valid @RequestBody final DownloadUserTaskRequest downloadRequest) throws Exception {
        return userTaskService.downloadFile(downloadRequest);
    }


    @CheckContestState
    @Operation(summary = "Send feedback", description = "Send feedback from judge to participant problem")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found - User task not found",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping("/feedback")
    public ResponseEntity<JudgeTableResponse> feedback(@Valid @RequestBody FeedbackRequest feedbackRequest) {
        return ResponseEntity.ok(userTaskService.feedback(feedbackRequest));
    }

    @Operation(summary = "Get contest user tasks table", description = "Returns a contest user tasks table for judge")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved")
    })
    @GetMapping("/contest/user-problems/result/{session}")
    public ResponseEntity<ResultTableResponse> getContestResultTableBySession(@PathVariable @Min(value = 0, message = "Session cannot be less than 0") Long session) {

        return ResponseEntity.ok(taskService.getResultTableResponse(session));

    }
}
