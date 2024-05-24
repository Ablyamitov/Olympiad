package com.example.olympiad.web.controller;

import com.example.olympiad.domain.contest.Contest;
import com.example.olympiad.domain.exception.entity.ContestNotStartedException;
import com.example.olympiad.service.ContestService;
import com.example.olympiad.service.TaskService;
import com.example.olympiad.service.UserService;
import com.example.olympiad.service.UserTaskService;
import com.example.olympiad.web.dto.contest.JudgeTable.JudgeTableResponse;
import com.example.olympiad.web.dto.task.Download.DownloadTaskRequest;
import com.example.olympiad.web.dto.task.Download.DownloadUserTaskRequest;
import com.example.olympiad.web.dto.task.GetAllTasks.GetAllTasksRequest;
import com.example.olympiad.web.dto.task.UploadFIle.UploadFileRequest;
import com.example.olympiad.web.dto.user.UserInfo.ChangeUserInfoResponse;
import com.example.olympiad.web.dto.user.UserInfo.UserInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.ErrorMessage;
import org.springframework.core.io.Resource;
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
    private final UserService userService;
    private final UserTaskService userTaskService;


    @Operation(summary = "Set user info", description = "Returns the participant with his specified first name, last name and email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Bad request - User does not exist",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping("/welcome")
    public ResponseEntity<ChangeUserInfoResponse> welcome(@Valid @RequestBody UserInfo userInfo) {
        return ResponseEntity.ok(userService.changeUserInfo(userInfo));
    }

    @Operation(summary = "Get started contest", description = "Returns a started contest for judge or participant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "504", description = "Gateway timeout - Contest not started",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @GetMapping("/contest/{session}")
    public ResponseEntity<Contest> getContestBySession(@PathVariable @Min(value = 0, message = "Session must be at least 0") Long session) {
        try {
            return ResponseEntity.ok(contestService.getContestOptionalBySession(session));
        } catch (ContestNotStartedException e) {
            throw new ContestNotStartedException("Contest not started");
        }
    }


    @Operation(summary = "Upload file", description = "Upload and return participant answer files")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "422", description = "IOException - Failed to upload file",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping("/contest/uploadFile")
    public ResponseEntity<List<JudgeTableResponse>> uploadFile(@Parameter(description = "Сессия") @RequestParam("session") @Min(value = 0, message = "Session must be at least 0") Long session,
                                                               @Parameter(description = "id участника") @RequestParam("userId") @Min(value = 0, message = "userId must be at least 0") Long userId,
                                                               @Parameter(description = "Номер задания") @RequestParam("taskNumber") @Min(value = 0, message = "taskNumber must be at least 0") Long taskNumber,
                                                               @Parameter(description = "Файл ответа участника") @RequestParam("file") MultipartFile file,
                                                               @Parameter(description = "Имя файла ответа участника", example = "index.js") @RequestParam("fileName") @NotBlank(message = "fileName cannot be blank") String fileName
    ) throws IOException {
        UploadFileRequest uploadFileRequest = new UploadFileRequest();
        uploadFileRequest.setSession(session);
        uploadFileRequest.setUserId(userId);
        uploadFileRequest.setTaskNumber(taskNumber);
        uploadFileRequest.setFile(file);
        uploadFileRequest.setFileName(fileName);

        try {
            return ResponseEntity.ok(userTaskService.uploadFile(uploadFileRequest));
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }


    @Operation(summary = "Get all problems by userId and taskNumber", description = "Returns all participant problems by his userId and taskNumber")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved")
    })
    @PostMapping("/contest/answers")
    public ResponseEntity<List<JudgeTableResponse>> getAllTasks(@Valid @RequestBody GetAllTasksRequest getAllTasksRequest) {
        return ResponseEntity.ok(userTaskService.getAllTasksByUserIdAndTaskNumber(getAllTasksRequest));
    }


    @Operation(summary = "Get user tasks file content", description = "Returns a file content user tasks for participant from localstorage")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found - File not found",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping("/download")
    public ResponseEntity<Resource> downloadUserTask(@Valid @RequestBody final DownloadUserTaskRequest downloadRequest) throws Exception {
        return userTaskService.downloadFile(downloadRequest);
    }


    @Operation(summary = "Get task file content", description = "Returns a file content task for participant from localstorage")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found - File not found",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping("/download-task")
    public ResponseEntity<Resource> downloadTask(@Valid @RequestBody final DownloadTaskRequest downloadTaskRequest) throws Exception {
        return taskService.downloadFile(downloadTaskRequest);
    }

}
