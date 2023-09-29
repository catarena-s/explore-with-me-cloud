package dev.shvetsova.ewmc.subscription.mapper;

import dev.shvetsova.ewmc.dto.subs.FriendshipDto;
import dev.shvetsova.ewmc.dto.subs.FriendshipShortDto;
import dev.shvetsova.ewmc.subscription.model.Friendship;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FriendshipMapper {
    public static FriendshipDto toDto(Friendship friendship) {
        return FriendshipDto.builder()
                .id(friendship.getId())
                .followerId(friendship.getFollowerId())
                .friend(UserMapper.toShotDto(friendship.getUserId()))
                .state(friendship.getState().name())
                .build();
    }

    public static FriendshipShortDto toShortDto(Friendship friendship) {
        return FriendshipShortDto.builder()
                .id(friendship.getId())
                .friend(UserMapper.toShotDto(friendship.getUserId()))
                .state(friendship.getState().name())
                .build();
    }
}
