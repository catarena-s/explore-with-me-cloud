package dev.shvetsova.ewmc.event.service.location;

import dev.shvetsova.ewmc.event.dto.location.LocationDto;
import dev.shvetsova.ewmc.event.model.Location;

public interface LocationService {

    Location findLocation(LocationDto location);
}
