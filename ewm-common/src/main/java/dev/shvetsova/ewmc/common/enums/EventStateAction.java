package dev.shvetsova.ewmc.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

//import static ru.practicum.mapper.EnumMapper.getEnumFromString;

@Getter
public enum EventStateAction {
    SEND_TO_REVIEW(EventState.PENDING),
    CANCEL_REVIEW(EventState.CANCELED),
    PUBLISH_EVENT(EventState.PUBLISHED),
    REJECT_EVENT(EventState.CANCELED);

    private final EventState eventState;

    EventStateAction(EventState eventState) {
        this.eventState = eventState;
    }

    @JsonCreator
    public static EventStateAction from(String name) {
        return EnumMapper.getEnumFromString(EventStateAction.class, name, "Unknown event state action");
    }
}
