package dev.shvetsova.ewmc.dto.location;

/**
 * Широта и долгота места проведения события
 */

public class LocationDto {
    private Float lat;
    private Float lon;

    public LocationDto(Float lat, Float lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public LocationDto() {
    }

    public static LocationDtoBuilder builder() {
        return new LocationDtoBuilder();
    }

    public Float getLat() {
        return this.lat;
    }

    public Float getLon() {
        return this.lon;
    }

    public static class LocationDtoBuilder {
        private Float lat;
        private Float lon;

        LocationDtoBuilder() {
        }

        public LocationDtoBuilder lat(Float lat) {
            this.lat = lat;
            return this;
        }

        public LocationDtoBuilder lon(Float lon) {
            this.lon = lon;
            return this;
        }

        public LocationDto build() {
            return new LocationDto(this.lat, this.lon);
        }

        public String toString() {
            return "LocationDto.LocationDtoBuilder(lat=" + this.lat + ", lon=" + this.lon + ")";
        }
    }
}
