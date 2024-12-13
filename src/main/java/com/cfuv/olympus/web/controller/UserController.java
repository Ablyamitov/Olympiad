package com.cfuv.olympus.web.controller;

import com.cfuv.olympus.domain.contest.Contest;
import com.cfuv.olympus.domain.exception.entity.contest.ContestNotStartedException;
import com.cfuv.olympus.service.ContestService;
import com.cfuv.olympus.service.TaskService;
import com.cfuv.olympus.service.UserService;
import com.cfuv.olympus.web.dto.contest.JudgeTable.JudgeTableResponse;
import com.cfuv.olympus.web.dto.contest.ResultTable.ResultTableRequest;
import com.cfuv.olympus.web.dto.contest.ResultTable.ResultTableResponse;
import com.cfuv.olympus.web.dto.task.Download.DownloadUserTaskRequest;
import com.cfuv.olympus.web.dto.user.UserInfo.ChangeUserInfoResponse;
import com.cfuv.olympus.web.dto.user.UserInfo.UserInfo;
import com.cfuv.olympus.service.UserTaskService;
import com.cfuv.olympus.service.contest.checker.aspect.CheckContestState;
import com.cfuv.olympus.web.dto.task.Download.DownloadTaskRequest;
import com.cfuv.olympus.web.dto.task.GetAllTasks.GetAllTasksRequest;
import com.cfuv.olympus.web.dto.task.UploadFIle.UploadFileRequest;
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
import org.springframework.http.MediaType;
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


    @CheckContestState
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

    @CheckContestState
    @Operation(summary = "Get started contest", description = "Returns a started contest for participant")
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
            throw new ContestNotStartedException("Олимпиада не начата");
        }
    }


    @CheckContestState
    @Operation(summary = "Upload file", description = "Upload and return participant answer files")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "422", description = "IOException - Failed to upload file",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping(value = "/contest/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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


    @CheckContestState
    @Operation(summary = "Get all problems by userId and taskNumber", description = "Returns all participant problems by his userId and taskNumber")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved")
    })
    @PostMapping("/contest/answers")
    public ResponseEntity<List<JudgeTableResponse>> getAllTasks(@Valid @RequestBody GetAllTasksRequest getAllTasksRequest) {
        return ResponseEntity.ok(userTaskService.getAllTasksByUserIdAndTaskNumber(getAllTasksRequest));
    }


    @CheckContestState
    @Operation(summary = "Get user tasks file content", description = "Returns a file content user tasks for participant from localstorage")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found - File not found",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping(value = "/download", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> downloadUserTask(@Valid @RequestBody final DownloadUserTaskRequest downloadRequest) throws Exception {
        return userTaskService.downloadFile(downloadRequest);
    }


    @CheckContestState
    @Operation(summary = "Get task file content", description = "Returns a file content task for participant from localstorage")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found - File not found",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping(value = "/download-task", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> downloadTask(@Valid @RequestBody final DownloadTaskRequest downloadTaskRequest) throws Exception {
        return taskService.downloadFile(downloadTaskRequest);
    }

    @Operation(summary = "Get contest user tasks table", description = "Returns a contest user tasks table for user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved")
    })
    @PostMapping("/contest/user-problems/result/{userId}")
    public ResponseEntity<ResultTableResponse> getContestResultTableBySession(@PathVariable @Min(value = 0, message = "User ID must be at least 0") Long userId,@RequestBody final ResultTableRequest resultTableRequest) {

        return ResponseEntity.ok(taskService.getResultTableResponse(resultTableRequest.getSession(), userId));

    }

}
