package ru.practicum.ewm.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@Table(name = "subscriptions", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {

    @EmbeddedId
    private SubscriptionId id;
}