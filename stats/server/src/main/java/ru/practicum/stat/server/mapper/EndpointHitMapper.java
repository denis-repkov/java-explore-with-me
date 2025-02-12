package ru.practicum.stat.server.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.stat.dto.CreateEndpointHitDto;
import ru.practicum.stat.dto.EndpointHitDto;
import ru.practicum.stat.server.model.EndpointHit;

@Component
public class EndpointHitMapper {

    public EndpointHit map(CreateEndpointHitDto request) {
        return EndpointHit.builder()
                .app(request.getApp())
                .uri(request.getUri())
                .ip(request.getIp())
                .timestamp(request.getTimestamp())
                .build();
    }

    public EndpointHitDto map(EndpointHit request) {
        return EndpointHitDto.builder()
                .id(request.getId())
                .app(request.getApp())
                .uri(request.getUri())
                .ip(request.getIp())
                .timestamp(request.getTimestamp())
                .build();
    }
}