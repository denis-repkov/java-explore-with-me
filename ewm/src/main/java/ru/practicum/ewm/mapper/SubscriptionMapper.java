package ru.practicum.ewm.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.dto.subscription.SubscriptionDto;
import ru.practicum.ewm.model.Subscription;

@Component
public class SubscriptionMapper {

    public SubscriptionDto map(Subscription subscription) {
        return SubscriptionDto.builder()
                .userId(subscription.getId().getUserId())
                .subscriberId(subscription.getId().getSubscriberId())
                .build();
    }
}