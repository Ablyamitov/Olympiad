package com.example.olympiad.web.controller;

import com.example.olympiad.domain.contest.Contest;
import com.example.olympiad.service.ContestService;
import com.example.olympiad.service.UserService;
import com.example.olympiad.web.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;
    private final ContestService contestService;

    private final UserMapper userMapper;


    @DeleteMapping("/{id}")

    public void deleteById(@PathVariable Long id) {
        userService.delete(id);
    }


    /*@GetMapping("/ContestTime/{session}")
    public DeferredResult<ResponseEntity<GetStartAndEndContestTimeResponse>> getContestTime(@PathVariable Long session) {

        DeferredResult<ResponseEntity<GetStartAndEndContestTimeResponse>> output = new DeferredResult<>(60000L);

        new Thread(() -> {
            while (!output.hasResult()) {
                Contest contest = contestService.getContestBySession(session);
                if (contest.getStartTime() != null) {
                    GetStartAndEndContestTimeResponse response = new GetStartAndEndContestTimeResponse();
                    response.setStartTime(contest.getStartTime());
                    response.setEndTime(contest.getEndTime());
                    output.setResult(ResponseEntity.ok(response));
                    break;
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException("Error: " + e.getMessage());
                }
            }
        }).start();

        output.onTimeout(() -> output.setErrorResult(
                ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body("Request timeout occurred.")));

        return output;

    }*/


    @GetMapping("/contest/{session}")
    public ResponseEntity<Contest> getContestBySession(@PathVariable Long session) throws InterruptedException {
        try {
            return ResponseEntity.ok(contestService.getContestOptionalBySession(session));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(null);
        }

    }


}
