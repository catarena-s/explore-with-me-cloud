package dev.shvetsova.ewmc.subscription.service.subs;

import dev.shvetsova.ewmc.dto.event.EventShortDto;
import dev.shvetsova.ewmc.dto.mq.EventInfoMq;
import dev.shvetsova.ewmc.dto.notification.NewNotificationDto;
import dev.shvetsova.ewmc.dto.user.UserDto;
import dev.shvetsova.ewmc.enums.SenderType;
import dev.shvetsova.ewmc.subscription.http.EventClient;
import dev.shvetsova.ewmc.subscription.http.RequestClient;
import dev.shvetsova.ewmc.subscription.http.UserClient;
import dev.shvetsova.ewmc.subscription.model.Friendship;
import dev.shvetsova.ewmc.subscription.mq.Supplier;
import dev.shvetsova.ewmc.subscription.repo.FriendshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static dev.shvetsova.ewmc.utils.UsersUtil.checkExistUser;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FriendServiceImpl implements FriendService {

    private final UserClient userClient;
    private final EventClient eventClient;
    private final RequestClient requestClient;
    private final FriendshipRepository friendshipRepository;

    private final Supplier supplier;

    /**
     * Получение списка друзей
     */
    @Override
    public List<UserDto> getFriends(long userId) {
        checkExistUser(userClient, userId);
        return userClient.getUserList(userId, friendshipRepository.findAllFriends(userId));
    }

    @Override
    public List<UserDto> getFollowers(long userId) {
        checkExistUser(userClient, userId);
        return userClient.getUserList(userId, friendshipRepository.findAllFollowers(userId));
    }

    /**
     * Получить список событий в которых примут участие друзья
     */
    @Override
    public List<EventShortDto> getParticipateEvents(long followerId, int from, int size) {
        checkExistUser(userClient, followerId);
        List<Long> allFriends = friendshipRepository.findAllFriends(followerId);
        List<Long> eventListIds = requestClient.getParticipateEventList(followerId, allFriends);
        return eventClient.getEventListByIds(eventListIds);
    }

    @Override
    public List<EventShortDto> getFriendEvents(long followerId, int from, int size) {
        checkExistUser(userClient, followerId);
        List<Long> allFriends = friendshipRepository.findAllFriends(followerId);
        return eventClient.getEventList(followerId, allFriends);
    }

    @Override
    public void sendNotificationToFollowers(EventInfoMq eventInfoMq) {
        List<Friendship> allByFriendId = friendshipRepository.findAllByFriendId(eventInfoMq.userId());
        Long eventId = eventInfoMq.eventId();
        allByFriendId.forEach(f -> supplier.sendNewMessage(NewNotificationDto.builder()
                .consumerId(f.getFollowerId())
                .senderId(eventId)
                .messageType(SenderType.EVENT)
                .text("New event from yor friend")
                .build())
        );
    }
}
