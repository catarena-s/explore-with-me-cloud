package dev.shvetsova.ewmc.stats.controller;

import dev.shvetsova.ewmc.stats.dto.EndpointHitDto;
import dev.shvetsova.ewmc.stats.dto.ViewStatsDto;
import dev.shvetsova.ewmc.stats.service.StatsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static dev.shvetsova.ewmc.stats.utils.Constants.*;

@RestController
@RequestMapping(path = "/")
@RequiredArgsConstructor
@Slf4j
@Validated
public class StatsServerController {
    private final StatsService statsService;

    @PostMapping(HIT_ENDPOINT)
    @ResponseStatus(HttpStatus.CREATED)
    public void saveHit(@Valid @RequestBody EndpointHitDto dto) {
        log.debug("Request received POST '{}' : {}", HIT_ENDPOINT, dto);
        statsService.saveHit(dto);
    }

    @GetMapping(STATS_ENDPOINT)
    public List<ViewStatsDto> getStats(@RequestParam(name = "start")
                                       @DateTimeFormat(pattern = YYYY_MM_DD_HH_MM_SS)
                                       LocalDateTime start,
                                       @RequestParam(name = "end")
                                       @DateTimeFormat(pattern = YYYY_MM_DD_HH_MM_SS)
                                       LocalDateTime end,
                                       @RequestParam(name = "uris", required = false) String[] uris,
                                       @RequestParam(name = "unique", defaultValue = "false") boolean unique
    ) {
        final String pathStr = getPathStr(start, end, uris, unique);
        log.debug("Request received GET '{}?{}'", STATS_ENDPOINT, pathStr);

        return statsService.getStats(start, end, uris, unique)
                .stream().sorted()
                .collect(Collectors.toList());
    }

    private String getPathStr(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique) {
        final List<String> path = new ArrayList<>();
        if (start != null) path.add("start=" + start.format(FORMATTER));
        if (end != null) path.add("end=" + end.format(FORMATTER));
        if (uris != null) path.add("uris=" + String.join("&uris=", uris));
        path.add("unique=" + unique);

        return String.join("&", path);
    }
}