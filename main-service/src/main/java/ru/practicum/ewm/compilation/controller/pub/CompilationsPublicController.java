package ru.practicum.ewm.compilation.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/compilations")
public class CompilationsPublicController {

    private final CompilationService compilationService;

    @GetMapping
    public ResponseEntity<Object> getCompilations(@RequestParam(defaultValue = "false") boolean pinned,
                                                  @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                  @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Запрос подборки событий, pinned = {}, from = {}, size = {}", pinned, from, size);
        return compilationService.getCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public ResponseEntity<Object> getCompilationById(@PathVariable @Positive long compId) {
        log.info("Запрос подборки по id = {}", compId);
        return compilationService.getCompilationById(compId);
    }
}
