package dev.shvetsova.ewmc.main.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import static dev.shvetsova.ewmc.main.enums.EnumMapper.getEnumFromString;

//import static ru.practicum.mapper.EnumMapper.getEnumFromString;

public enum RequestStatus {
    PENDING, CONFIRMED, REJECTED, CANCELED;

    @JsonCreator
    public static RequestStatus from(String name) {
        return getEnumFromString(RequestStatus.class, name, "Unknown request status");
    }
}
