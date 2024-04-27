package com.example.olympiad.web.controller;
import com.example.olympiad.domain.contest.Contest;
import com.example.olympiad.service.ContestService;
import com.example.olympiad.web.dto.contest.AddProblems.AddProblemsRequest;
import com.example.olympiad.web.dto.contest.AllContestsNameSessionResponse;
import com.example.olympiad.web.dto.contest.ChangeDuration.ChangeDurationRequest;
import com.example.olympiad.web.dto.contest.CreateContest.ContestAndFileResponse;
import com.example.olympiad.web.dto.contest.CreateContest.ContestRequest;
import com.example.olympiad.web.dto.contest.CreateContest.ContestResponse;
import com.example.olympiad.web.dto.contest.DeleteContestRequest;
import com.example.olympiad.web.dto.contest.GetStartAndEndContestTime.GetStartAndEndContestTimeRequest;
import com.example.olympiad.web.dto.contest.GetStartAndEndContestTime.GetStartAndEndContestTimeResponse;
import com.example.olympiad.web.dto.contest.createUsers.CreateUsersRequest;
import com.example.olympiad.web.dto.contest.createUsers.CreatedFile;
import com.example.olympiad.web.dto.contest.createUsers.FileResponse;
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Tag(name = "Admin controller", description = "Administrator management")
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Validated
public class AdminController {

    private final ContestService contestService;

    @Operation(summary = "Create contest", description = "Return created contest and users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Bad request - Contest already exists",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping("/createContest")
    public ResponseEntity<ContestAndFileResponse> createContest(@RequestBody final ContestRequest contestRequest) {
        ContestResponse contestResponse = contestService.create(contestRequest);
        File file = contestResponse.getFile();

        try {
            byte[] fileContent = Files.readAllBytes(file.toPath());
            ContestAndFileResponse response = new ContestAndFileResponse();
            response.setContest(contestResponse.getContest());


            response.setFileContent(fileContent);

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Operation(summary = "Create users for contest", description = "Return created users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Bad request - Contest does not exists",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping("/createUsers")
    public ResponseEntity<FileResponse> createUsers(@RequestBody final CreateUsersRequest createUsersRequest) {
        CreatedFile response = contestService.createUsers(createUsersRequest);

        File file = response.getFile();

        try {
            byte[] fileContent = Files.readAllBytes(file.toPath());
            FileResponse fileResponse = new FileResponse();
            fileResponse.setFileContent(fileContent);

            return ResponseEntity.ok(fileResponse);
        } catch (IOException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Operation(summary = "Get all contests", description = "Return all contests")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved")
    })
    @GetMapping("/contests")
    public ResponseEntity<List<AllContestsNameSessionResponse>> getAllContests() {
        List<AllContestsNameSessionResponse> contests = contestService.getAllContests();
        return ResponseEntity.ok(contests);
    }

    @Operation(summary = "Get contest by session", description = "Return contest by session")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Bad request - Contest does not exists",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @GetMapping("/contest/{session}")
    public ResponseEntity<Contest> getContestBySession(@PathVariable Long session) {
        Contest contest = contestService.getContestBySession(session);
        return ResponseEntity.ok(contest);
    }


    @Operation(summary = "Start contest", description = "Return contest start and end time")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Bad request - Contest does not exists",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping("/startContest")
    public ResponseEntity<GetStartAndEndContestTimeResponse> startContest(@RequestBody final GetStartAndEndContestTimeRequest getStartAndEndContestTimeRequest) {
        return ResponseEntity.ok(contestService
                .start(getStartAndEndContestTimeRequest.getSession()));
    }

    @Operation(summary = "Change duration", description = "Change contest duration")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Bad request - Contest does not exists",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping("/changeDuration")
    public ResponseEntity<Contest> changeDuration(@RequestBody final ChangeDurationRequest changeDurationRequest) {
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
    public ResponseEntity<Contest> addProblems(@RequestBody final AddProblemsRequest addProblemsRequest) {
        return ResponseEntity.ok(contestService
                .addProblems(addProblemsRequest));
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
