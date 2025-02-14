package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dal.EventRepository;
import ru.practicum.ewm.dal.UserRepository;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.user.UserShortDto;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.mapper.UserMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    private final UserMapper userMapper;

    private final EventMapper eventMapper;

    @Transactional(readOnly = true)
    public List<UserShortDto> getUserRecommendations(int userId) {
        return userRepository.getRecommendations(userId).stream()
                .map(userMapper::map)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EventShortDto> getEventRecommendations(int userId) {
        return eventRepository.getRecommendations(userId).stream()
                .map(eventMapper::mapToShort)
                .toList();
    }
}