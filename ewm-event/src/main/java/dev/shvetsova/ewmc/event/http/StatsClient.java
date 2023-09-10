package dev.shvetsova.ewmc.event.http;

import dev.shvetsova.ewmc.event.dto.EndpointHitDto;
import dev.shvetsova.ewmc.event.dto.ViewStatsDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

import static dev.shvetsova.ewmc.event.utils.Constants.*;

@FeignClient(name = "ewm-stats")
public interface StatsClient {
    @PostMapping(HIT_ENDPOINT)
    ResponseEntity<Object> saveHit(@Valid @RequestBody EndpointHitDto dto);

    @GetMapping(STATS_ENDPOINT)
    ResponseEntity<List<ViewStatsDto>> getStats(@RequestParam(name = "start")
                                                @DateTimeFormat(pattern = YYYY_MM_DD_HH_MM_SS)
                                                LocalDateTime start,
                                                @RequestParam(name = "end")
                                                @DateTimeFormat(pattern = YYYY_MM_DD_HH_MM_SS)
                                                LocalDateTime end,
                                                @RequestParam(name = "uris", required = false) List<String> uris,
                                                @RequestParam(name = "unique", defaultValue = "false") boolean unique);
}


