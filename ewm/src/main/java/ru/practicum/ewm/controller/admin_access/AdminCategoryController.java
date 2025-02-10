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
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.category.NewCategoryDto;
import ru.practicum.ewm.dto.category.UpdateCategoryDto;
import ru.practicum.ewm.model.StatEvent;
import ru.practicum.ewm.service.CategoryService;
import ru.practicum.ewm.service.StatisticsService;

import static ru.practicum.ewm.utils.Constants.MAIN_SERVICE;

@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryService categoryService;

    private final StatisticsService statService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto add(@Valid @RequestBody NewCategoryDto dto, HttpServletRequest request) {
        StatEvent statEvent = StatEvent.builder()
                .serviceName(MAIN_SERVICE)
                .uri(request.getRequestURI())
                .build();
        statService.sendStat(statEvent, request);
        return categoryService.add(dto);
    }

    @PatchMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto update(@PathVariable int catId, @Valid @RequestBody UpdateCategoryDto dto, HttpServletRequest request) {
        StatEvent statEvent = StatEvent.builder()
                .serviceName(MAIN_SERVICE)
                .uri(request.getRequestURI())
                .build();
        statService.sendStat(statEvent, request);
        return categoryService.update(catId, dto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCategory(@PathVariable int catId, HttpServletRequest request) {
        StatEvent statEvent = StatEvent.builder()
                .serviceName(MAIN_SERVICE)
                .uri(request.getRequestURI())
                .build();
        statService.sendStat(statEvent, request);
        categoryService.delete(catId);
    }
}