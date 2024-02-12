package ru.practicum.ewm.compilation.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewm.compilation.service.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
public class CompilationsAdminController {

    private final CompilationService compilationService;

    @PostMapping
    public ResponseEntity<Object> createCompilation(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        log.info("Запрос на создание подборки. " +
                "event: " + newCompilationDto.getEvents());
        return compilationService.createCompilation(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<Object> deleteCompilation(@PathVariable @Positive long compId) {
        log.info("Запрос на удаление подборки id = {}", compId);
        return compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<Object> updateCompilation(@PathVariable @Positive long compId,
                             @RequestBody @Valid UpdateCompilationRequest updateCompilationRequest) {
        log.info("Запрос на обновление подборки id = {}", compId);
        return compilationService.updateCompilation(compId, updateCompilationRequest);
    }
}
