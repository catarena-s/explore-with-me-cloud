package dev.shvetsova.ewmc.main.service.subs;

import dev.shvetsova.ewmc.main.dto.event.EventShortDto;
import dev.shvetsova.ewmc.main.dto.user.UserDto;

import java.util.List;

public interface FriendService {
    List<EventShortDto> getParticipateEvents(long followerId, int from, int size);

    List<UserDto> getFriends(long followerId);

    List<EventShortDto> getFriendEvents(long followerId, int from, int size);

    List<UserDto> getFollowers(long userId);
}
