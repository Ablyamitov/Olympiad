package com.example.olympiad.web.controller;

import com.example.olympiad.domain.contest.Contest;
import com.example.olympiad.domain.exception.entity.ContestNotStartedException;
import com.example.olympiad.service.ContestService;
import com.example.olympiad.service.TaskService;
import com.example.olympiad.service.UserService;
import com.example.olympiad.web.dto.contest.JudgeTable.JudgeTableResponse;
import com.example.olympiad.web.dto.task.Download.DownloadRequest;
import com.example.olympiad.web.dto.task.GetAllTasks.GetAllTasksRequest;
import com.example.olympiad.web.dto.task.UploadFileRequest;
import com.example.olympiad.web.dto.user.UserInfo.ChangeUserInfoResponse;
import com.example.olympiad.web.dto.user.UserInfo.UserInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Tag(name = "User controller", description = "User management ")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final Tika tika = new Tika();
    private final ContestService contestService;
    private final TaskService taskService;
    private final UserService userService;


    @Operation(summary = "Set user info", description = "Returns the participant with his specified first name, last name and email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Bad request - User does not exist",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping("/welcome")
    public ResponseEntity<ChangeUserInfoResponse> welcome(@RequestBody UserInfo userInfo) {
        return ResponseEntity.ok(userService.changeUserInfo(userInfo));
    }

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


    @Operation(summary = "Upload file", description = "Upload and return participant answer files")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "422", description = "IOException - Failed to upload file",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping("/contest/uploadFile")
    public ResponseEntity<List<JudgeTableResponse>> uploadFile(@RequestParam("session") Long session,
                                                               @RequestParam("userId") Long userId,
                                                               @RequestParam("taskNumber") Long taskNumber,
                                                               @RequestParam("file") MultipartFile file,
                                                               @RequestParam("fileExtension") String fileExtension,
                                                               @RequestParam("fileName") String fileName
    ) throws IOException {
        UploadFileRequest uploadFileRequest = new UploadFileRequest();
        uploadFileRequest.setSession(session);
        uploadFileRequest.setUserId(userId);
        uploadFileRequest.setTaskNumber(taskNumber);
        uploadFileRequest.setFile(file);
        uploadFileRequest.setFileExtension(fileExtension);
        uploadFileRequest.setFileName(fileName);

        try {
            return ResponseEntity.ok(taskService.uploadFile(uploadFileRequest));
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }


    }


    @Operation(summary = "Get all problems by userId and taskNumber", description = "Returns all participant problems by his userId and taskNumber")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved")
    })
    @PostMapping("/contest/answers")
    public ResponseEntity<List<JudgeTableResponse>> getAllTasks(@RequestBody GetAllTasksRequest getAllTasksRequest) {
        return ResponseEntity.ok(taskService.getAllTasksByUserIdAndTaskNumber(getAllTasksRequest));
    }


    @Operation(summary = "Get user tasks file content", description = "Returns a file content user tasks for participant from localstorage")
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



}
