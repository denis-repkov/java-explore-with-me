package ru.practicum.ewm.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.model.ParticipationRequest;

@Component
public class ParticipationRequestMapper {

    public ParticipationRequest map(ParticipationRequestDto dto) {
        return ParticipationRequest.builder()
                .id(dto.getId())
                .eventId(dto.getEvent())
                .created(dto.getCreated())
                .requesterId(dto.getRequester())
                .status(dto.getStatus())
                .build();
    }

    public ParticipationRequestDto map(ParticipationRequest dto) {
        return ParticipationRequestDto.builder()
                .id(dto.getId())
                .event(dto.getEventId())
                .created(dto.getCreated())
                .requester(dto.getRequesterId())
                .status(dto.getStatus())
                .build();
    }
}