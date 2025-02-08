package ru.practicum.stat.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EndpointHitDto {

    private int id;

    private String app;

    private String uri;

    private String ip;

    private LocalDateTime timestamp;
}