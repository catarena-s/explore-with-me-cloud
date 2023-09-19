package dev.shvetsova.ewmc.event.utils.filter;

import com.querydsl.core.types.Predicate;
import dev.shvetsova.ewmc.event.utils.QPredicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import dev.shvetsova.ewmc.event.model.QEvent;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventPredicate {
    public static Predicate getAvailable() {
        return QEvent.event.participantLimit.eq(0)
                .or(QEvent.event.confirmedRequests.lt(QEvent.event.participantLimit));
    }

    public static Predicate getTextFilter(String text) {
        return QEvent.event.description.likeIgnoreCase(text)
                .or(QEvent.event.annotation.likeIgnoreCase(text));
    }

    public static Predicate getAndEventPredicate(EventFilter filter) {
        return QPredicate.builder()
                .add(filter.getId(), QEvent.event.id::eq)
                .add(filter.getTitle(), QEvent.event.title::likeIgnoreCase)
                .add(filter.getDescription(), QEvent.event.description::likeIgnoreCase)
                .add(filter.getAnnotation(), QEvent.event.annotation::likeIgnoreCase)
                .add(filter.getPaidEq(), QEvent.event.paid::eq)
//                .add(filter.getInitiatorId(), QEvent.event.initiatorId::eq)
//                .add(filter.getInitiatorIn(), QEvent.event.initiatorId::in)
                .add(filter.getCategoryId(), QEvent.event.category.id::eq)
                .add(filter.getCategoryIn(), QEvent.event.category.id::in)
                .add(filter.getEventDateAfter(), QEvent.event.eventDate::after)
                .add(filter.getEventDateBefore(), QEvent.event.eventDate::before)
                .add(filter.getCreatedOnAfter(), QEvent.event.createdOn::after)
                .add(filter.getCreatedOnBefore(), QEvent.event.createdOn::before)
                .add(filter.getPublishedOnAfter(), QEvent.event.publishedOn::after)
                .add(filter.getPublishedOnBefore(), QEvent.event.publishedOn::before)
                .add(filter.getParticipantLimitEq(), QEvent.event.participantLimit::eq)
                .add(filter.getRequestModeration(), QEvent.event.requestModeration::eq)
                .add(filter.getConfirmedRequestsLessThan(), QEvent.event.confirmedRequests::lt)
                .add(filter.getStatesIn(), QEvent.event.state::in)
                .add(filter.getStateEq(), QEvent.event.state::eq)
                .buildAnd();
    }
}
