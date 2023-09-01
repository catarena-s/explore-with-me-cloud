package dev.shvetsova.ewmc.main.service.location;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import dev.shvetsova.ewmc.common.dto.location.LocationDto;
import dev.shvetsova.ewmc.main.mapper.LocationMapper;
import dev.shvetsova.ewmc.main.model.Location;
import dev.shvetsova.ewmc.main.repository.LocationRepository;

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
