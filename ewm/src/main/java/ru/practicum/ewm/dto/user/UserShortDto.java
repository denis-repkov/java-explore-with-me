package ru.practicum.ewm.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserShortDto {

    private Integer id;

    private String name;
}