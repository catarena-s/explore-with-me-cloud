package dev.shvetsova.ewmc.event.mapper;

import dev.shvetsova.ewmc.dto.event.EventFullDto;
import dev.shvetsova.ewmc.dto.event.EventShortDto;
import dev.shvetsova.ewmc.dto.event.NewEventDto;
import dev.shvetsova.ewmc.event.enums.EventState;
import dev.shvetsova.ewmc.event.model.Category;
import dev.shvetsova.ewmc.event.model.Event;
import dev.shvetsova.ewmc.event.model.Location;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {

    public static Event fromDto(NewEventDto body,
                                Long userId,
                                Category category,
                                Location location,
                                EventState state,
                                LocalDateTime createdOn) {
        return Event.builder()
                .eventDate(body.getEventDate())
                .description(body.getDescription())
                .annotation(body.getAnnotation())
                .title(body.getTitle())
                .paid(body.isPaid())
                .participantLimit(body.getParticipantLimit())
                .createdOn(createdOn)
                .requestModeration(body.isRequestModeration())
                .category(category)
                .initiatorId(userId)
                .location(location)
                .confirmedRequests(0)
                .state(state)
                .build();
    }

    public static EventFullDto toFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .initiator(UserMapper.toShotDto(event.getInitiatorId()))
                .category(CategoryMapper.toDto(event.getCategory()))
                .location(LocationMapper.toDto(event.getLocation()))
                .paid(event.getPaid())
                .eventDate(event.getEventDate())
                .createdOn(event.getCreatedOn())
                .publishedOn(event.getPublishedOn())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .confirmedRequests(event.getConfirmedRequests())
                .state(event.getState().name())
                .views(0L)
                .build();
    }

    public static EventShortDto toShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .initiator(UserMapper.toShotDto(event.getInitiatorId()))
                .category(CategoryMapper.toDto(event.getCategory()))
                .paid(event.getPaid())
                .eventDate(event.getEventDate())
                .build();
    }

    public static EventFullDto toFullDto(Event event, long hits) {
        return toFullDto(event).toBuilder()
                .views(hits)
                .build();
    }

    public static EventShortDto toShortDto(Event event, long hits) {
        return toShortDto(event).toBuilder()
                .views(hits)
                .build();
    }

    public static List<EventShortDto> toDto(List<Event> subs) {
        return subs.stream()
                .map(EventMapper::toShortDto)
                .collect(Collectors.toList());
    }
}
