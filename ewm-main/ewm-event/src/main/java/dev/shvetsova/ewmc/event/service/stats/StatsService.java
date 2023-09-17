package dev.shvetsova.ewmc.event.service.stats;


import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface StatsService {
    void save(HttpServletRequest request);

    void save(HttpServletRequest request, List<Long> ids);

    Map<String, Long> getMap(HttpServletRequest request, List<Long> collect, boolean unique);

    Map<String, Long> getMap(HttpServletRequest request, List<Long> collect, LocalDateTime start, LocalDateTime end, boolean unique);

    Map<String, Long> getMap(HttpServletRequest request, boolean unique);
}
