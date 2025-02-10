package ru.practicum.ewm.dto.request;

import lombok.Data;
import ru.practicum.ewm.model.AdminStateAction;

@Data
public class UpdateEventAdminRequest extends UpdateEventRequest {

    private AdminStateAction stateAction;

}