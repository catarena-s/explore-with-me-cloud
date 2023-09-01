package dev.shvetsova.ewmc.common.enums;

import lombok.Getter;

//import static ru.practicum.mapper.EnumMapper.getEnumFromString;

@Getter
public enum EventState {
    CANCELED,
    PENDING,
    PUBLISHED;

    public static EventState from(String name) {
        return EnumMapper.getEnumFromString(EventState.class, name, "Unknown event state");
    }

}