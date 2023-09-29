package dev.shvetsova.ewmc.subscription.service.subs;

import dev.shvetsova.ewmc.dto.mq.FriendshipRequestMq;
import dev.shvetsova.ewmc.dto.notification.NewNotificationDto;
import dev.shvetsova.ewmc.dto.subs.FriendshipDto;
import dev.shvetsova.ewmc.dto.subs.FriendshipShortDto;
import dev.shvetsova.ewmc.enums.MessageType;
import dev.shvetsova.ewmc.exception.ConflictException;
import dev.shvetsova.ewmc.exception.NotFoundException;
import dev.shvetsova.ewmc.subscription.mapper.FriendshipMapper;
import dev.shvetsova.ewmc.subscription.model.Friendship;
import dev.shvetsova.ewmc.subscription.model.FriendshipState;
import dev.shvetsova.ewmc.subscription.mq.FriendshipRequestSupplier;
import dev.shvetsova.ewmc.subscription.mq.NotificationSupplier;
import dev.shvetsova.ewmc.subscription.repo.FriendshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static dev.shvetsova.ewmc.enums.MessageType.FRIENDSHIP_APPROVED;
import static dev.shvetsova.ewmc.enums.MessageType.FRIENDSHIP_REJECTED;
import static dev.shvetsova.ewmc.subscription.model.FriendshipState.*;


@Service
@RequiredArgsConstructor
public class FriendshipServiceImpl implements FriendshipService {
    private final FriendshipRepository friendshipRepository;

    private final NotificationSupplier notificationSupplier;
    private final FriendshipRequestSupplier friendshipRequestSupplier;

    /**
     * Создать запрос на дружбу
     *
     * @param followerId Id пользователя подавшего заявку на дружбу
     * @param friendId   Id пользователя добавляемого в друзья
     */
    @Override
    @Transactional
    public FriendshipDto requestFriendship(String followerId, String friendId) {
        if (followerId.equals(friendId)) {
            throw new ConflictException("You can't follow yourself.");
        }
        throwWhenFriendshipExist(followerId, friendId);

        final Friendship friendshipRequest = Friendship.builder()
                .followerId(followerId)
                .userId(friendId)
                .state(PENDING)
                .createdOn(LocalDateTime.now())
                .build();
        final Friendship friendship = friendshipRepository.save(friendshipRequest);

        friendshipRequestSupplier.sendMessageToQueue(new FriendshipRequestMq(friendId, followerId));
        return FriendshipMapper.toDto(friendship);
    }

    /**
     * Подтверждение дружбы
     *
     * @param userId Id пользователя, получившего запрос на дружбу
     * @param ids    набор идентификаторов запросов на дружбу
     * @return пользователь со списком принятых запросов
     */
    @Override
    @Transactional
    public List<FriendshipShortDto> approveFriendship(String userId, List<Long> ids) {
        final List<Friendship> subList = friendshipRepository.findAllById(ids);

        throwWhenFriendshipListEmpty(subList);
        confirmUser(userId, subList);
        confirmFriendshipRequest(subList, Set.of(PENDING));

        subList.forEach(FriendshipServiceImpl::approveFriendship);
        final List<Friendship> saved = friendshipRepository.saveAll(subList);
        for (Long id : ids) {
            sendNotification(String.valueOf(id), userId, FRIENDSHIP_APPROVED, "Friendship was APPROVED.");
        }

        return saved.stream().map(FriendshipMapper::toShortDto).toList();
    }

    @Override
    @Transactional
    public void approveFriendship(FriendshipRequestMq requestMq) {
        String followerId = requestMq.followerId();
        String userId = requestMq.friendId();

        Friendship friendship = friendshipRepository.findAllByUserIdAndFollowerId(userId, followerId)
                .orElseThrow(() -> new NotFoundException("Friendship request no exist."));
        approveFriendship(friendship);
        friendshipRepository.save(friendship);
    }

