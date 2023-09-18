package dev.shvetsova.ewmc.compilation.mapper;

import dev.shvetsova.ewmc.compilation.model.Compilation;
import dev.shvetsova.ewmc.dto.compilation.CompilationDto;
import dev.shvetsova.ewmc.dto.compilation.NewCompilationDto;
import dev.shvetsova.ewmc.dto.event.EventShortDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CompilationMapper {
    public static Compilation fromDto(NewCompilationDto body) {
        return Compilation.builder()
                .title(body.getTitle())
                .pinned(body.isPinned())
                .build();
    }

    public static CompilationDto toDto(Compilation c, List<EventShortDto> events) {
        return CompilationDto.builder()
                .id(c.getId())
                .title(c.getTitle())
                .pinned(c.getPinned())
                .events(events == null || events.isEmpty() ? Collections.emptyList() : events)
                .build();
    }

    public static CompilationDto toDto(Compilation c) {
        return CompilationDto.builder()
                .id(c.getId())
                .title(c.getTitle())
                .pinned(c.getPinned())
                .events(Collections.emptyList())
                .build();
    }
}
