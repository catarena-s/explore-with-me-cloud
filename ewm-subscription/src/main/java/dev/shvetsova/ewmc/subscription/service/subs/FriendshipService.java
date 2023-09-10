package dev.shvetsova.ewmc.subscription.service.subs;


import dev.shvetsova.ewmc.subscription.dto.subs.FriendshipDto;
import dev.shvetsova.ewmc.subscription.dto.subs.FriendshipShortDto;

import java.util.List;

public interface FriendshipService {

    FriendshipDto requestFriendship(long followerId, long userId);

    List<FriendshipShortDto> approveFriendship(long userId, List<Long> ids);

    List<FriendshipShortDto> rejectFriendship(long userId, List<Long> ids);

    void deleteFriendshipRequest(long followerId, long subsId);

    List<FriendshipShortDto> getFriendshipRequests(long followerId, String filter);

    List<FriendshipShortDto> getIncomingFriendRequests(long userId, String filter);
}
