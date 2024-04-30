package com.example.olympiad.web.controller;

import com.example.olympiad.domain.contest.Contest;
import com.example.olympiad.domain.contest.UserTasks;
import com.example.olympiad.domain.exception.entity.ContestNotStartedException;
import com.example.olympiad.repository.UserTasksRepository;
import com.example.olympiad.service.ContestService;
import com.example.olympiad.service.TaskService;
import com.example.olympiad.web.dto.contest.JudgeTable.JudgeTableResponse;
import com.example.olympiad.web.dto.contest.createUsers.CreateUsersRequest;
import com.example.olympiad.web.dto.task.Download.DownloadRequest;
import com.example.olympiad.web.dto.task.feedback.FeedbackRequest;
import com.example.olympiad.web.dto.task.feedback.FeedbackResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springdoc.api.ErrorMessage;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Tag(name = "Judge controller", description = "Judge management ")
@RestController
@RequestMapping("/api/v1/judge")
@RequiredArgsConstructor
@Validated
public class JudgeController {
    private final Tika tika = new Tika();
    private final TaskService taskService;
    private final UserTasksRepository userTasksRepository;
    //Judge
    @Operation(summary = "Get contest user tasks table", description = "Returns a contest user tasks table for judge")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved")
    })
    @GetMapping("/contest/{session}")
    public ResponseEntity<List<JudgeTableResponse>> getContestTableBySession(@PathVariable Long session) {

            return ResponseEntity.ok(taskService.getJudgeTableBySession(session));

    }

    @Operation(summary = "Get user tasks file content", description = "Returns a file content user tasks for judge from database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found - File not found",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @GetMapping("/download/{id}")
    public ResponseEntity<String> download(@PathVariable Long id) {
        UserTasks userTask = userTasksRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("File not found with id: " + id));

        return ResponseEntity.ok().body(userTask.getFileContent());
    }


    @Operation(summary = "Get user tasks file content", description = "Returns a file content user tasks for judge from localstorage")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found - File not found",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping("/download")
    public ResponseEntity<Resource> download(@RequestBody final DownloadRequest downloadRequest) throws Exception {
        //Подправить, засунув в fileContent путь
        Path file = Paths.get("uploads", downloadRequest.getUserId().toString(), downloadRequest.getUserTasksId().toString(), downloadRequest.getFileName());
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


    @Operation(summary = "Send feedback", description = "Send feedback from judge to participant problem")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found - User task not found",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping("/feedback")
    public ResponseEntity<JudgeTableResponse> feedback(@RequestBody FeedbackRequest feedbackRequest) {
        return ResponseEntity.ok(taskService.feedback(feedbackRequest));
    }
}
