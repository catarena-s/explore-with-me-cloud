package dev.shvetsova.ewmc.main.enums;

//import static ru.practicum.mapper.EnumMapper.getEnumFromString;

import static dev.shvetsova.ewmc.main.enums.EnumMapper.getEnumFromString;

public enum FriendshipState {
    PENDING, APPROVED, REJECTED;

    public static FriendshipState from(String name) {
        return getEnumFromString(FriendshipState.class, name, "Unknown friendship status");
    }
}
