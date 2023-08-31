package dev.shvetsova.ewmc.main.service.subs;

import dev.shvetsova.ewmc.main.dto.subs.FriendshipShortDto;
import dev.shvetsova.ewmc.main.dto.subs.FriendshipDto;

import java.util.List;

public interface FriendshipService {

    FriendshipDto requestFriendship(long followerId, long userId);

    List<FriendshipShortDto> approveFriendship(long userId, List<Long> ids);

    List<FriendshipShortDto> rejectFriendship(long userId, List<Long> ids);

    void deleteFriendshipRequest(long followerId, long subsId);

    List<FriendshipShortDto> getFriendshipRequests(long followerId, String filter);

    List<FriendshipShortDto> getIncomingFriendRequests(long userId, String filter);
}
