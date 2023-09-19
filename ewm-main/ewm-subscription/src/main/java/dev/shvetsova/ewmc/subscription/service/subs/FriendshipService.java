package dev.shvetsova.ewmc.subscription.service.subs;


import dev.shvetsova.ewmc.dto.mq.FriendshipRequestMq;
import dev.shvetsova.ewmc.dto.subs.FriendshipDto;
import dev.shvetsova.ewmc.dto.subs.FriendshipShortDto;

import java.util.List;

public interface FriendshipService {

    FriendshipDto requestFriendship(String followerId, String userId);

    List<FriendshipShortDto> approveFriendship(String userId, List<Long> ids);
    void approveFriendship(FriendshipRequestMq requestMq);

    List<FriendshipShortDto> rejectFriendship(String userId, List<Long> ids);

    void deleteFriendshipRequest(String followerId, Long subsId);

    List<FriendshipShortDto> getFriendshipRequests(String followerId, String filter);

    List<FriendshipShortDto> getIncomingFriendRequests(String userId, String filter);
}
