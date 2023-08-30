package dev.shvetsova.ewmc.main.service.location;

import dev.shvetsova.ewmc.main.dto.location.LocationDto;
import dev.shvetsova.ewmc.main.model.Location;

public interface LocationService {

    Location findLocation(LocationDto location);
}
