package dev.shvetsova.ewmc.subscription.service.subs;

import dev.shvetsova.ewmc.subscription.dto.subs.FriendshipDto;
import dev.shvetsova.ewmc.subscription.dto.subs.FriendshipShortDto;
import dev.shvetsova.ewmc.subscription.dto.user.UserDto;
import dev.shvetsova.ewmc.subscription.exception.ConflictException;
import dev.shvetsova.ewmc.subscription.exception.NotFoundException;
import dev.shvetsova.ewmc.subscription.http.UserClient;
import dev.shvetsova.ewmc.subscription.mapper.FriendshipMapper;
import dev.shvetsova.ewmc.subscription.model.Friendship;
import dev.shvetsova.ewmc.subscription.model.FriendshipState;
import dev.shvetsova.ewmc.subscription.repo.FriendshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static dev.shvetsova.ewmc.subscription.model.FriendshipState.REJECTED;


@Service
@RequiredArgsConstructor
public class FriendshipServiceImpl implements FriendshipService {
    private final FriendshipRepository friendshipRepository;

    private final UserClient userClient;

    /**
     * Создать запрос на дружбу
     *
     * @param followerId Id пользователя подавшего заявку на дружбу
     * @param userId     Id пользователя добавляемого в друзья
     * @return
     */
    @Override
    @Transactional
    public FriendshipDto requestFriendship(long followerId, long userId) {
        if (followerId == userId) {
            throw new ConflictException("You can't follow yourself.");
        }
//        final UserDto follower = userClient.checkExistById(followerId);
        final UserDto friend = userClient.findUserById(userId);
        userClient.checkExistById(followerId);
//        userClient.checkExistById(userId);


        throwWhenFriendshipExist(followerId, userId);

        final Friendship friendshipRequest = Friendship.builder()
                .followerId(followerId)
                .friendId(userId)
                .state(friend.getIsAutoSubscribe() ? FriendshipState.APPROVED : FriendshipState.PENDING)
                .createdOn(LocalDateTime.now())
                .build();
        final Friendship friendship = friendshipRepository.save(friendshipRequest);
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
    public List<FriendshipShortDto> approveFriendship(long userId, List<Long> ids) {
        final List<Friendship> subList = friendshipRepository.findAllById(ids);

        throwWhenFriendshipListEmpty(subList);
        confirmUser(userId, subList);
        confirmFriendshipRequest(subList, Set.of(FriendshipState.PENDING));

        subList.forEach(FriendshipServiceImpl::approveFriendship);
        final List<Friendship> saved = friendshipRepository.saveAll(subList);
        return saved.stream().map(FriendshipMapper::toShortDto).collect(Collectors.toList());
    }

    /**
     * Отклонение запроса на дружбу
     */
    @Override
    @Transactional
    public List<FriendshipShortDto> rejectFriendship(long userId, List<Long> ids) {
        final List<Friendship> subList = friendshipRepository.findAllById(ids);

        throwWhenFriendshipListEmpty(subList);
        confirmUser(userId, subList);
        confirmFriendshipRequest(subList, Set.of(FriendshipState.PENDING, FriendshipState.APPROVED));

        subList.forEach(FriendshipServiceImpl::rejectFriendship);
        final List<Friendship> saved = friendshipRepository.saveAll(subList);
        return saved.stream().map(FriendshipMapper::toShortDto).collect(Collectors.toList());
    }

    /**
     * Получить фолловером списка запросов на дружбу
     */
    @Override
    @Transactional(readOnly = true)
    public List<FriendshipShortDto> getFriendshipRequests(long userId, String filter) {
        userClient.checkExistById(userId);

        throwWhenWrongFilter(filter, Set.of("ALL", FriendshipState.PENDING.name(), FriendshipState.APPROVED.name(), FriendshipState.REJECTED.name()));
        List<Friendship> f = ("ALL".equalsIgnoreCase(filter))
                ? friendshipRepository.findAllByFollowerId(userId)
                : friendshipRepository.findAllByFollowerIdAndState(userId, FriendshipState.from(filter));
        return f.stream().map(FriendshipMapper::toShortDto).toList();
    }

    /**
     * Получить пользователем списка поданных запросов на дружбу пользователем
     */
    @Override
    @Transactional(readOnly = true)
    public List<FriendshipShortDto> getIncomingFriendRequests(long userId, String filter) {
        userClient.checkExistById(userId);
        throwWhenWrongFilter(filter, Set.of("ALL", FriendshipState.PENDING.name(), FriendshipState.APPROVED.name(),
                FriendshipState.REJECTED.name()));

        List<Friendship> f = ("ALL".equalsIgnoreCase(filter))
                ? friendshipRepository.findAllByFriendId(userId)
                : friendshipRepository.findAllByFriendIdAndState(userId, FriendshipState.from(filter));
        return f.stream().map(FriendshipMapper::toShortDto).toList();
    }

    /**
     * Отмена запроса на дружбу
     */
    @Override
    @Transactional
    public void deleteFriendshipRequest(long followerId, long subsId) {
        userClient.checkExistById(followerId);
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
                    set.stream().sorted().collect(Collectors.toList())));
        }
    }

    private void confirmFriendshipRequest(List<Friendship> subs, Set<FriendshipState> set) {
        final boolean isNotAllStateCorrect = subs.stream().noneMatch(s -> set.contains(s.getState()));
        if (isNotAllStateCorrect) {
            throw new ConflictException("Status should be one of: " + set);
        }
    }

    private void throwWhenFriendshipExist(long userId, long friendId) {
        if (friendshipRepository.existsByFollowerIdAndFriendIdAndStateNot(userId, friendId, REJECTED)) {
            throw new ConflictException("Already friends.");
        }
    }

    private void confirmUser(long userId, List<Friendship> subs) {
        final boolean allMatchUser = subs.stream().allMatch(s -> s.getFriendId() == userId);
        if (!allMatchUser) {
            throw new ConflictException("You can change only your requests.");
        }
    }

    private static void approveFriendship(Friendship f) {
        f.setState(FriendshipState.APPROVED);
    }

    private static void rejectFriendship(Friendship f) {
        f.setState(FriendshipState.REJECTED);
    }
}
