package dev.shvetsova.compilation.api;

import dev.shvetsova.compilation.service.compilation.CompilationService;
import dev.shvetsova.ewmc.dto.compilation.CompilationDto;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static dev.shvetsova.ewmc.utils.Constants.FROM;
import static dev.shvetsova.ewmc.utils.Constants.PAGE_SIZE;


@RestController
@RequestMapping(path = "/compilations")
@Slf4j
@RequiredArgsConstructor
@Validated
public class PublicCompilationController {
    private final CompilationService compilationService;

    /**
     * В случае, если подборки с заданным id не найдено, возвращает статус код 404
     */
    @GetMapping("/{compId}")
    public CompilationDto getCompilation(@PathVariable long compId) {
        log.debug("Request received GET /compilations/{}", compId);
        return compilationService.getCompilation(compId);
    }

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @PositiveOrZero @RequestParam(value = "from", defaultValue = FROM) Integer from,
                                                @Positive @RequestParam(value = "size", defaultValue = PAGE_SIZE) Integer size) {
        log.debug("Request received GET /compilations");
        log.debug("RequestParams: pinned={},from={},size={}", pinned, from, size);
        return compilationService.getCompilations(pinned, from, size);
    }

}
