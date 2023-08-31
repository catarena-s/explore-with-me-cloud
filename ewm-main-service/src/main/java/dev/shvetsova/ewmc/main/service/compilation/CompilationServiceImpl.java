package dev.shvetsova.ewmc.main.service.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import dev.shvetsova.ewmc.main.dto.compilation.CompilationDto;
import dev.shvetsova.ewmc.main.dto.compilation.NewCompilationDto;
import dev.shvetsova.ewmc.main.dto.compilation.UpdateCompilationRequest;
import dev.shvetsova.ewmc.main.exception.ConflictException;
import dev.shvetsova.ewmc.main.exception.NotFoundException;
import dev.shvetsova.ewmc.main.mapper.CompilationMapper;
import dev.shvetsova.ewmc.main.model.Compilation;
import dev.shvetsova.ewmc.main.model.Event;
import dev.shvetsova.ewmc.main.repository.CompilationRepository;
import dev.shvetsova.ewmc.main.service.event.EventService;
import dev.shvetsova.ewmc.main.utils.Constants;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static dev.shvetsova.ewmc.main.utils.Constants.THE_REQUIRED_OBJECT_WAS_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventService eventService;

    @Override
    public Compilation findCompilationById(long compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(
                        String.format(Constants.COMPILATION_WITH_ID_WAS_NOT_FOUND, compId),
                        THE_REQUIRED_OBJECT_WAS_NOT_FOUND));
    }

    @Override
    public CompilationDto getCompilation(long compId) {
        final Compilation compilation = findCompilationById(compId);
        return CompilationMapper.toDto(compilation);
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        final PageRequest page = PageRequest.of(from / size, size);
        final List<Compilation> compilations =
                (pinned == null)
                        ? compilationRepository.findAll(page).getContent()
                        : compilationRepository.findAllByPinned(pinned, page).getContent();
        return compilations.stream()
                .map(CompilationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto saveCompilation(NewCompilationDto body) {
        try {
            final List<Long> eventIdList = body.getEvents();
            final List<Event> events = (eventIdList != null)
                    ? eventService.findEventsByIds(eventIdList)
                    : Collections.emptyList();
            final Compilation compilation = compilationRepository.save(CompilationMapper.fromDto(body, events));
            return CompilationMapper.toDto(compilation);
        } catch (
                DataIntegrityViolationException ex) {
            throw new ConflictException(String.format("Compilation with title='%s' already exists", body.getTitle()));
        }
    }

    @Override
    public void delete(long compId) {
        if (!compilationRepository.existsById(compId)) {
            throw new NotFoundException(
                    String.format(Constants.COMPILATION_WITH_ID_WAS_NOT_FOUND, compId),
                    THE_REQUIRED_OBJECT_WAS_NOT_FOUND);
        }
        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationDto updateCompilation(UpdateCompilationRequest body, long compId) {
        final Compilation compilation = findCompilationById(compId);

        if (body.getPinned() != null) {
            compilation.setPinned(body.getPinned());
        }
        if (body.getTitle() != null) {
            compilation.setTitle(body.getTitle());
        }
        if (body.getEvents() != null && !body.getEvents().isEmpty()) {
            final List<Event> events = eventService.findEventsByIds(body.getEvents());
            compilation.setEvents(events);
        }
        final Compilation savedCompilation = compilationRepository.save(compilation);
        return CompilationMapper.toDto(savedCompilation);
    }
}
