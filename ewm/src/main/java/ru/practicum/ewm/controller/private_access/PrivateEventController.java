package ru.practicum.ewm.controller.private_access;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.event.NewEventDto;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.ewm.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.dto.request.UpdateEventUserRequest;
import ru.practicum.ewm.service.EventService;
import ru.practicum.ewm.service.RequestService;
import ru.practicum.ewm.service.StatisticsService;

import java.util.List;

import static ru.practicum.ewm.utils.Constants.MAIN_SERVICE;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class PrivateEventController {

    private final EventService eventService;

    private final RequestService requestService;

    private final StatisticsService statService;

    @GetMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> findByInitiator(@PathVariable int userId,
                                               @RequestParam(defaultValue = "0") int from,
                                               @RequestParam(defaultValue = "10") int size,
                                               HttpServletRequest request) {
        statService.sendStat(MAIN_SERVICE, request);
        return eventService.findByInitiator(userId, from, size);
    }

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto add(@PathVariable int userId,
                            @Valid @RequestBody NewEventDto dto,
                            HttpServletRequest request) {
        statService.sendStat(MAIN_SERVICE, request);
        return eventService.add(userId, dto);
    }

    @GetMapping("/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto get(@PathVariable int userId, @PathVariable int eventId, HttpServletRequest request) {
        statService.sendStat(MAIN_SERVICE, request);
        return eventService.findByInitiatorAndEvent(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto update(@PathVariable int userId,
                               @PathVariable int eventId,
                               @Valid @RequestBody UpdateEventUserRequest dto,
                               HttpServletRequest request) {
        statService.sendStat(MAIN_SERVICE, request);
        return eventService.userUpdate(userId, eventId, dto);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getRequests(@PathVariable int userId,
                                                     @PathVariable int eventId,
                                                     HttpServletRequest request) {
        statService.sendStat(MAIN_SERVICE, request);
        return requestService.getRequestsInfo(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResult updateRequestStatus(
            @PathVariable int userId,
            @PathVariable int eventId,
            @Valid @RequestBody EventRequestStatusUpdateRequest updateRequest,
            HttpServletRequest request) {
        statService.sendStat(MAIN_SERVICE, request);
        return requestService.updateRequestStatus(userId, eventId, updateRequest);
    }

}