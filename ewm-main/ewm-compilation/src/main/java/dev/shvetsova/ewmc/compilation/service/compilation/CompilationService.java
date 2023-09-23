package dev.shvetsova.ewmc.compilation.service.compilation;

import dev.shvetsova.ewmc.dto.compilation.CompilationDto;
import dev.shvetsova.ewmc.dto.compilation.NewCompilationDto;
import dev.shvetsova.ewmc.dto.compilation.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {
    void delete(long compId);

    CompilationDto saveCompilation(NewCompilationDto body);

    CompilationDto updateCompilation(UpdateCompilationRequest body, long compId);

    CompilationDto getCompilation(long compId);

    List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);
}
