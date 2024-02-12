package ru.practicum.ewm.category.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.service.CategoryService;

import javax.validation.constraints.Positive;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
public class CategoryAdminController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Object> createCategory(@RequestBody @Validated CategoryDto categoryDto) {
        log.info("Запрос на создание категории name = {}", categoryDto.getName());
        return categoryService.createCategory(categoryDto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable @Positive int catId) {
        log.info("Запрос на удаление категории id = {}", catId);
        categoryService.deleteCategory(catId);
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<Object> updateCategory(@PathVariable @Positive int catId,
                                                 @RequestBody @Validated CategoryDto categoryDto) {
        log.info("Запрос на обновление категории id = {}, name = {}", catId, categoryDto.getName());
        return categoryService.updateCategory(catId, categoryDto);
    }
}
