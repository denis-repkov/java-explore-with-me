package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dal.UserRepository;
import ru.practicum.ewm.dto.user.NewUserRequest;
import ru.practicum.ewm.dto.user.UserDto;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.mapper.UserMapper;
import ru.practicum.ewm.model.User;

import java.util.List;

import static ru.practicum.ewm.exception.ErrorMessages.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public List<UserDto> findAll(List<Integer> ids, int from, int size) {
        Pageable page = PageRequest.of(from / size, size);
        return userRepository.findAll(ids, page).stream()
                .map(userMapper::map)
                .toList();
    }

    public UserDto findById(int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND + id));
        return userMapper.map(user);
    }

    public UserDto add(NewUserRequest request) {
        User toCreate = userMapper.map(request);
        User created = userRepository.save(toCreate);
        return userMapper.map(created);
    }

    public boolean activate(int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND + id));
        user.setActive(true);
        return userRepository.save(user) != null;
    }

    public void delete(int id) {
        if (userRepository.findById(id).isEmpty()) {
            throw new NotFoundException(USER_NOT_FOUND + id);
        }
        userRepository.deleteById(id);
    }
}