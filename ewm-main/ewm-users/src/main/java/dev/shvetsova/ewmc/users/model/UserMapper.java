package dev.shvetsova.ewmc.users.model;

import dev.shvetsova.ewmc.dto.user.NewUserRequest;
import dev.shvetsova.ewmc.dto.user.UserDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {
    public static UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getUid())
                .name(user.getName())
                .email(user.getEmail())
                .isAutoSubscribe(user.isAutoSubscribe())
                .build();
    }

    public static List<UserDto> toDto(List<User> fiends) {
        return fiends.stream().map(UserMapper::toDto).collect(Collectors.toList());
    }

    public static User fromDto(NewUserRequest body, String uid) {
        return User.builder()
                .name(body.getName())
                .email(body.getEmail())
                .autoSubscribe(false)
                .uid(uid)
                .build();
    }
}
