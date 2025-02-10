package ru.practicum.ewm.controller.admin_access;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.dto.compilation.UpdateCompilationRequest;
import ru.practicum.ewm.model.StatEvent;
import ru.practicum.ewm.service.CompilationService;
import ru.practicum.ewm.service.StatisticsService;

import static ru.practicum.ewm.utils.Constants.MAIN_SERVICE;

@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
public class AdminCompilationController {

    private final CompilationService compilationService;

    private final StatisticsService statService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto add(@Valid @RequestBody NewCompilationDto dto, HttpServletRequest request) {
        StatEvent statEvent = StatEvent.builder()
                .serviceName(MAIN_SERVICE)
                .uri(request.getRequestURI())
                .build();
        statService.sendStat(statEvent, request);
        return compilationService.add(dto);
    }

    @PatchMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public UpdateCompilationRequest updateCompilation(@PathVariable int compId,
                                                      @Valid @RequestBody UpdateCompilationRequest compRequest,
                                                      HttpServletRequest request) {
        StatEvent statEvent = StatEvent.builder()
                .serviceName(MAIN_SERVICE)
                .uri(request.getRequestURI())
                .build();
        statService.sendStat(statEvent, request);
        return compilationService.update(compId, compRequest);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable int compId, HttpServletRequest request) {
        StatEvent statEvent = StatEvent.builder()
                .serviceName(MAIN_SERVICE)
                .uri(request.getRequestURI())
                .build();
        statService.sendStat(statEvent, request);
        compilationService.delete(compId);
    }

}