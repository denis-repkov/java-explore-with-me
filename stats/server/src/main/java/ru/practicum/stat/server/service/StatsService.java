package ru.practicum.stat.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.stat.dto.CreateEndpointHitDto;
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

    public void saveHit(CreateEndpointHitDto hit) {
        EndpointHit toCreate = mapper.map(hit);
        hitRepository.save(toCreate);
    }

    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (uris != null && !uris.isEmpty()) {
            return hitRepository.findStatsByUris(start, end, uris, unique);
        }
        return hitRepository.findStats(start, end, unique);
    }
}