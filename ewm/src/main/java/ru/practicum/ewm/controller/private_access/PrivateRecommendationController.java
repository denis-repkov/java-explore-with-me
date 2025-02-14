package ru.practicum.ewm.controller.private_access;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.user.UserShortDto;
import ru.practicum.ewm.service.RecommendationService;
import ru.practicum.ewm.service.StatisticsService;

import java.util.List;

import static ru.practicum.ewm.utils.Constants.MAIN_SERVICE;

@RestController
@RequestMapping(path = "/users/recommendations")
@RequiredArgsConstructor
public class PrivateRecommendationController {

    private final RecommendationService recommendationService;

    private final StatisticsService statService;

    @GetMapping("/{userId}/user")
    public List<UserShortDto> getUserRecommendations(@PathVariable int userId,
                                                     HttpServletRequest request) {
        statService.sendStat(MAIN_SERVICE, request);
        return recommendationService.getUserRecommendations(userId);
    }

    @GetMapping("/{userId}/events")
    public List<EventShortDto> getEventRecommendations(@PathVariable int userId,
                                                       HttpServletRequest request) {
        statService.sendStat(MAIN_SERVICE, request);
        return recommendationService.getEventRecommendations(userId);
    }

}