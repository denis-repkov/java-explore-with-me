package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dal.EventRepository;
import ru.practicum.ewm.dal.RequestRepository;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.ewm.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.exception.ApproveRequestException;
import ru.practicum.ewm.exception.AuthentificationException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.mapper.ParticipationRequestMapper;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.EventStatus;
import ru.practicum.ewm.model.ParticipationRequest;
import ru.practicum.ewm.model.RequestStatus;

import java.util.ArrayList;
import java.util.List;

import static ru.practicum.ewm.exception.ErrorMessages.EVENT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;

    private final EventRepository eventRepository;

    private final ParticipationRequestMapper requestMapper;

    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getRequestsByUserId(int userId) {
        return requestRepository.findByRequesterId(userId)
                .stream()
                .map(requestMapper::map)
                .toList();
    }

    @Transactional
    public ParticipationRequestDto createParticipationRequest(int userId, int eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(EVENT_NOT_FOUND + eventId));
        if (event.getInitiator().getId().equals(userId)) {
            throw new ValidationException("Инициатор мероприятия не может отправить заявку на участие");
        }
        if (!event.getState().equals(EventStatus.PUBLISHED)) {
            throw new ValidationException("Невозможно присоединиться к неопубликованному мероприятию");
        }
        if (!requestRepository.findByRequesterIdAndEventId(userId, eventId).isEmpty()) {
            throw new ValidationException("Дублирование запроса не допускается");
        }
        if (event.getParticipantLimit() > 0 && event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new ApproveRequestException("Достигнут лимит участников в мероприятии");
        }

        RequestStatus status;
        if (!event.isRequestModeration()) {
            status = RequestStatus.CONFIRMED;
        } else {
            if (event.getParticipantLimit() == 0) {
                status = RequestStatus.CONFIRMED;
            } else {
                status = RequestStatus.PENDING;
            }
        }

        if (status.equals(RequestStatus.CONFIRMED)) {
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        }

        ParticipationRequest request = ParticipationRequest.builder()
                .requesterId(userId)
                .eventId(eventId)
                .status(status)
                .build();

        ParticipationRequest savedRequest = requestRepository.save(request);
        return requestMapper.map(savedRequest);
    }

    @Transactional
    public ParticipationRequestDto cancelRequest(int userId, int requestId) {
        ParticipationRequest request = requestRepository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new ValidationException("Запрос не найден"));

        if (request.getRequesterId() != userId) {
            throw new AuthentificationException("Только запрашивающая сторона может отменить свой запрос");
        }

        request.setStatus(RequestStatus.CANCELED);
        ParticipationRequest updatedRequest = requestRepository.save(request);

        return requestMapper.map(updatedRequest);
    }

    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getRequestsInfo(int userId, int eventId) {
        List<ParticipationRequest> requests = requestRepository.findByInitiatorIdAndEventId(userId, eventId);
        return requests.stream()
                .map(requestMapper::map)
                .toList();
    }

    @Transactional
    public EventRequestStatusUpdateResult updateRequestStatus(int initiatorId, int eventId, EventRequestStatusUpdateRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(EVENT_NOT_FOUND + eventId));

        if (initiatorId != event.getInitiator().getId()) {
            throw new AuthentificationException("На запрос может ответить только инициатор мероприятия");
        }

        if (event.getParticipantLimit() == event.getConfirmedRequests()) {
            throw new ApproveRequestException(String.format("Достигнут лимит участников для мероприятия %s", eventId));
        }

        if (event.getParticipantLimit() == 0 || !event.isRequestModeration()) {
            return new EventRequestStatusUpdateResult();
        }

        List<ParticipationRequest> reqs = requestRepository.findByIdIn(request.getRequestIds());

        if (request.getStatus().equals(RequestStatus.REJECTED)) {
            return new EventRequestStatusUpdateResult(
                    new ArrayList<>(),
                    reqs.stream().peek(r -> r.setStatus(RequestStatus.REJECTED)).map(requestMapper::map).toList());
        }

        int availableRequestsToConfirm = Math.min(event.getParticipantLimit() - event.getConfirmedRequests(), reqs.size());
        event.setConfirmedRequests(event.getConfirmedRequests() + availableRequestsToConfirm);
        eventRepository.save(event);

        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult(new ArrayList<>(), new ArrayList<>());
        int i = 0;
        while (availableRequestsToConfirm - i > 0) {
            ParticipationRequest req = reqs.get(i);
            if (!req.getStatus().equals(RequestStatus.PENDING)) {
                throw new ApproveRequestException(String.format("Запрос %s должен иметь статус PENDING", req.getId()));
            }

            req.setStatus(RequestStatus.CONFIRMED);
            result.getConfirmedRequests().add(requestMapper.map(req));

            i++;
        }

        for (int j = i; j < reqs.size(); j++) {
            ParticipationRequest req = reqs.get(j);
            req.setStatus(RequestStatus.REJECTED);
            result.getRejectedRequests().add(requestMapper.map(req));
        }

        requestRepository.saveAll(reqs);

        return result;
    }
}