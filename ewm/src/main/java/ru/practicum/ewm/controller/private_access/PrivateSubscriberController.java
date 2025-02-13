package ru.practicum.ewm.controller.private_access;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.subscription.SubscriptionDto;
import ru.practicum.ewm.dto.user.UserShortDto;
import ru.practicum.ewm.service.StatisticsService;
import ru.practicum.ewm.service.SubscriptionService;

import java.util.List;

import static ru.practicum.ewm.utils.Constants.MAIN_SERVICE;

@RestController
@RequestMapping(path = "/users/subscriptions")
@RequiredArgsConstructor
public class PrivateSubscriberController {

    private final SubscriptionService subscriptionService;

    private final StatisticsService statService;

    @GetMapping("/{userId}/{subscriberId}")
    public boolean exists(@PathVariable int userId,
                          @PathVariable int subscriberId,
                          HttpServletRequest request) {
        statService.sendStat(MAIN_SERVICE, request);
        return subscriptionService.exists(userId, subscriberId);
    }

    @GetMapping("/{userId}")
    public List<UserShortDto> getSubscribers(@PathVariable int userId,
                                             HttpServletRequest request) {
        statService.sendStat(MAIN_SERVICE, request);
        return subscriptionService.findSubscribers(userId);
    }

    @PostMapping("/{userId}/{subscriberId}")
    public SubscriptionDto subscribe(@PathVariable int userId,
                                     @PathVariable int subscriberId,
                                     HttpServletRequest request) {
        statService.sendStat(MAIN_SERVICE, request);
        return subscriptionService.subscribe(userId, subscriberId);
    }

    @DeleteMapping("/{userId}/{subscriberId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unsubscribe(@PathVariable int userId,
                            @PathVariable int subscriberId,
                            HttpServletRequest request) {
        statService.sendStat(MAIN_SERVICE, request);
        subscriptionService.unsubscribe(userId, subscriberId);
    }

}