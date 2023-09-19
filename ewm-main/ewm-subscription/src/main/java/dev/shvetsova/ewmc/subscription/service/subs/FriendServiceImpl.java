package dev.shvetsova.ewmc.subscription.service.subs;

import dev.shvetsova.ewmc.dto.event.EventShortDto;
import dev.shvetsova.ewmc.dto.mq.EventInfoMq;
import dev.shvetsova.ewmc.dto.notification.NewNotificationDto;
import dev.shvetsova.ewmc.dto.user.UserDto;
import dev.shvetsova.ewmc.enums.MessageType;
import dev.shvetsova.ewmc.subscription.http.EventClient;
import dev.shvetsova.ewmc.subscription.http.RequestClient;
import dev.shvetsova.ewmc.subscription.http.UserClient;
import dev.shvetsova.ewmc.subscription.model.Friendship;
import dev.shvetsova.ewmc.subscription.mq.NotificationSupplier;
import dev.shvetsova.ewmc.subscription.repo.FriendshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {
    private final UserClient userClient;
    private final EventClient eventClient;
    private final RequestClient requestClient;
    private final FriendshipRepository friendshipRepository;

    private final NotificationSupplier notificationSupplier;

    /**
     * Получение списка друзей
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getFriends(String userId) {
        return userClient.getUserList(userId, friendshipRepository.findAllFriends(userId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getFollowers(String userId) {
        return userClient.getUserList(userId, friendshipRepository.findAllFollowers(userId));
    }

    public void sendNotificationToFriends(EventInfoMq eventInfoMq) {
        List<Friendship> allByFriendId = friendshipRepository.findAllByFriendId(eventInfoMq.userId());
        Long eventId = eventInfoMq.eventId();
        allByFriendId.forEach(f -> {
            notificationSupplier.sendNewMessage(NewNotificationDto.builder()
                    .text("New event from yor friend")
                    .userId(f.getFollowerId())
                    .senderId(String.valueOf(eventId))
                    .messageType(MessageType.EVENT)
                    .build());
        });
    }

    /**
     * Получить список событий в которых примут участие друзья
     */
    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getParticipateEvents(String followerId, int from, int size) {
        List<Long> allFriends = friendshipRepository.findAllFriends(followerId);
        List<Long> eventListIds = requestClient.getParticipateEventList(followerId, allFriends);
        return eventClient.getEventListByIds(eventListIds);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getFriendEvents(String followerId, int from, int size) {
        List<Long> allFriends = friendshipRepository.findAllFriends(followerId);
        return eventClient.getEventList(followerId, allFriends);
    }
}
