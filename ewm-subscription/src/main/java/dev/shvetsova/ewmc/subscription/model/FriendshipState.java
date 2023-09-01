package dev.shvetsova.ewmc.subscription.model;


import dev.shvetsova.ewmc.subscription.mapper.EnumMapper;

public enum FriendshipState {
    PENDING, APPROVED, REJECTED;

    public static FriendshipState from(String name) {
        return EnumMapper.getEnumFromString(FriendshipState.class, name, "Unknown friendship status");
    }
}
