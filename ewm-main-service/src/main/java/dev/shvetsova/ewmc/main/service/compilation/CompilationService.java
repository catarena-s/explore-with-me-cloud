package dev.shvetsova.ewmc.main.service.compilation;

import dev.shvetsova.ewmc.common.dto.compilation.CompilationDto;
import dev.shvetsova.ewmc.common.dto.compilation.NewCompilationDto;
import dev.shvetsova.ewmc.common.dto.compilation.UpdateCompilationRequest;
import dev.shvetsova.ewmc.main.model.Compilation;

import java.util.List;

public interface CompilationService {
    void delete(long compId);

    CompilationDto saveCompilation(NewCompilationDto body);

    CompilationDto updateCompilation(UpdateCompilationRequest body, long compId);

    Compilation findCompilationById(long compId);

    CompilationDto getCompilation(long compId);

    List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);
}
