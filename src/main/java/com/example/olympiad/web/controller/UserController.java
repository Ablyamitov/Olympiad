package com.example.olympiad.web.controller;

import com.example.olympiad.domain.contest.Contest;
import com.example.olympiad.domain.user.User;
import com.example.olympiad.service.ContestService;
import com.example.olympiad.service.UserService;
import com.example.olympiad.web.dto.contest.GetStartAndEndContestTime.GetStartAndEndContestTimeResponse;
import com.example.olympiad.web.dto.user.UserDto;
import com.example.olympiad.web.dto.validation.OnUpdate;
import com.example.olympiad.web.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;
    private final ContestService contestService;

    private final UserMapper userMapper;

    @PutMapping
    public UserDto update(@Validated(OnUpdate.class)
                          @RequestBody UserDto dto) {
        User user = userMapper.toEntity(dto);
        User updatedUser = userService.update(user);
        return userMapper.toDto(updatedUser);
    }

    @PostMapping("/createParticipant")
    public List<User> createParticipant(){
        List<User> users = new ArrayList<>();
        IntStream.range(0, 20)
                .mapToObj(i -> {
                    User user = new User();
                    user.setSession(1L);
                    user.setName("Ivan" + i);
                    user.setUsername("Ivan" + i);
                    user.setPassword("12345");
                    return userService.create(user);
                })
                .forEach(users::add);
        return users;
    }


    @GetMapping("/{id}")
    public UserDto getById(@PathVariable Long id) {
        User user = userService.getById(id);
        return userMapper.toDto(user);
    }

    @DeleteMapping("/{id}")

    public void deleteById(@PathVariable Long id) {
        userService.delete(id);
    }


    @GetMapping("/ContestTime/{session}")
    public DeferredResult<ResponseEntity<GetStartAndEndContestTimeResponse>> getContestTime(@PathVariable Long session) {
        /*DeferredResult<ResponseEntity<GetStartAndEndContestTimeResponse>> output = new DeferredResult<>();
        requests.add(output);

        output.onCompletion(() -> requests.remove(output));*/

        DeferredResult<ResponseEntity<GetStartAndEndContestTimeResponse>> output = new DeferredResult<>(60000L);

        // Запускаем новый поток, который будет проверять, началась ли олимпиада
        new Thread(() -> {
            while (!output.hasResult()) {
                Contest contest = contestService.getContestBySession(session);
                if (contest.getStartTime() != null) {
                    // Олимпиада началась, устанавливаем результат и выходим из цикла
                    GetStartAndEndContestTimeResponse response = new GetStartAndEndContestTimeResponse();
                    response.setStartTime(contest.getStartTime());
                    response.setEndTime(contest.getEndTime());
                    output.setResult(ResponseEntity.ok(response));
                    break;
                }

                try {
                    Thread.sleep(1000); // ждем 1 секунду перед следующей проверкой
                } catch (InterruptedException e) {
                    throw new RuntimeException("Error: " + e.getMessage());
                }
            }
        }).start();

        output.onTimeout(() -> output.setErrorResult(
                ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body("Request timeout occurred.")));

        return output;

    }

}
