package ru.practicum.ewm.category.service;

import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto postCategory(CategoryDto categoryDto);

    void deleteCategory(long id);

    CategoryDto patchCategory(long id, CategoryDto categoryDto);

    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategoryById(long catId);

    Category checkExistCategory(long categoryId);
}
