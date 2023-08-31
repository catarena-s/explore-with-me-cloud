package dev.shvetsova.ewmc.main.service.subs;

import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import dev.shvetsova.ewmc.main.dto.subs.FriendshipDto;
import dev.shvetsova.ewmc.main.dto.subs.FriendshipShortDto;
import dev.shvetsova.ewmc.main.enums.FriendshipState;
import dev.shvetsova.ewmc.main.exception.ConflictException;
import dev.shvetsova.ewmc.main.exception.NotFoundException;
import dev.shvetsova.ewmc.main.mapper.FriendshipMapper;
import dev.shvetsova.ewmc.main.model.Friendship;
import dev.shvetsova.ewmc.main.model.QUser;
import dev.shvetsova.ewmc.main.model.User;
import dev.shvetsova.ewmc.main.repository.FriendshipRepository;
import dev.shvetsova.ewmc.main.service.user.UserService;
import dev.shvetsova.ewmc.main.utils.QPredicate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static dev.shvetsova.ewmc.main.enums.FriendshipState.APPROVED;
import static dev.shvetsova.ewmc.main.enums.FriendshipState.PENDING;
import static dev.shvetsova.ewmc.main.enums.FriendshipState.REJECTED;
import static dev.shvetsova.ewmc.main.model.QFriendship.friendship;

@Service
@RequiredArgsConstructor
public class FriendshipServiceImpl implements FriendshipService {
    private final FriendshipRepository friendshipRepository;

    private final UserService userService;

    /**
     * Создать запрос на дружбу
     * @param followerId Id пользователя подавшего заявку на дружбу
     * @param userId Id пользователя добавляемого в друзья
     * @return
     */
    @Override
    @Transactional
    public FriendshipDto requestFriendship(long followerId, long userId) {
        if (followerId == userId) {
            throw new ConflictException("You can't follow yourself.");
        }
        final User follower = userService.findUserById(followerId);
        final User friend = userService.findUserById(userId);
        throwWhenFriendshipExist(followerId, userId);

        final Friendship friendshipRequest = Friendship.builder()
                .follower(follower)
                .friend(friend)
                .state(friend.isAutoSubscribe() ? APPROVED : PENDING)
                .createdOn(LocalDateTime.now())
                .build();
        final Friendship friendship = friendshipRepository.save(friendshipRequest);
        return FriendshipMapper.toDto(friendship);
    }

    /**
     * Подтверждение дружбы
     * @param userId Id пользователя, получившего запрос на дружбу
     * @param ids набор идентификаторов запросов на дружбу
     * @return пользователь со списком принятых запросов
     */
    @Override
    @Transactional
    public List<FriendshipShortDto> approveFriendship(long userId, List<Long> ids) {
        final List<Friendship> subList = friendshipRepository.findAllById(ids);

        throwWhenFriendshipListEmpty(subList);
        confirmUser(userId, subList);
        confirmFriendshipRequest(subList, Set.of(PENDING));

        subList.forEach(FriendshipServiceImpl::approveFriendship);
        final List<Friendship> saved = friendshipRepository.saveAll(subList);
        return saved.stream().map(FriendshipMapper::toShortDto).collect(Collectors.toList());
    }

    /** Отклонение запроса на дружбу */
    @Override
    @Transactional
    public List<FriendshipShortDto> rejectFriendship(long userId, List<Long> ids) {
        final List<Friendship> subList = friendshipRepository.findAllById(ids);

        throwWhenFriendshipListEmpty(subList);
        confirmUser(userId, subList);
        confirmFriendshipRequest(subList, Set.of(PENDING, APPROVED));

        subList.forEach(FriendshipServiceImpl::rejectFriendship);
        final List<Friendship> saved = friendshipRepository.saveAll(subList);
        return saved.stream().map(FriendshipMapper::toShortDto).collect(Collectors.toList());
    }

    /** Получить фолловером списка запросов на дружбу */
    @Override
    @Transactional(readOnly = true)
    public List<FriendshipShortDto> getFriendshipRequests(long userId, String filter) {
        userService.checkExistById(userId);
        throwWhenWrongFilter(filter, Set.of("ALL", PENDING.name(), APPROVED.name(), REJECTED.name()));
        return getRequestByFilter(filter, friendship.follower, userId);
    }

    /** Получить пользователем списка поданных запросов на дружбу пользователем */
    @Override
    @Transactional(readOnly = true)
    public List<FriendshipShortDto> getIncomingFriendRequests(long userId, String filter) {
        userService.checkExistById(userId);
        throwWhenWrongFilter(filter, Set.of("ALL", PENDING.name(), APPROVED.name(), REJECTED.name()));
        return getRequestByFilter(filter, friendship.friend, userId);
    }

    /** Отмена запроса на дружбу */
    @Override
    @Transactional
    public void deleteFriendshipRequest(long followerId, long subsId) {
        userService.checkExistById(followerId);
        if (!friendshipRepository.existsByIdAndFollowerId(subsId, followerId)) {
            throw new NotFoundException("Friendship request no exist.");
        }

        friendshipRepository.deleteByFollowerIdAndId(followerId, subsId);
    }

    private List<FriendshipShortDto> getRequestByFilter(String filter, QUser user, long userId) {
        final List<Predicate> eq = ("ALL".equalsIgnoreCase(filter))
                ? List.of(user.id.eq(userId))
                : List.of(user.id.eq(userId), friendship.state.eq(FriendshipState.from(filter)));

        final Predicate predicate = QPredicate.buildAnd(eq);
        final List<Friendship> subs = (List<Friendship>) friendshipRepository.findAll(predicate);

        return subs.stream().map(FriendshipMapper::toShortDto).collect(Collectors.toList());
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
        final boolean allMatchUser = subs.stream().allMatch(s -> s.getFriend().getId() == userId);
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
