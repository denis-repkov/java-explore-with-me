package ru.practicum.ewm.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.dto.user.NewUserRequest;
import ru.practicum.ewm.dto.user.UserDto;
import ru.practicum.ewm.dto.user.UserShortDto;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.model.UserShort;

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
                .subscriberGroup(request.getSubscriberGroup())
                .build();
    }

    public UserShortDto map(UserShort userShort) {
        return UserShortDto.builder()
                .id(userShort.getId())
                .name(userShort.getName())
                .build();
    }
}