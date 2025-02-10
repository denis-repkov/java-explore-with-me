package ru.practicum.ewm.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatEvent {

    private String uri;

    private String serviceName;
}