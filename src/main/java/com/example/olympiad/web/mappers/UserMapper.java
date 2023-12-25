package com.example.olympiad.web.mappers;

import com.example.olympiad.domain.user.User;
import com.example.olympiad.web.dto.user.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);

    User toEntity(UserDto dto);
}
