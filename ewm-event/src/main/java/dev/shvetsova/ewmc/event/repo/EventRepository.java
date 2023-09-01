package dev.shvetsova.ewmc.event.repo;

import dev.shvetsova.ewmc.event.model.enums.EventState;
import dev.shvetsova.ewmc.event.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    Optional<Event> findByIdAndInitiatorId(long eventId, long userId);

    Page<Event> findAllByInitiatorId(long userId, PageRequest page);

    boolean existsByCategoryId(long catId);

    Optional<Event> findByIdAndState(long id, EventState eventState);

    boolean existsByInitiatorId(long userId);

    List<Event> findAllByInitiatorIdInAndState(List<Long> friendsId, EventState published);

    List<Event> findAllByIdInAndState(List<Long> eventIds, EventState eventState);
}
