package dev.shvetsova.ewmc.main.filter;

import com.querydsl.core.types.Predicate;
import dev.shvetsova.ewmc.main.utils.QPredicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

//import java.util.function.Predicate;

import static dev.shvetsova.ewmc.main.model.QEvent.event;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventPredicate {
    public static Predicate getAvailable() {
        return event.participantLimit.eq(0)
                .or(event.confirmedRequests.lt(event.participantLimit));
    }

    public static Predicate getTextFilter(String text) {
        return event.description.likeIgnoreCase(text)
                .or(event.annotation.likeIgnoreCase(text));
    }

    public static Predicate getAndEventPredicate(EventFilter filter) {
        return QPredicate.builder()
                .add(filter.getId(), event.id::eq)
                .add(filter.getTitle(), event.title::likeIgnoreCase)
                .add(filter.getDescription(), event.description::likeIgnoreCase)
                .add(filter.getAnnotation(), event.annotation::likeIgnoreCase)
                .add(filter.getPaidEq(), event.paid::eq)
                .add(filter.getInitiatorId(), event.initiator.id::eq)
                .add(filter.getInitiatorIn(), event.initiator.id::in)
                .add(filter.getCategoryId(), event.category.id::eq)
                .add(filter.getCategoryIn(), event.category.id::in)
                .add(filter.getEventDateAfter(), event.eventDate::after)
                .add(filter.getEventDateBefore(), event.eventDate::before)
                .add(filter.getCreatedOnAfter(), event.createdOn::after)
                .add(filter.getCreatedOnBefore(), event.createdOn::before)
                .add(filter.getPublishedOnAfter(), event.publishedOn::after)
                .add(filter.getPublishedOnBefore(), event.publishedOn::before)
                .add(filter.getParticipantLimitEq(), event.participantLimit::eq)
                .add(filter.getRequestModeration(), event.requestModeration::eq)
                .add(filter.getConfirmedRequestsLessThan(), event.confirmedRequests::lt)
                .add(filter.getStatesIn(), event.state::in)
                .add(filter.getStateEq(), event.state::eq)
                .buildAnd();
    }
}
