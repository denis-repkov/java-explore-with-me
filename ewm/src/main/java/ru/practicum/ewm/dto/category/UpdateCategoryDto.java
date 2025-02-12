package ru.practicum.ewm.dto.category;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateCategoryDto {

    @Size(min = 1, max = 50)
    private String name;

}