package dev.shvetsova.ewmc.stats.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder(toBuilder = true)
public class EndpointHitFilter {
    private String app;
    private String[] uris;
    private String ip;
    private LocalDateTime timestampBefore;
    private LocalDateTime timestampAfter;
}