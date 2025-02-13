package ru.practicum.ewm.controller.admin_access;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.user.NewUserRequest;
import ru.practicum.ewm.dto.user.UserDto;
import ru.practicum.ewm.model.AllowedSubscriberGroup;
import ru.practicum.ewm.service.StatisticsService;
import ru.practicum.ewm.service.UserService;

import java.util.List;

import static ru.practicum.ewm.utils.Constants.MAIN_SERVICE;

@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    private final StatisticsService statService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> findAll(@RequestParam(required = false) List<Integer> ids,
                                 @RequestParam(defaultValue = "0") int from,
                                 @RequestParam(defaultValue = "10") int size,
                                 HttpServletRequest request) {
        statService.sendStat(MAIN_SERVICE, request);
        return userService.findAll(ids, from, size);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto findById(@PathVariable int id, HttpServletRequest request) {
        statService.sendStat(MAIN_SERVICE, request);
        return userService.findById(id);
    }

    @PatchMapping("/activation/{id}")
    @ResponseStatus(HttpStatus.OK)
    public boolean activateUser(@PathVariable int id, HttpServletRequest request) {
        statService.sendStat(MAIN_SERVICE, request);
        return userService.activate(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto add(@Valid @RequestBody NewUserRequest newUser, HttpServletRequest request) {
        statService.sendStat(MAIN_SERVICE, request);
        return userService.add(newUser);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable int id, HttpServletRequest request) {
        statService.sendStat(MAIN_SERVICE, request);
        userService.delete(id);
    }

    @PostMapping("/subscriber/group/{id}")
    @ResponseStatus(HttpStatus.OK)
    public boolean changeSubscriberGroup(@PathVariable int id,
                                         @RequestParam(defaultValue = "ALL", required = false) AllowedSubscriberGroup subscriberGroup,
                                         HttpServletRequest request) {
        statService.sendStat(MAIN_SERVICE, request);
        return userService.changeSubscriberGroup(id, subscriberGroup);
    }

}