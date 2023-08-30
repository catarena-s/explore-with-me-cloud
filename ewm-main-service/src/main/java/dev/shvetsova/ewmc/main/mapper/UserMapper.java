package dev.shvetsova.ewmc.main.mapper;

import dev.shvetsova.ewmc.main.dto.user.NewUserRequest;
import dev.shvetsova.ewmc.main.dto.user.UserDto;
import dev.shvetsova.ewmc.main.dto.user.UserShortDto;
import dev.shvetsova.ewmc.main.model.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {
    public static User fromDto(NewUserRequest body) {
        return User.builder()
                .name(body.getName())
                .email(body.getEmail())
                .autoSubscribe(false)
                .build();
    }

    public static UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .isAutoSubscribe(user.isAutoSubscribe())
                .build();
    }

    public static List<UserDto> toDto(List<User> fiends) {
        return fiends.stream().map(UserMapper::toDto).collect(Collectors.toList());
    }

    public static UserShortDto toShotDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
