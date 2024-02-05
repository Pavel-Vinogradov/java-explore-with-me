package ru.practicum.ewm.category.mapper;

import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.dto.CategoryDto;

public class CategoryMapper {
    public static Category toCategory(CategoryDto categoryDto) {
        return new Category(categoryDto.getId(), categoryDto.getName());
    }

    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }
}
