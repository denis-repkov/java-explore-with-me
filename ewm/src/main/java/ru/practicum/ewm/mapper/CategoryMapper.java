package ru.practicum.ewm.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.category.NewCategoryDto;
import ru.practicum.ewm.dto.category.UpdateCategoryDto;
import ru.practicum.ewm.model.Category;

import java.util.List;

@Component
public class CategoryMapper {

    public CategoryDto map(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public Category map(NewCategoryDto dto) {
        return Category.builder()
                .name(dto.getName())
                .build();
    }

    public Category map(int id, UpdateCategoryDto dto) {
        return Category.builder()
                .id(id)
                .name(dto.getName())
                .build();
    }

    public List<CategoryDto> map(List<Category> categories) {
        return categories.stream()
                .map(this::map)
                .toList();
    }
}