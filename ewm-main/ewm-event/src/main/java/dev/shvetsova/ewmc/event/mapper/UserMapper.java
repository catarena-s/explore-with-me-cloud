package dev.shvetsova.ewmc.event.mapper;

import dev.shvetsova.ewmc.dto.user.UserShortDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

    public static UserShortDto toShotDto(long userId) {
        return UserShortDto.builder()
                .id(userId)
                .build();
    }
}
