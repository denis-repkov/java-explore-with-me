package ru.practicum.ewm.dto.request;

import lombok.Data;
import ru.practicum.ewm.model.RequestStatus;

import java.util.List;

@Data
public class EventRequestStatusUpdateRequest {

    List<Integer> requestIds;

    RequestStatus status;

}