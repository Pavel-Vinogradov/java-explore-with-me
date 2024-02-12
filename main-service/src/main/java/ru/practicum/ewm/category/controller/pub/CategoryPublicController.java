package ru.practicum.ewm.category.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.service.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryPublicController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<Object> getCategories(@RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Запрос на возврат всех категорий, from = {}, size = {}", from, size);
        return categoryService.getAll(from, size);
    }

    @GetMapping("/{catId}")
    public ResponseEntity<Object> getCategoryById(@PathVariable @Positive int catId) {
        log.info("Запрос на возврат категории id = {}", catId);
        return categoryService.getCategoryById(catId);
    }
}