package ru.practicum.ewm.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.model.StatEvent;
import ru.practicum.stat.client.StatisticsClient;
import ru.practicum.stat.dto.CreateEndpointHitDto;
import ru.practicum.stat.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final StatisticsClient statClient;

    public void sendStat(StatEvent statEvent, HttpServletRequest request) {
        String ip = request.getRemoteAddr();

        CreateEndpointHitDto requestDto = new CreateEndpointHitDto();
        requestDto.setTimestamp(LocalDateTime.now());
        requestDto.setUri(statEvent.getUri());
        requestDto.setApp(statEvent.getServiceName());
        requestDto.setIp(ip);
        statClient.createRecord(requestDto);
    }

    public List<ViewStatsDto> getStats(String start, String end, List<String> uris, boolean unique) {
        return statClient.getStats(start, end, uris, unique);
    }

}