package dev.shvetsova.ewmc.event.utils.filter;

import dev.shvetsova.ewmc.event.enums.EventState;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder(toBuilder = true)
public class EventFilter {
    private Long id;
    private String title;
    private String description;
    private String annotation;

    private Long categoryId;
    private List<Long> categoryIn;

    private Long confirmedRequestEq;
    private Long confirmedRequestsLessThan;
    private Long confirmedRequestsMore;

    private LocalDateTime createdOnEq;
    private LocalDateTime createdOnAfter;
    private LocalDateTime createdOnBefore;
    private LocalDateTime eventDateEq;
    private LocalDateTime eventDateAfter;
    private LocalDateTime eventDateBefore;

    private String initiatorId;
    private List<String> initiatorIn;

    private Boolean paidEq;
    private Integer participantLimitEq;
    private Boolean requestModeration;

    private LocalDateTime publishedOnEq;
    private LocalDateTime publishedOnAfter;
    private LocalDateTime publishedOnBefore;

    private EventState stateEq;
    private List<EventState> statesIn;
}
