package dev.shvetsova.ewmc.main.mapper;

import dev.shvetsova.ewmc.main.dto.subs.FriendshipDto;
import dev.shvetsova.ewmc.main.dto.subs.FriendshipShortDto;
import dev.shvetsova.ewmc.main.model.Friendship;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FriendshipMapper {
    public static FriendshipDto toDto(Friendship friendship) {
        return FriendshipDto.builder()
                .id(friendship.getId())
                .followerId(friendship.getFollower().getId())
                .friend(UserMapper.toShotDto(friendship.getFriend()))
                .state(friendship.getState())
                .build();
    }

    public static FriendshipShortDto toShortDto(Friendship friendship) {
        return FriendshipShortDto.builder()
                .id(friendship.getId())
                .friend(UserMapper.toShotDto(friendship.getFriend()))
                .state(friendship.getState())
                .build();
    }

    public static List<FriendshipShortDto> toShortDto(List<Friendship> subs) {
        return subs.stream()
                .map(FriendshipMapper::toShortDto)
                .collect(Collectors.toList());
    }
}