    /**
     * Отклонение запроса на дружбу
     */
    @Override
    @Transactional
    public List<FriendshipShortDto> rejectFriendship(String userId, List<Long> ids) {
        final List<Friendship> subList = friendshipRepository.findAllById(ids);

        throwWhenFriendshipListEmpty(subList);
        confirmUser(userId, subList);
        confirmFriendshipRequest(subList, Set.of(PENDING, APPROVED));

        subList.forEach(FriendshipServiceImpl::rejectFriendship);
        final List<Friendship> saved = friendshipRepository.saveAll(subList);
        for (Long id : ids) {
            sendNotification(String.valueOf(id), userId, FRIENDSHIP_REJECTED, "Friendship was REJECTED.");
        }
        return saved.stream().map(FriendshipMapper::toShortDto).toList();
    }

    private void sendNotification(String userId, String senderId, MessageType messageType, String text) {
        notificationSupplier.sendMessageToQueue(NewNotificationDto.builder()
                .userId(userId)
                .senderId(senderId)
                .messageType(messageType)
                .text(text)
                .build());
    }

    /**
     * Получить фолловером списка запросов на дружбу
     */
    @Override
    @Transactional(readOnly = true)
    public List<FriendshipShortDto> getFriendshipRequests(String userId, String filter) {
        throwWhenWrongFilter(filter, Set.of("ALL", PENDING.name(), APPROVED.name(), REJECTED.name()));
        List<Friendship> f = ("ALL".equalsIgnoreCase(filter))
                ? friendshipRepository.findAllByFollowerId(userId)
                : friendshipRepository.findAllByFollowerIdAndState(userId, from(filter));
        return f.stream().map(FriendshipMapper::toShortDto).toList();
    }

    /**
     * Получить пользователем списка поданных запросов на дружбу пользователем
     */
    @Override
    @Transactional(readOnly = true)
    public List<FriendshipShortDto> getIncomingFriendRequests(String userId, String filter) {
        throwWhenWrongFilter(filter, Set.of("ALL", PENDING.name(), APPROVED.name(),
                REJECTED.name()));

        List<Friendship> f = ("ALL".equalsIgnoreCase(filter))
                ? friendshipRepository.findAllByUserId(userId)
                : friendshipRepository.findAllByUserIdAndState(userId, from(filter));
        return f.stream().map(FriendshipMapper::toShortDto).toList();
    }

    /**
     * Отмена запроса на дружбу
     */
    @Override
    @Transactional
    public void deleteFriendshipRequest(String followerId, Long subsId) {
        if (!friendshipRepository.existsByIdAndFollowerId(subsId, followerId)) {
            throw new NotFoundException("Friendship request no exist.");
        }

        friendshipRepository.deleteByFollowerIdAndId(followerId, subsId);
    }

    private void throwWhenFriendshipListEmpty(List<Friendship> subList) {
        if (subList.isEmpty()) {
            throw new NotFoundException("Request friendship not found.");
        }
    }

    private void throwWhenWrongFilter(String filter, Set<String> set) {
        if (!set.contains(filter)) {
            throw new ConflictException(String.format("Wrong filter. Filter should be one of: %s",
                    set.stream().sorted().toList()));
        }
    }

    private void confirmFriendshipRequest(List<Friendship> subs, Set<FriendshipState> set) {
        final boolean isNotAllStateCorrect = subs.stream().noneMatch(s -> set.contains(s.getState()));
        if (isNotAllStateCorrect) {
            throw new ConflictException("Status should be one of: " + set);
        }
    }

    private void throwWhenFriendshipExist(String userId, String friendId) {
        if (friendshipRepository.existsByFollowerIdAndUserIdAndStateNot(userId, friendId, REJECTED)) {
            throw new ConflictException("Already friends.");
        }
    }

    private void confirmUser(String userId, List<Friendship> subs) {
        final boolean allMatchUser = subs.stream().allMatch(s -> s.getUserId().equals(userId));
        if (!allMatchUser) {
            throw new ConflictException("You can change only your requests.");
        }
    }

    private static void approveFriendship(Friendship f) {
        f.setState(APPROVED);
    }

    private static void rejectFriendship(Friendship f) {
        f.setState(REJECTED);
    }
}
