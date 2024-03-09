package com.example.olympiad.web.controller;

import com.example.olympiad.domain.contest.Contest;
import com.example.olympiad.domain.contest.Tasks;
import com.example.olympiad.repository.TasksRepository;
import com.example.olympiad.service.ContestService;
import com.example.olympiad.web.dto.contest.*;
import com.example.olympiad.web.dto.contest.CreateContest.ContestAndFileResponse;
import com.example.olympiad.web.dto.contest.CreateContest.ContestRequest;
import com.example.olympiad.web.dto.contest.CreateContest.ContestResponse;
import com.example.olympiad.web.dto.contest.GetContestAndTasks.ContestAndTasksResponse;
import com.example.olympiad.web.dto.contest.GetStartAndEndContestTime.GetStartAndEndContestTimeRequest;
import com.example.olympiad.web.dto.contest.GetStartAndEndContestTime.GetStartAndEndContestTimeResponse;
import com.example.olympiad.web.dto.contest.createUsers.CreateUsersRequest;
import com.example.olympiad.web.dto.contest.createUsers.CreatedFile;
import com.example.olympiad.web.dto.contest.createUsers.FileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.config.Task;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Validated
public class AdminController {

    private final ContestService contestService;
    private final TasksRepository tasksRepository;

    @PostMapping("/createContest")
    public ResponseEntity<ContestAndFileResponse> createContest(@RequestBody final ContestRequest contestRequest) {
        ContestResponse contestResponse = contestService.create(contestRequest);
        File file = contestResponse.getFile();

        try {
            byte[] fileContent = Files.readAllBytes(file.toPath());
            ContestAndFileResponse response = new ContestAndFileResponse();
            response.setContest(contestResponse.getContest());

            response.getContest().getTasks().size();

            response.setFileContent(fileContent);

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

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

    @GetMapping("/contests")
    public ResponseEntity<List<AllContestsNameSessionResponse>> getAllContests() {
        List<AllContestsNameSessionResponse> contests = contestService.getAllContests();
        return ResponseEntity.ok(contests);
    }

    @GetMapping("/contest/{session}")
    public ResponseEntity<Contest> getContestBySession(@PathVariable Long session) {
        Contest contest = contestService.getContestBySession(session);
        //contest.getTasks().size();
        //ContestAndTasksResponse response = new ContestAndTasksResponse();
        //response.setContest(contest);
        return ResponseEntity.ok(contest);
    }


    @PostMapping("/startContest")
    public ResponseEntity<GetStartAndEndContestTimeResponse> startContest(@RequestBody final GetStartAndEndContestTimeRequest getStartAndEndContestTimeRequest) {
        GetStartAndEndContestTimeResponse getStartAndEndContestTimeResponse = contestService
                .start(getStartAndEndContestTimeRequest.getSession());
        return ResponseEntity.ok(getStartAndEndContestTimeResponse);
    }

    @PostMapping("/deleteContest")
    public ResponseEntity<Void> deleteContest(@RequestBody final DeleteContestRequest deleteContestRequest) {
        contestService.deleteContest(deleteContestRequest.getContestSession());
        return ResponseEntity.ok().build();
    }
}
