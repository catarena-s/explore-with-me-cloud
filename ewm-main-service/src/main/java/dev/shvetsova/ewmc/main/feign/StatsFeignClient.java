package dev.shvetsova.ewmc.main.feign;

import dev.shvetsova.ewmc.common.dto.EndpointHitDto;
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

import static dev.shvetsova.ewmc.common.Constants.*;

@FeignClient(name = "ewm-stats")
public interface StatsFeignClient {
    @PostMapping(HIT_ENDPOINT)
    ResponseEntity<Object> saveHit(@Valid @RequestBody EndpointHitDto dto);

    @GetMapping(STATS_ENDPOINT)
    ResponseEntity<Object> getStats(@RequestParam(name = "start")
                                   @DateTimeFormat(pattern = YYYY_MM_DD_HH_MM_SS)
                                   LocalDateTime start,
                                   @RequestParam(name = "end")
                                   @DateTimeFormat(pattern = YYYY_MM_DD_HH_MM_SS)
                                   LocalDateTime end,
                                   @RequestParam(name = "uris", required = false) List<String> uris,
                                   @RequestParam(name = "unique", defaultValue = "false") boolean unique);
}


