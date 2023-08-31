package dev.shvetsova.ewmc.main.service.subs;

import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import dev.shvetsova.ewmc.main.dto.event.EventShortDto;
import dev.shvetsova.ewmc.main.dto.user.UserDto;
import dev.shvetsova.ewmc.main.enums.EventState;
import dev.shvetsova.ewmc.main.enums.RequestStatus;
import dev.shvetsova.ewmc.main.mapper.EventMapper;
import dev.shvetsova.ewmc.main.mapper.UserMapper;
import dev.shvetsova.ewmc.main.model.Event;
import dev.shvetsova.ewmc.main.model.QUser;
import dev.shvetsova.ewmc.main.model.User;
import dev.shvetsova.ewmc.main.service.user.UserService;


import java.time.LocalDateTime;
import java.util.List;

import static dev.shvetsova.ewmc.main.enums.FriendshipState.APPROVED;
import static dev.shvetsova.ewmc.main.model.QEvent.event;
import static dev.shvetsova.ewmc.main.model.QFriendship.friendship;
import static dev.shvetsova.ewmc.main.model.QRequest.request;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {
    private final UserService userService;
//    private final JPAQueryFactory queryFactory;
    @PersistenceContext
    private EntityManager em;

    /** Получение списка друзей */
    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getFriends(long userId) {
        userService.checkExistById(userId);
        final List<User> fiends = getUserList(friendship.friend, friendship.follower, userId);
        return UserMapper.toDto(fiends);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getFollowers(long userId) {
        userService.checkExistById(userId);
        final List<User> fiends = getUserList(friendship.follower, friendship.friend, userId);
        return UserMapper.toDto(fiends);
    }

    /** Получить список событий в которых примут участие друзья */
    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getParticipateEvents(long followerId, int from, int size) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, em);
        userService.checkExistById(followerId);
        final List<Event> events =
                queryFactory
                        .selectDistinct(request.event)
                        .from(request)
                        .innerJoin(friendship).on(friendship.friend.eq(request.requester))
                        .where(friendship.follower.id.eq(followerId)
                                .and(request.isPrivate.isFalse())
                                .and(request.status.eq(RequestStatus.CONFIRMED))
                                .and(request.event.eventDate.after(LocalDateTime.now().plusHours(2))))
                        .offset(from)
                        .limit(size)
                        .fetch();


        return EventMapper.toDto(events);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getFriendEvents(long followerId, int from, int size) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, em);
        userService.checkExistById(followerId);
        final List<Event> events =
                queryFactory
                        .selectDistinct(event)
                        .from(event)
                        .innerJoin(friendship).on(friendship.friend.eq(event.initiator))
                        .where(friendship.follower.id.eq(followerId)
                                .and(event.state.eq(EventState.PUBLISHED))
                                .and(event.eventDate.after(LocalDateTime.now().plusHours(2))))
                        .offset(from)
                        .limit(size)
                        .fetch();

        return EventMapper.toDto(events);
    }

    private List<User> getUserList(QUser friend, QUser user, long userId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, em);
        return queryFactory
                .select(friend)
                .from(friendship)
                .where(user.id.eq(userId).and(friendship.state.eq(APPROVED)))
                .fetch();
    }
}
