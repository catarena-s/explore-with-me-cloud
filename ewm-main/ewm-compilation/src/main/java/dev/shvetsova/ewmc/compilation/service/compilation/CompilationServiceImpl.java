package dev.shvetsova.ewmc.compilation.service.compilation;

import dev.shvetsova.ewmc.compilation.http.EventClient;
import dev.shvetsova.ewmc.compilation.mapper.CompilationMapper;
import dev.shvetsova.ewmc.compilation.model.Compilation;
import dev.shvetsova.ewmc.compilation.model.CompilationEvent;
import dev.shvetsova.ewmc.compilation.model.CompilationEventKey;
import dev.shvetsova.ewmc.compilation.repo.CompilationEventRepository;
import dev.shvetsova.ewmc.compilation.repo.CompilationRepository;
import dev.shvetsova.ewmc.dto.compilation.CompilationDto;
import dev.shvetsova.ewmc.dto.compilation.NewCompilationDto;
import dev.shvetsova.ewmc.dto.compilation.UpdateCompilationRequest;
import dev.shvetsova.ewmc.dto.event.EventShortDto;
import dev.shvetsova.ewmc.exception.ConflictException;
import dev.shvetsova.ewmc.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static dev.shvetsova.ewmc.utils.Constants.COMPILATION_WITH_ID_WAS_NOT_FOUND;
import static dev.shvetsova.ewmc.utils.Constants.THE_REQUIRED_OBJECT_WAS_NOT_FOUND;


@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final CompilationEventRepository compilationEventRepository;
    private final EventClient eventClient;

    @Override
    public CompilationDto getCompilation(long compId) {
        final Compilation compilation = findCompilationById(compId);
        final List<Long> eventIds = compilationEventRepository.findAllByCompilationId(compId);
        final List<EventShortDto> events = getEventListForCompilation(eventIds);
        return CompilationMapper.toDto(compilation, events);
    }

    private List<EventShortDto> getEventListForCompilation(List<Long> eventIds) {
        return (eventIds != null)
                ? eventClient.findEventsByIds(eventIds)
                : Collections.emptyList();
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        final PageRequest page = PageRequest.of(from / size, size);
        final List<Compilation> compilations = (pinned == null)
                ? compilationRepository.findAll(page).getContent()
                : compilationRepository.findAllByPinned(pinned, page).getContent();
        final List<CompilationEventKey> allByCompilationIdIn = compilationEventRepository.findAllByCompilationIdIn(
                compilations.stream()
                        .map(Compilation::getId)
                        .toList());
        final Map<Long, List<Long>> compilationEventIds = allByCompilationIdIn.stream()
                .collect(Collectors.groupingBy(CompilationEventKey::getCompilationId,
                        Collectors.mapping(CompilationEventKey::getEventId, Collectors.toList())));

        final List<Long> eventIds = compilationEventIds.values().stream()
                .flatMap(Collection::stream)
                .toList();

        final List<EventShortDto> events = getEventListForCompilation(eventIds);

        final List<CompilationDto> compilationDtoList = new ArrayList<>();
        for (Compilation c : compilations) {
            List<Long> compilationEvents = compilationEventIds.get(c.getId());
            if (compilationEvents == null || compilationEvents.isEmpty()) {
                compilationDtoList.add(CompilationMapper.toDto(c, null));
                continue;
            }
            List<EventShortDto> dtoList = events.stream().filter(e -> compilationEvents.contains(e.getId())).toList();
            compilationDtoList.add(CompilationMapper.toDto(c, dtoList));
        }
        return compilationDtoList;
    }

    @Override
    public CompilationDto saveCompilation(NewCompilationDto body) {
        try {
            final Compilation compilation = compilationRepository.save(CompilationMapper.fromDto(body));
            CompilationDto dto = CompilationMapper.toDto(compilation);
            final List<Long> eventIdList = body.getEvents();
            if (eventIdList != null && !eventIdList.isEmpty()) {
                final List<EventShortDto> events = getEventListForCompilation(eventIdList);
                List<CompilationEvent> c = events.stream()
                        .map(m -> new CompilationEvent(new CompilationEventKey(compilation.getId(), m.getId())))
                        .toList();
                compilationEventRepository.saveAll(c);
                dto.setEvents(events);
            }
            return dto;
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException(String.format("Compilation with title='%s' already exists", body.getTitle()));
        }
    }

    @Override
    @Transactional
    public void delete(long compId) {
        if (!compilationRepository.existsById(compId)) {
            throw new NotFoundException(String.format(COMPILATION_WITH_ID_WAS_NOT_FOUND, compId),
                    THE_REQUIRED_OBJECT_WAS_NOT_FOUND);
        }
        compilationEventRepository.deleteAllById_CompilationId(compId);
        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(UpdateCompilationRequest body, long compId) {
        final Compilation compilation = findCompilationById(compId);

        if (body.getPinned() != null) {
            compilation.setPinned(body.getPinned());
        }
        if (body.getTitle() != null) {
            compilation.setTitle(body.getTitle());
        }
        final Compilation savedCompilation = compilationRepository.save(compilation);
        CompilationDto dto = CompilationMapper.toDto(savedCompilation);
        compilationEventRepository.deleteAllById_CompilationId(compId);
        if (body.getEvents() != null && !body.getEvents().isEmpty()) {
            final List<EventShortDto> events = eventClient.findEventsByIds(body.getEvents());
            List<CompilationEvent> c = events.stream()
                    .map(m -> new CompilationEvent(new CompilationEventKey(compilation.getId(), m.getId())))
                    .toList();
            compilationEventRepository.saveAll(c);
            dto.setEvents(events);
        }
        return dto;
    }

    private Compilation findCompilationById(long compId) {
        return compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException(String.format(COMPILATION_WITH_ID_WAS_NOT_FOUND, compId),
                        THE_REQUIRED_OBJECT_WAS_NOT_FOUND));
    }
}