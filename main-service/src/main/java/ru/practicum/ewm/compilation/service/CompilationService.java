package ru.practicum.ewm.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.compilation.controller.mapper.CompilationMapper;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.exception.ex.CompilationNotFountException;
import ru.practicum.ewm.utils.Page;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompilationService {
    private final CompilationRepository compilationStorage;
    private final EventService eventService;

    public ResponseEntity<Object> createCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto);
        Set<Event> events = new HashSet<>();

        if (newCompilationDto.getEvents() != null) {
            for (long eventId : newCompilationDto.getEvents()) {
                events.add(eventService.checkExistsEvent(eventId));
            }
        }

        log.info("Размер списка event после стрима: " + events.size());
        compilation.setEvents(events);
        Compilation saveComp = compilationStorage.save(compilation);
        CompilationDto compilationDto = CompilationMapper.toCompilationDto(saveComp);
        return new ResponseEntity<>(compilationDto, HttpStatus.CREATED);
    }

    public ResponseEntity<Object> deleteCompilation(long compId) {
        checkExistsCompilation(compId);
        compilationStorage.deleteById(compId);
        return new ResponseEntity<>("Подборка удалена.", HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<Object> updateCompilation(long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = checkExistsCompilation(compId);

        if (updateCompilationRequest.getEvents() != null) {
            Set<Event> events = new HashSet<>();

            for (long eventId : updateCompilationRequest.getEvents()) {
                events.add(eventService.checkExistsEvent(eventId));
            }
            compilation.setEvents(events);
        }

        if (updateCompilationRequest.getTitle() != null) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }

        if (updateCompilationRequest.getPinned() != null) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }

        CompilationDto compilationDto = CompilationMapper.toCompilationDto(compilationStorage.save(compilation));
        return new ResponseEntity<>(compilationDto, HttpStatus.OK);
    }

    public ResponseEntity<Object> getCompilations(boolean pinned, int from, int size) {
        PageRequest page = Page.createPageRequest(from, size);
        List<CompilationDto> compilations = compilationStorage.findAllByPinned(pinned, page).stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(compilations, HttpStatus.OK);
    }

    public ResponseEntity<Object> getCompilationById(long compId) {
        Compilation compilation = checkExistsCompilation(compId);
        return new ResponseEntity<>(CompilationMapper.toCompilationDto(compilation), HttpStatus.OK);
    }

    private Compilation checkExistsCompilation(long compId) {
        Optional<Compilation> compilationOptional = compilationStorage.findById(compId);

        if (compilationOptional.isEmpty()) {
            throw new CompilationNotFountException("Compilation with " + compId + " was not found");
        }

        return compilationOptional.get();
    }
}
