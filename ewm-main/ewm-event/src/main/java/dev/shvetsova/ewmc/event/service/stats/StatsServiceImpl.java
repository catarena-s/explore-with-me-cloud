package dev.shvetsova.ewmc.event.service.stats;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.shvetsova.ewmc.dto.stats.EndpointHitDto;
import dev.shvetsova.ewmc.dto.stats.ViewStatsDto;
import dev.shvetsova.ewmc.event.http.StatsClient;
import dev.shvetsova.ewmc.exception.ResponseException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static dev.shvetsova.ewmc.utils.Constants.END;
import static dev.shvetsova.ewmc.utils.Constants.START;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    @Value(value = "${app.name}")
    private String appName;
    private final StatsClient statsClient;

    @Override
    public void save(HttpServletRequest request) {
        saveHit(request.getRemoteAddr(), request.getRequestURI());
    }

    @Override
    public void save(HttpServletRequest request, List<Long> ids) {
        ids.forEach(id -> saveHit(request.getRemoteAddr(), request.getRequestURI() + "/" + id));
    }

    @Override
    public Map<String, Long> getMap(HttpServletRequest request, List<Long> collect, boolean unique) {
        final List<ViewStatsDto> viewStats = get(request, collect, START, END, unique);
        return getGroupedMap(viewStats);
    }

    @Override
    public Map<String, Long> getMap(HttpServletRequest request, List<Long> collect, LocalDateTime start, LocalDateTime end, boolean unique) {
        final List<ViewStatsDto> viewStats = get(request, collect, start, end, unique);
        return getGroupedMap(viewStats);
    }

    @Override
    public Map<String, Long> getMap(HttpServletRequest request, boolean unique) {
        final List<ViewStatsDto> viewStats = get(request, null, START, END, unique);
        return getGroupedMap(viewStats);
    }

    private Map<String, Long> getGroupedMap(List<ViewStatsDto> viewStats) {
        if (viewStats.isEmpty()) return new HashMap<>();
        return viewStats.stream()
                .collect(Collectors.groupingBy(ViewStatsDto::getUri, Collectors.summingLong(ViewStatsDto::getHits)));
    }

    private List<ViewStatsDto> get(HttpServletRequest request, List<Long> ids, LocalDateTime start, LocalDateTime end,
                                   boolean unique) {
        final ObjectMapper objectMapper = new ObjectMapper();
        final String requestURI = request.getRequestURI();
        final List<ViewStatsDto> response = (ids != null)
                ? getResponse(requestURI, ids, start, end, unique)
                : getResponse(List.of(requestURI), start, end, unique);
        return (response != null)
                ? response.stream()
                .map(object -> objectMapper.convertValue(object, ViewStatsDto.class))
                .filter(v -> v.getApp().equals(appName)).toList()
                : Collections.emptyList();
    }


    private List<ViewStatsDto> getResponse(String requestURI, List<Long> ids, LocalDateTime start, LocalDateTime end, boolean unique) {
        final List<String> idsList = ids.stream()
                .map(id -> requestURI + "/" + id)
                .toList();
        return getResponse(idsList, start, end, unique);
    }

    private List<ViewStatsDto> getResponse(List<String> idsList, LocalDateTime start, LocalDateTime end, boolean unique) {
        final ResponseEntity<List<ViewStatsDto>> response = statsClient.getStats(start, end, idsList, unique);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new ResponseException("Failed to get data from stats service.");
        }
        return response.getBody();
    }

    private void saveHit(String ip, String uri) {
        final EndpointHitDto dto = EndpointHitDto.builder()
                .app(appName)
                .ip(ip)
                .uri(uri)
                .timestamp(LocalDateTime.now())
                .build();
        final ResponseEntity<Object> response = statsClient.saveHit(dto);
        if (response.getStatusCode() != HttpStatus.CREATED) {
            throw new ResponseException("Failed to save data to stats service.");
        }
    }
}
