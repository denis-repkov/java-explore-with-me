package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dal.CompilationRepository;
import ru.practicum.ewm.dal.EventRepository;
import ru.practicum.ewm.dal.RequestRepository;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.dto.compilation.UpdateCompilationRequest;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.mapper.CompilationMapper;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.model.Compilation;
import ru.practicum.ewm.model.Event;

import java.util.List;

import static ru.practicum.ewm.exception.ErrorMessages.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CompilationService {

    private final CompilationRepository compilationRepository;

    private final RequestRepository requestRepository;

    private final EventRepository eventRepository;

    private final CompilationMapper compilationMapper;

    private final EventMapper eventMapper;

    public CompilationDto add(NewCompilationDto request) {
        List<Event> events = eventRepository.findByIdIn(request.getEvents());
        Compilation toCreate = compilationMapper.map(request, events);
        Compilation created = compilationRepository.save(toCreate);
        return compilationMapper.map(created, created.getStatEvents().stream().map(eventMapper::mapToShort).toList());
    }

    public void delete(int id) {
        compilationRepository.deleteById(id);
    }

    public UpdateCompilationRequest update(int id, UpdateCompilationRequest request) {
        Compilation compilation = compilationRepository.findById(id).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND + id));
        List<Event> events = eventRepository.findByIdIn(request.getEvents());
        Compilation toUpdate = compilationMapper.map(id, request, events, compilation);
        return compilationMapper.map(compilationRepository.save(toUpdate));
    }

    public List<CompilationDto> findAll(boolean pinned, int from, int size) {
        return compilationRepository.findCompilations(pinned, from, size).stream()
                .map(c -> compilationMapper.map(c, eventMapper.mapToShort(c.getStatEvents())))
                .toList();
    }

    public CompilationDto findById(int id) {
        Compilation compilation = compilationRepository.findById(id).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND + id));
        return compilationMapper.map(compilation, eventMapper.mapToShort(compilation.getStatEvents()));
    }
}