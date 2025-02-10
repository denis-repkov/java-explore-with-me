package ru.practicum.ewm.controller.admin_access;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.request.UpdateEventAdminRequest;
import ru.practicum.ewm.model.EventStatus;
import ru.practicum.ewm.model.StatEvent;
import ru.practicum.ewm.service.EventService;
import ru.practicum.ewm.service.StatisticsService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.utils.Constants.DATE_TIME_FORMAT;
import static ru.practicum.ewm.utils.Constants.MAIN_SERVICE;


@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
public class AdminEventController {

    private final EventService eventService;

    private final StatisticsService statService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> get(
            @RequestParam(required = false) List<Integer> users,
            @RequestParam(required = false) List<EventStatus> states,
            @RequestParam(required = false) List<Integer> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request
    ) {
        StatEvent statEvent = StatEvent.builder()
                .serviceName(MAIN_SERVICE)
                .uri(request.getRequestURI())
                .build();
        statService.sendStat(statEvent, request);
        return eventService.adminFindAll(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto update(@PathVariable int eventId,
                               @Valid @RequestBody UpdateEventAdminRequest dto,
                               HttpServletRequest request) {
        StatEvent statEvent = StatEvent.builder()
                .serviceName(MAIN_SERVICE)
                .uri(request.getRequestURI())
                .build();
        statService.sendStat(statEvent, request);
        return eventService.adminUpdate(eventId, dto);
    }

}