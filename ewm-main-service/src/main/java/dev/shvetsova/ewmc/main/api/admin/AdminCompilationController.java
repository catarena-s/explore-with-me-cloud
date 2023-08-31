package dev.shvetsova.ewmc.main.api.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import dev.shvetsova.ewmc.main.dto.compilation.CompilationDto;
import dev.shvetsova.ewmc.main.dto.compilation.NewCompilationDto;
import dev.shvetsova.ewmc.main.dto.compilation.UpdateCompilationRequest;
import dev.shvetsova.ewmc.main.service.compilation.CompilationService;


@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@Slf4j
public class AdminCompilationController {
    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto saveCompilation(@Valid @RequestBody NewCompilationDto body) {
        log.debug("Request received POST /admin/compilations : {}", body);
        return compilationService.saveCompilation(body);
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateCompilation(@Valid @RequestBody UpdateCompilationRequest body,
                                            @PathVariable long compId) {
        log.debug("Request received PATCH /admin/compilations/{}:{}", compId, body);
        return compilationService.updateCompilation(body, compId);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable long compId) {
        log.debug("Request received DELETE /admin/compilations : {}", compId);
        compilationService.delete(compId);
    }
}
