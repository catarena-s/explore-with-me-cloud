package dev.shvetsova.ewmc.event.enums;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EnumMapper {
    public static <T extends Enum<T>> T getEnumFromString(Class<T> c, String string, String errMsg) {
        if (c == null || string == null) return null;
        try {
            return Enum.valueOf(c, string.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(String.format("%s: %s", errMsg, string));
        }
    }
}
