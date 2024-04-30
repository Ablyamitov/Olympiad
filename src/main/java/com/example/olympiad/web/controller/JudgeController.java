package com.example.olympiad.web.controller;

import com.example.olympiad.domain.contest.Contest;
import com.example.olympiad.domain.contest.UserTasks;
import com.example.olympiad.domain.exception.entity.ContestNotStartedException;
import com.example.olympiad.repository.UserTasksRepository;
import com.example.olympiad.service.ContestService;
import com.example.olympiad.service.TaskService;
import com.example.olympiad.web.dto.contest.JudgeTable.JudgeTableResponse;
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
import org.springdoc.api.ErrorMessage;
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
    private final UserTasksRepository userTasksRepository;
    //Judge
    @Operation(summary = "Get contest user tasks table", description = "Returns a contest user tasks table for judge")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "504", description = "Gateway timeout - Contest not started",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @GetMapping("/contest/{session}")
    public ResponseEntity<List<JudgeTableResponse>> getContestTableBySession(@PathVariable Long session) {
        try {
            return ResponseEntity.ok(taskService.getJudgeTableBySession(session));
        } catch (ContestNotStartedException e) {
            throw new ContestNotStartedException("Contest not started");
        }
    }
    @GetMapping("/download/{id}")
    public ResponseEntity<String> download(@PathVariable Long id) {
        UserTasks userTask = userTasksRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("File not found with id: " + id));

        return ResponseEntity.ok().body(userTask.getFileContent());
    }
}
