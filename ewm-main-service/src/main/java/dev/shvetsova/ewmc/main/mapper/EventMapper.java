package dev.shvetsova.ewmc.main.mapper;

import dev.shvetsova.ewmc.common.dto.UserMapper;
import dev.shvetsova.ewmc.common.dto.event.EventFullDto;
import dev.shvetsova.ewmc.common.dto.event.EventShortDto;
import dev.shvetsova.ewmc.common.dto.event.NewEventDto;
import dev.shvetsova.ewmc.common.dto.user.UserDto;
import dev.shvetsova.ewmc.main.model.Category;
import dev.shvetsova.ewmc.main.model.Event;
import dev.shvetsova.ewmc.main.model.Location;
import dev.shvetsova.ewmc.common.enums.EventState;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {

    public static Event fromDto(NewEventDto body,
                                UserDto user,
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
                .initiatorId(user.getId())
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
                .state(event.getState())
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
