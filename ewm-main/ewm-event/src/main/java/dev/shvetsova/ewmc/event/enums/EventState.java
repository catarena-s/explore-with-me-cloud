package dev.shvetsova.ewmc.event.enums;

import lombok.Getter;

@Getter
public enum EventState {
    CANCELED,
    PENDING,
    PUBLISHED;

    public static EventState from(String name) {
        return EnumMapper.getEnumFromString(EventState.class, name, "Unknown event state");
    }

}