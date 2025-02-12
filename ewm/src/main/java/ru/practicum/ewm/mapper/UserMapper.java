package ru.practicum.ewm.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.dto.user.NewUserRequest;
import ru.practicum.ewm.dto.user.UserDto;
import ru.practicum.ewm.model.User;

@Component
public class UserMapper {

    public UserDto map(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public User map(NewUserRequest request) {
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .active(false)
                .build();
    }
}