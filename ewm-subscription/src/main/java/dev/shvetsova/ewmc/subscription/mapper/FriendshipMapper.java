package dev.shvetsova.ewmc.subscription.mapper;

import dev.shvetsova.ewmc.subscription.dto.subs.FriendshipDto;
import dev.shvetsova.ewmc.subscription.dto.subs.FriendshipShortDto;
import dev.shvetsova.ewmc.subscription.model.Friendship;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FriendshipMapper {
    public static FriendshipDto toDto(Friendship friendship) {
        return FriendshipDto.builder()
                .id(friendship.getId())
                .followerId(friendship.getFollowerId())
                .friend(UserMapper.toShotDto(friendship.getFriendId()))
                .state(friendship.getState().name())
                .build();
    }

    public static FriendshipShortDto toShortDto(Friendship friendship) {
        return FriendshipShortDto.builder()
                .id(friendship.getId())
                .friend(UserMapper.toShotDto(friendship.getFriendId()))
                .state(friendship.getState().name())
                .build();
    }

//    public static List<FriendshipShortDto> toShortDto(List<Friendship> subs) {
//        return subs.stream()
//                .map(FriendshipMapper::toShortDto)
//                .collect(Collectors.toList());
//    }
}
