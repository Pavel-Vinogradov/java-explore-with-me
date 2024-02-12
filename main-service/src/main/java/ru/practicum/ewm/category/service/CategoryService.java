package ru.practicum.ewm.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.exception.ex.CategoryNotFountException;
import ru.practicum.ewm.utils.Page;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryStorage;

    public ResponseEntity<Object> createCategory(CategoryDto categoryDto) {
        Category saveCategory = categoryStorage.save(CategoryMapper.toCategory(categoryDto));
        return new ResponseEntity<>(saveCategory, HttpStatus.CREATED);
    }

    public void deleteCategory(int catId) {
        checkExistCategory(catId);
        categoryStorage.deleteById(catId);
    }

    public ResponseEntity<Object> updateCategory(int catId, CategoryDto categoryDto) {
        Category category = CategoryMapper.toCategory(categoryDto);
        category.setId(catId);
        Category saveCategory = categoryStorage.save(category);
        return new ResponseEntity<>(saveCategory, HttpStatus.OK);
    }

    public ResponseEntity<Object> getAll(int from, int size) {
        PageRequest page = Page.createPageRequest(from, size);
        List<CategoryDto> categories = categoryStorage.findAll(page).stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    public ResponseEntity<Object> getCategoryById(int catId) {
        Category category = checkExistCategory(catId);
        CategoryDto categoryDto = CategoryMapper.toCategoryDto(category);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

    public Category checkExistCategory(int catId) {
        Optional<Category> optionalCategory = categoryStorage.findById(catId);

        if (optionalCategory.isEmpty()) {
            throw new CategoryNotFountException("Категория не найдена или недоступна");
        }

        return optionalCategory.get();
    }
}