package dev.shvetsova.ewmc.stats.service;

import dev.shvetsova.ewmc.stats.dto.EndpointHitDto;
import dev.shvetsova.ewmc.stats.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    /**
     * Add endpoint to storage
     * @param dto input data
     */
    void saveHit(EndpointHitDto dto);

    /**
     * get stats from storage
     * @param start start time for filter statistic
     * @param end end time for filter statistic
     * @param uris list or uri for filter statistic
     * @param unique true or false(if true get unique records by ip)
     * @return stats list in ViewStatsDto format
     */
    List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, String[] uris, Boolean unique);
}