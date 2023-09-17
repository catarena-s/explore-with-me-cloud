package dev.shvetsova.ewmc.stats.mapper;

import dev.shvetsova.ewmc.dto.stats.EndpointHitDto;
import dev.shvetsova.ewmc.stats.model.EndpointHit;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EndpointHitMapper {

    public static EndpointHit fromDto(EndpointHitDto dto) {
        return EndpointHit.builder()
                .app(dto.getApp())
                .uri(dto.getUri())
                .ip(dto.getIp())
                .timestamp(dto.getTimestamp())
                .build();
    }
}