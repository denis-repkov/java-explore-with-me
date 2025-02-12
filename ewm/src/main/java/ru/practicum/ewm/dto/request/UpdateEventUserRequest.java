package ru.practicum.ewm.dto.request;

import lombok.Data;
import ru.practicum.ewm.model.UserStateAction;

@Data
public class UpdateEventUserRequest extends UpdateEventRequest {

    private UserStateAction stateAction;

}