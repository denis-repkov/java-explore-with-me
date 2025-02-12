package ru.practicum.ewm.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.event.NewEventDto;
import ru.practicum.ewm.dto.request.UpdateEventAdminRequest;
import ru.practicum.ewm.dto.request.UpdateEventUserRequest;
import ru.practicum.ewm.dto.user.UserShortDto;
import ru.practicum.ewm.model.AdminStateAction;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.EventStatus;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.model.UserStateAction;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class EventMapper {

    public EventFullDto mapToFull(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryDto.builder()
                        .id(event.getCategory().getId())
                        .name(event.getCategory().getName())
                        .build())
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(UserShortDto.builder()
                        .id(event.getInitiator().getId())
                        .name(event.getInitiator().getName())
                        .build())
                .location(event.getLocation())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.isRequestModeration())
                .state(event.getState().toString())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public EventShortDto mapToShort(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .description(event.getDescription())
                .annotation(event.getAnnotation())
                .category(CategoryDto.builder()
                        .id(event.getCategory().getId())
                        .name(event.getCategory().getName())
                        .build())
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .initiator(UserShortDto.builder()
                        .id(event.getInitiator().getId())
                        .name(event.getInitiator().getName())
                        .build())
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public List<EventShortDto> mapToShort(List<Event> events) {
        return events.stream()
                .map(this::mapToShort)
                .toList();
    }

    public Event mapToFull(User initiator, Category category, NewEventDto event) {
        return Event.builder()
                .annotation(event.getAnnotation())
                .category(category)
                .confirmedRequests(0)
                .createdOn(LocalDateTime.now())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(initiator)
                .location(event.getLocation())
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .state(EventStatus.PENDING)
                .title(event.getTitle())
                .views(0)
                .build();
    }

    public Event updatedEvent(Event event, UpdateEventAdminRequest uEvent, Category newCat) {
        EventStatus state = event.getState();
        LocalDateTime publishedOn = null;
        if (uEvent.getStateAction() != null) {
            if (uEvent.getStateAction().equals(AdminStateAction.PUBLISH_EVENT)) {
                state = EventStatus.PUBLISHED;
                publishedOn = LocalDateTime.now();
            } else {
                state = EventStatus.CANCELED;
            }
        }
        return Event.builder()
                .id(event.getId())
                .annotation(uEvent.getAnnotation() != null ? uEvent.getAnnotation() : event.getAnnotation())
                .category(uEvent.getCategory() != null ? newCat : event.getCategory())
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .publishedOn(publishedOn)
                .description(uEvent.getDescription() != null ? uEvent.getDescription() : event.getDescription())
                .eventDate(uEvent.getEventDate() != null ? uEvent.getEventDate() : event.getEventDate())
                .initiator(event.getInitiator())
                .location(uEvent.getLocation() != null ? uEvent.getLocation() : event.getLocation())
                .paid(uEvent.getPaid() != null ? uEvent.getPaid() : event.getPaid())
                .participantLimit(uEvent.getParticipantLimit() != null ? uEvent.getParticipantLimit() : event.getParticipantLimit())
                .requestModeration(uEvent.getRequestModeration() != null ? uEvent.getRequestModeration() : event.isRequestModeration())
                .state(state)
                .title(uEvent.getTitle() != null ? uEvent.getTitle() : event.getTitle())
                .views(event.getViews())
                .build();
    }

    public Event updatedEvent(Event event, UpdateEventUserRequest uEvent, Category newCat) {
        EventStatus state = event.getState();
        if (uEvent.getStateAction() != null) {
            if (uEvent.getStateAction().equals(UserStateAction.SEND_TO_REVIEW)) {
                state = EventStatus.PENDING;
            } else {
                state = EventStatus.CANCELED;
            }
        }
        return Event.builder()
                .id(event.getId())
                .annotation(uEvent.getAnnotation() != null ? uEvent.getAnnotation() : event.getAnnotation())
                .category(uEvent.getCategory() != null ? newCat : event.getCategory())
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(uEvent.getDescription() != null ? uEvent.getDescription() : event.getDescription())
                .eventDate(uEvent.getEventDate() != null ? uEvent.getEventDate() : event.getEventDate())
                .initiator(event.getInitiator())
                .location(uEvent.getLocation() != null ? uEvent.getLocation() : event.getLocation())
                .paid(uEvent.getPaid() != null ? uEvent.getPaid() : event.getPaid())
                .participantLimit(uEvent.getParticipantLimit() != null ? uEvent.getParticipantLimit() : event.getParticipantLimit())
                .requestModeration(uEvent.getRequestModeration() != null ? uEvent.getRequestModeration() : event.isRequestModeration())
                .state(state)
                .title(uEvent.getTitle() != null ? uEvent.getTitle() : event.getTitle())
                .views(event.getViews())
                .build();
    }
}