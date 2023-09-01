package dev.shvetsova.ewmc.request.model;

import com.fasterxml.jackson.annotation.JsonCreator;


public enum RequestStatus {
    PENDING, CONFIRMED, REJECTED, CANCELED;

    @JsonCreator
    public static RequestStatus from(String name) {
        return EnumMapper.getEnumFromString(RequestStatus.class, name, "Unknown request status");
    }
}
