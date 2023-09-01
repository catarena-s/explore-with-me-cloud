package dev.shvetsova.ewmc.subscription.mapper;

import dev.shvetsova.ewmc.subscription.dto.user.UserShortDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {


//    public static UserDto toDto(User user) {
//        return UserDto.builder()
//                .id(user.getId())
//                .name(user.getName())
//                .email(user.getEmail())
//                .isAutoSubscribe(user.isAutoSubscribe())
//                .build();
//    }

//    public static List<UserDto> toDto(List<User> fiends) {
//        return fiends.stream().map(UserMapper::toDto).collect(Collectors.toList());
//    }

    public static UserShortDto toShotDto(long userId) {
        return UserShortDto.builder()
                .id(userId)
                .build();
    }
}
