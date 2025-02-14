package ru.practicum.ewm.model;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
@Embeddable
@EqualsAndHashCode
public class SubscriptionId implements Serializable {

    private int userId;
    private int subscriberId;

    public SubscriptionId() {
    }

    public SubscriptionId(int userId, int subscriberId) {
        this.userId = userId;
        this.subscriberId = subscriberId;
    }
}