package ru.practicum.stat.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.stat.dto.CreateEndpointHitDto;
import ru.practicum.stat.dto.EndpointHitDto;
import ru.practicum.stat.server.model.ViewStats;
import ru.practicum.stat.server.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatsController {

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private final StatsService statsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitDto saveHit(@RequestBody CreateEndpointHitDto hit) {
        log.info("Saving hit: " + hit.getUri());
        return statsService.saveHit(hit);
    }


    @GetMapping("/stats")
    public List<ViewStats> getStats(
            @RequestParam @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(required = false, defaultValue = "false") boolean unique) {
        log.info("Fetching stats from {} to {}, URIs: {}, Unique: {}", start, end, uris, unique);
        return statsService.getStats(start, end, uris, unique);
    }
}