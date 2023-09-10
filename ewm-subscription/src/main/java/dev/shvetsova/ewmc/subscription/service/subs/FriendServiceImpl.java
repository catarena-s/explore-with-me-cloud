package dev.shvetsova.ewmc.subscription.service.subs;

import dev.shvetsova.ewmc.subscription.dto.event.EventShortDto;
import dev.shvetsova.ewmc.subscription.dto.user.UserDto;
import dev.shvetsova.ewmc.subscription.http.EventClient;
import dev.shvetsova.ewmc.subscription.http.RequestClient;
import dev.shvetsova.ewmc.subscription.http.UserClient;
import dev.shvetsova.ewmc.subscription.repo.FriendshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static dev.shvetsova.ewmc.subscription.utils.UsersUtil.checkExistUser;


@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {
    private final UserClient userClient;
    private final EventClient eventClient;
    private final RequestClient requestClient;
    private final FriendshipRepository friendshipRepository;

    /**
     * Получение списка друзей
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getFriends(long userId) {
        checkExistUser(userClient, userId);
        return userClient.getUserList(userId, friendshipRepository.findAllFriends(userId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getFollowers(long userId) {
        checkExistUser(userClient, userId);
        return userClient.getUserList(userId, friendshipRepository.findAllFollowers(userId));
    }

    /**
     * Получить список событий в которых примут участие друзья
     */
    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getParticipateEvents(long followerId, int from, int size) {
        checkExistUser(userClient, followerId);
        List<Long> allFriends = friendshipRepository.findAllFriends(followerId);
        List<Long> eventListIds = requestClient.getParticipateEventList(followerId, allFriends);
        List<EventShortDto> eventList = eventClient.getEventListByIds(eventListIds);
        return eventList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getFriendEvents(long followerId, int from, int size) {
        checkExistUser(userClient, followerId);
        List<Long> allFriends = friendshipRepository.findAllFriends(followerId);
        List<EventShortDto> eventList = eventClient.getEventList(followerId, allFriends);
        return eventList;
    }
}
