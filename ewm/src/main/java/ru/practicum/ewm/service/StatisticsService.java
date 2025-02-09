package ru.practicum.ewm.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.model.Event;
import ru.practicum.stat.client.StatisticsClient;
import ru.practicum.stat.dto.CreateEndpointHitDto;
import ru.practicum.stat.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final StatisticsClient statClient;

    public void sendStat(Event event, HttpServletRequest request) {
        String ip = request.getRemoteAddr();

        CreateEndpointHitDto requestDto = new CreateEndpointHitDto();
        requestDto.setTimestamp(LocalDateTime.now());
        requestDto.setUri(event.getUri());
        requestDto.setApp(event.getServiceName());
        requestDto.setIp(ip);
        statClient.createRecord(requestDto);
    }

    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        return statClient.getStats(start, end, uris, unique);
    }

}