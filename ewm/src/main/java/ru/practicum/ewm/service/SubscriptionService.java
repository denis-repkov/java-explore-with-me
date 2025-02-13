package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dal.SubscriptionRepository;
import ru.practicum.ewm.dal.UserRepository;
import ru.practicum.ewm.dto.subscription.SubscriptionDto;
import ru.practicum.ewm.dto.user.UserShortDto;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.mapper.SubscriptionMapper;
import ru.practicum.ewm.mapper.UserMapper;
import ru.practicum.ewm.model.Subscription;
import ru.practicum.ewm.model.SubscriptionId;
import ru.practicum.ewm.model.User;

import java.util.List;

import static ru.practicum.ewm.exception.ErrorMessages.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final UserRepository userRepository;

    private final SubscriptionRepository subscriptionRepository;

    private final UserMapper userMapper;

    private final SubscriptionMapper subscriptionMapper;

    @Transactional(readOnly = true)
    public List<UserShortDto> findSubscribers(int userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(USER_NOT_FOUND + userId);
        }
        return subscriptionRepository.findSubscribers(userId).stream()
                .map(userMapper::map)
                .toList();

    }

    @Transactional
    public SubscriptionDto subscribe(int userId, int subscriberId) {
        if (!userRepository.existsById(userId) || !userRepository.existsById(subscriberId)) {
            throw new NotFoundException("Пользователи " + userId + " и " + subscriberId + " должны существовать");
        }

        SubscriptionId id = SubscriptionId.builder()
                .userId(userId)
                .subscriberId(subscriberId)
                .build();

        if (subscriptionRepository.existsById(id)) {
            throw new ValidationException("Подписка уже существует");
        }
        checkSubscribersGroup(userId, subscriberId);

        Subscription request = Subscription.builder()
                .id(id)
                .build();
        return subscriptionMapper.map(subscriptionRepository.save(request));
    }

    @Transactional(readOnly = true)
    public boolean exists(int userId, int subscriberId) {
        SubscriptionId id = SubscriptionId.builder()
                .userId(userId)
                .subscriberId(subscriberId)
                .build();

        return subscriptionRepository.existsById(id);
    }

    @Transactional
    public void unsubscribe(int userId, int subscriberId) {
        if (!userRepository.existsById(userId) || !userRepository.existsById(subscriberId)) {
            throw new NotFoundException(
                    String.format("Пользователь %s и подписчик %s должны существовать в базе данных", userId, subscriberId));
        }

        SubscriptionId id = SubscriptionId.builder()
                .userId(userId)
                .subscriberId(subscriberId)
                .build();

        if (!subscriptionRepository.existsById(id)) {
            throw new NotFoundException("Подписки не существует");
        }
        subscriptionRepository.deleteById(id);
    }

    private void checkSubscribersGroup(int userId, int subscriberId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND + userId));

        switch (user.getSubscriberGroup()) {
            case NOBODY -> throw new ValidationException("Никто не может подписаться на пользователя " + userId);

            case SUBSCRIBER_OF_SUBSCRIBERS -> {
                if (!subscriptionRepository.isSubscriberOfSubscribers(userId, subscriberId)) {
                    throw new ValidationException("Только подписчики подписчиков могут подписаться на пользователя " + userId);
                }
            }
        }
    }
}