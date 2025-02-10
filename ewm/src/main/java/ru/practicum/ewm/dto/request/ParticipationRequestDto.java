package ru.practicum.ewm.dto.request;

import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.model.RequestStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class ParticipationRequestDto {

    private Integer id;

    private int event;

    private LocalDateTime created;

    private int requester;

    private RequestStatus status;

}