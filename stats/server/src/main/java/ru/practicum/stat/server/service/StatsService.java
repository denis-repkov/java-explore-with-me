package ru.practicum.stat.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.stat.dto.CreateEndpointHitDto;
import ru.practicum.stat.dto.EndpointHitDto;
import ru.practicum.stat.server.exception.ConditionException;
import ru.practicum.stat.server.mapper.EndpointHitMapper;
import ru.practicum.stat.server.model.EndpointHit;
import ru.practicum.stat.server.model.ViewStats;
import ru.practicum.stat.server.repository.EndpointHitRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final EndpointHitRepository hitRepository;

    private final EndpointHitMapper mapper;

    public EndpointHitDto saveHit(CreateEndpointHitDto hit) {
        EndpointHit toCreate = mapper.map(hit);
        EndpointHit created = hitRepository.save(toCreate);
        return mapper.map(created);
    }

    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (start.isAfter(end)) {
            throw new ConditionException("Время начала должно быть позже времени окончания");
        }
        if (uris != null && !uris.isEmpty()) {
            return hitRepository.findStatsByUris(start, end, uris, unique);
        }
        return hitRepository.findStats(start, end, unique);
    }
}