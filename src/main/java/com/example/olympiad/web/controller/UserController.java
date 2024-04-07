package com.example.olympiad.web.controller;

import com.example.olympiad.domain.contest.Contest;
import com.example.olympiad.domain.contest.UserTasks;
import com.example.olympiad.domain.exception.entity.ContestNotStartedException;
import com.example.olympiad.service.ContestService;
import com.example.olympiad.service.TaskService;
import com.example.olympiad.web.dto.task.GetAllTasks.GetAllTasksRequest;
import com.example.olympiad.web.dto.task.UploadFileRequest;
import com.example.olympiad.web.dto.task.UploadFileResponse;
import com.example.olympiad.web.dto.task.feedback.FeedbackRequest;
import com.example.olympiad.web.dto.task.feedback.FeedbackResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "User controller", description = "User management ")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final ContestService contestService;
    private final TaskService taskService;


    @Operation(summary = "Get started contest", description = "Returns a started contest for judge or participant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "504", description = "Gateway timeout - Contest not started",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @GetMapping("/contest/{session}")
    public ResponseEntity<Contest> getContestBySession(@PathVariable Long session) {
        try {
            return ResponseEntity.ok(contestService.getContestOptionalBySession(session));
        } catch (ContestNotStartedException e) {
            throw new ContestNotStartedException("Contest not started");
        }
    }


    @PostMapping("/contest/uploadFile")
    public ResponseEntity<List<UserTasks>> uploadFile(@RequestParam("session") Long session,
                                                      @RequestParam("userId") Long userId,
                                                      @RequestParam("taskNumber") Long taskNumber,
                                                      @RequestParam("file") MultipartFile file
    ) throws IOException {
        UploadFileRequest uploadFileRequest = new UploadFileRequest();
        uploadFileRequest.setSession(session);
        uploadFileRequest.setUserId(userId);
        uploadFileRequest.setTaskNumber(taskNumber);
        uploadFileRequest.setFile(file);

        try {
            return ResponseEntity.ok(taskService.uploadFile(uploadFileRequest));
        }
        catch (IOException e)
        {
            throw new IOException(e.getMessage());
        }


    }


    @PostMapping("/contest/answers")
    public ResponseEntity<List<UserTasks>> getAllTasks(@RequestBody GetAllTasksRequest getAllTasksRequest) {
        return ResponseEntity.ok(taskService.getAllTasksByUserIdAndTaskNumber(getAllTasksRequest));
    }


    @PostMapping("/contest/feedback")
    public ResponseEntity<FeedbackResponse> feedback(@RequestBody FeedbackRequest feedbackRequest) {
        return ResponseEntity.ok(taskService.feedback(feedbackRequest));
    }
}
