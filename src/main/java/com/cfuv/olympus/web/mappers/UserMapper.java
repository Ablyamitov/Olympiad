package com.cfuv.olympus.web.mappers;

import com.cfuv.olympus.domain.user.User;
import com.cfuv.olympus.web.dto.user.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);

    User toEntity(UserDto dto);
}
