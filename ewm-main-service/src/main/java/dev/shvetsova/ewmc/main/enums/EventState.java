package dev.shvetsova.ewmc.main.enums;

import lombok.Getter;

import static dev.shvetsova.ewmc.main.enums.EnumMapper.getEnumFromString;

//import static ru.practicum.mapper.EnumMapper.getEnumFromString;

@Getter
public enum EventState {
    CANCELED,
    PENDING,
    PUBLISHED;

    public static EventState from(String name) {
        return getEnumFromString(EventState.class, name, "Unknown event state");
    }

}