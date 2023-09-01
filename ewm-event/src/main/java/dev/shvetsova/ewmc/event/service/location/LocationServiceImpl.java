package dev.shvetsova.ewmc.event.service.location;

import dev.shvetsova.ewmc.event.mapper.LocationMapper;
import dev.shvetsova.ewmc.event.model.Location;
import dev.shvetsova.ewmc.event.repo.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import dev.shvetsova.ewmc.event.dto.location.LocationDto;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;

    @Override
    public Location findLocation(LocationDto body) {
        return locationRepository.findByLatAndLon(body.getLat(), body.getLon())
                .orElseGet(() -> saveLocation(body));
    }

    private Location saveLocation(LocationDto body) {
        return locationRepository.save(LocationMapper.fromDto(body));
    }
}
