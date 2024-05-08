package com.example.olympiad.web.controller;

import com.example.olympiad.domain.contest.Contest;
import com.example.olympiad.domain.contest.Tasks;
import com.example.olympiad.service.ContestService;
import com.example.olympiad.service.TaskService;
import com.example.olympiad.web.dto.contest.ChangeDuration.ChangeDurationRequest;
import com.example.olympiad.web.dto.contest.CreateContest.ContestAndFileResponse;
import com.example.olympiad.web.dto.contest.CreateContest.ContestRequest;
import com.example.olympiad.web.dto.contest.DeleteContestRequest;
import com.example.olympiad.web.dto.contest.EditProblems.AddProblemRequest;
import com.example.olympiad.web.dto.contest.EditProblems.DeleteProblemRequest;
import com.example.olympiad.web.dto.contest.GetAllContests.GetAllContestsRequest;
import com.example.olympiad.web.dto.contest.GetAllContests.GetAllContestsResponse;
import com.example.olympiad.web.dto.contest.GetAllContestsContainingName.GetAllContestsContainingNameRequest;
import com.example.olympiad.web.dto.contest.GetStartAndEndContestTime.GetStartAndEndContestTimeRequest;
import com.example.olympiad.web.dto.contest.GetStartAndEndContestTime.GetStartAndEndContestTimeResponse;
import com.example.olympiad.web.dto.contest.JudgeTable.JudgeTableResponse;
import com.example.olympiad.web.dto.contest.ResultTable.ResultTableResponse;
import com.example.olympiad.web.dto.contest.createUsers.CreateUsersRequest;
import com.example.olympiad.web.dto.contest.createUsers.FileResponse;
import com.example.olympiad.web.dto.task.Download.DownloadTaskRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "Admin controller", description = "Administrator management")
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Validated
public class AdminController {

    private final ContestService contestService;
    private final TaskService taskService;


    @Operation(summary = "Create contest", description = "Return created contest and users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Bad request - Contest already exists",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping("/createContest")
    public ResponseEntity<ContestAndFileResponse> createContest(@Valid @RequestBody final ContestRequest contestRequest) throws IOException {
        ContestAndFileResponse response = contestService.create(contestRequest);
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Create users for contest", description = "Return created users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Bad request - Contest does not exists",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping("/createUsers")
    public ResponseEntity<FileResponse> createUsers(@Valid @RequestBody final CreateUsersRequest createUsersRequest) throws IOException {
        FileResponse response = contestService.createUsers(createUsersRequest);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get contests by page and total count", description = "Return contests limited by page and total contests count")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved")
    })
    @PostMapping("/contests")
    public ResponseEntity<GetAllContestsResponse> getAllContests(@Valid @RequestBody final GetAllContestsRequest getAllContestsRequest) {
        return ResponseEntity.ok(contestService.getAllContests(getAllContestsRequest.getPage()));
    }


    @Operation(summary = "Find contests containing name or state", description = "Return contests limited by page and containing name or state")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved")
    })
    @PostMapping("/contests/search")
    public ResponseEntity<GetAllContestsResponse> getAllContestsContainingNameOrState(@Valid @RequestBody final GetAllContestsContainingNameRequest getAllContestsContainingNameRequest) {
        return ResponseEntity.ok(contestService.getAllContestsContainingNameOrState(getAllContestsContainingNameRequest.getPage(),
                getAllContestsContainingNameRequest.getName(),
                getAllContestsContainingNameRequest.getStates()));
    }

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

    @Operation(summary = "Get contest user tasks table", description = "Returns a contest user tasks table for admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved")
    })
    @GetMapping("/contest/user-problems/{session}")
    public ResponseEntity<List<JudgeTableResponse>> getContestTableBySession(@PathVariable @Min(value = 0, message = "Session cannot be less than 0") Long session) {

        return ResponseEntity.ok(taskService.getJudgeTableBySession(session));

    }

    @Operation(summary = "Get contest user tasks table", description = "Returns a contest user tasks table for admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved")
    })
    @GetMapping("/contest/user-problems/result/{session}")
    public ResponseEntity<ResultTableResponse> getContestResultTableBySession(@PathVariable @Min(value = 0, message = "Session cannot be less than 0") Long session) {

        return ResponseEntity.ok(taskService.getResultTableResponse(session));

    }


    @Operation(summary = "Start contest", description = "Return contest start and end time")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Bad request - Contest does not exists",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping("/startContest")
    public ResponseEntity<GetStartAndEndContestTimeResponse> startContest(@Valid @RequestBody final GetStartAndEndContestTimeRequest getStartAndEndContestTimeRequest) {
        return ResponseEntity.ok(contestService
                .start(getStartAndEndContestTimeRequest.getSession()));
    }

    @Operation(summary = "Change duration", description = "Change contest duration")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Valid exception - validation failed",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
            ,
            @ApiResponse(responseCode = "404", description = "Bad request - Contest does not exists",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping("/changeDuration")
    public ResponseEntity<String> changeDuration(@Valid @RequestBody final ChangeDurationRequest changeDurationRequest) {
        return ResponseEntity.ok(contestService
                .changeDuration(changeDurationRequest));
    }

    @Operation(summary = "Add problems", description = "Add problems to the contest")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Bad request - Contest does not exists",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping("/addProblems")
    public ResponseEntity<List<Tasks>> addProblems(
            @Parameter(description = "Сессия", example = "1") @RequestParam("session") @Min(value = 0, message = "Session must be at least 0") Long session,
            @Parameter(description = "Имя html", example = "Сортировка.html") @RequestParam(value = "htmlName", required = false) String htmlName,
            @Parameter(description = "Содержимое html", example = "<!DOCTYPE html>") @RequestParam(value = "htmlContent") String htmlContent,
            @Parameter(description = "Название zip файла", example = "Work1.zip") @RequestParam(value = "name", required = false) String name,
            @Parameter(description = "Zip файл") @RequestParam(value = "problem", required = false) MultipartFile file,
            @Parameter(description = "Макс. кол-во очков", example = "20") @RequestParam("points") @Min(value = 0, message = "Session must be at least 0") int points
    ) throws IOException {
        AddProblemRequest addProblemRequest = new AddProblemRequest();
        addProblemRequest.setSession(session);
        addProblemRequest.setHtmlContent(htmlContent);
        addProblemRequest.setName(name);
        addProblemRequest.setProblem(file);
        addProblemRequest.setPoints(points);
        addProblemRequest.setHtmlName(htmlName);
        return ResponseEntity.ok(contestService
                .addProblems(addProblemRequest));
    }


    @Operation(summary = "Get user tasks file content", description = "Returns a file content user tasks for admin from localstorage")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found - File not found",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping("/download")
    public ResponseEntity<Resource> download(@Valid @RequestBody final DownloadTaskRequest downloadTaskRequest) throws Exception {
        return taskService.downloadFile(downloadTaskRequest);
    }

    @Operation(summary = "Delete problems", description = "Delete problems from the contest")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Bad request - Contest does not exists",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping("/deleteProblems")
    public ResponseEntity<List<Tasks>> deleteProblems(@Valid @RequestBody final DeleteProblemRequest deleteProblemRequest) {
        return ResponseEntity.ok(contestService
                .deleteProblem(deleteProblemRequest));
    }


    @Operation(summary = "Delete contest", description = "Return created users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Bad request - Contest does not exists",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping("/deleteContest")
    public ResponseEntity<Void> deleteContest(@RequestBody final DeleteContestRequest deleteContestRequest) {
        contestService.deleteContest(deleteContestRequest.getContestSession());
        return ResponseEntity.ok().build();
    }
}
