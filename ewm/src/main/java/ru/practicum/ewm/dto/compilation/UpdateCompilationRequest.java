package ru.practicum.ewm.dto.compilation;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UpdateCompilationRequest {

    private int id;

    private List<Integer> events;

    private Boolean pinned;

    @Size(min = 1, max = 50)
    private String title;
}