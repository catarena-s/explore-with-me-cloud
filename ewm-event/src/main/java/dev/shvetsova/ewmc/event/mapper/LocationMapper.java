package dev.shvetsova.ewmc.event.mapper;

import dev.shvetsova.ewmc.event.dto.location.LocationDto;
import dev.shvetsova.ewmc.event.model.Location;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocationMapper {
    public static Location fromDto(LocationDto location) {
        return Location.builder()
                .lon(location.getLon())
                .lat(location.getLat())
                .build();
    }

    public static LocationDto toDto(Location location) {
        return LocationDto.builder()
                .lon(location.getLon())
                .lat(location.getLat())
                .build();
    }
}
