package dev.shvetsova.ewmc.subscription.service.subs;

import dev.shvetsova.ewmc.dto.mq.EventInfoMq;
import dev.shvetsova.ewmc.dto.event.EventShortDto;
import dev.shvetsova.ewmc.dto.user.UserDto;

import java.util.List;

public interface FriendService {
    List<EventShortDto> getParticipateEvents(String followerId, int from, int size);

    List<UserDto> getFriends(String followerId);

    List<EventShortDto> getFriendEvents(String followerId, int from, int size);

    List<UserDto> getFollowers(String userId);

    void sendNotificationToFriends(EventInfoMq eventInfoMq);
}
