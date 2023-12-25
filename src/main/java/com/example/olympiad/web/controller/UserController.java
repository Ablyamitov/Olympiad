package com.example.olympiad.web.controller;

import com.example.olympiad.domain.user.User;
import com.example.olympiad.service.UserService;
import com.example.olympiad.web.dto.user.UserDto;
import com.example.olympiad.web.dto.validation.OnUpdate;
import com.example.olympiad.web.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    private final UserMapper userMapper;

    @PutMapping
    public UserDto update(@Validated(OnUpdate.class)
                          @RequestBody UserDto dto) {
        User user = userMapper.toEntity(dto);
        User updatedUser = userService.update(user);
        return userMapper.toDto(updatedUser);
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

}
