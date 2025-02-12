package ru.practicum.ewm.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.dto.compilation.UpdateCompilationRequest;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.model.Compilation;
import ru.practicum.ewm.model.Event;

import java.util.List;

@Component
public class CompilationMapper {

    public Compilation map(NewCompilationDto compilation, List<Event> events) {
        return Compilation.builder()
                .statEvents(events)
                .pinned(compilation.isPinned())
                .title(compilation.getTitle())
                .build();
    }

    public CompilationDto map(Compilation compilation, List<EventShortDto> events) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .events(events)
                .pinned(compilation.isPinned())
                .title(compilation.getTitle())
                .build();
    }

    public UpdateCompilationRequest map(Compilation compilation) {
        return UpdateCompilationRequest.builder()
                .id(compilation.getId())
                .events(compilation.getStatEvents().stream().map(Event::getId).toList())
                .pinned(compilation.isPinned())
                .title(compilation.getTitle())
                .build();
    }

    public Compilation map(int id, UpdateCompilationRequest request, List<Event> events, Compilation compilation) {
        return Compilation.builder()
                .id(id)
                .statEvents(events != null ? events : compilation.getStatEvents())
                .pinned(request.getPinned() != null ? request.getPinned() : compilation.isPinned())
                .title(request.getTitle() != null ? request.getTitle() : compilation.getTitle())
                .build();
    }
}