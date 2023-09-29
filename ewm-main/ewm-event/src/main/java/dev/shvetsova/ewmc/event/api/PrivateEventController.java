package dev.shvetsova.ewmc.event.api;

import dev.shvetsova.ewmc.dto.event.EventFullDto;
import dev.shvetsova.ewmc.dto.event.EventShortDto;
import dev.shvetsova.ewmc.dto.event.NewEventDto;
import dev.shvetsova.ewmc.dto.event.UpdateEventUserRequest;
import dev.shvetsova.ewmc.dto.request.EventRequestStatusUpdateRequest;
import dev.shvetsova.ewmc.event.service.event.EventService;
import dev.shvetsova.ewmc.utils.Constants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/users/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PrivateEventController {
    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto saveEvent(@Valid @RequestBody NewEventDto body,
                                  @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        log.debug("Request received POST /users/{}/events : {}", userId, body);
        return eventService.saveEvent(userId, body);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable(value = "eventId") long eventId,
                                 @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        log.debug("Request received GET /users/{}/events/{}", userId, eventId);
        return eventService.getEventById(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventByUser(@Valid @RequestBody UpdateEventUserRequest body,
                                          @PathVariable(value = "eventId") long eventId,
                                          @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        log.debug("Request received PATCH /users/{}/events/{} : {}", userId, eventId, body);
        return eventService.updateEventByUser(body, userId, eventId);
    }

    @GetMapping
    public List<EventShortDto> getEvents(@PositiveOrZero @RequestParam(value = "from", defaultValue = Constants.FROM) int from,
                                         @Positive @RequestParam(value = "size", defaultValue = Constants.PAGE_SIZE) int size,
                                         @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        log.debug("Request received GET /users/{}/events?from={}&size={}", userId, from, size);
        return eventService.getPublishedEvents(userId, from, size);
    }

    @GetMapping("/check/{userId}")
    public boolean checkEvents(
            @PathVariable(value = "userId") String uId) {
        log.debug("Request received GET /users/{}/events", uId);
        return eventService.isExistByInitiator(uId);
    }

    @GetMapping("/fiends")
    public List<EventShortDto> getEventList(@RequestParam(value = "friendsId") List<Long> friendsId,
                                            @AuthenticationPrincipal Jwt jwt) {
        return eventService.getPublishedEvents(friendsId);
    }

    @PatchMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public void changeRequestStatus(@RequestBody EventRequestStatusUpdateRequest body,
                                    @PathVariable(value = "eventId") long eventId,
                                    @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        log.debug("Request received PATCH /users/{}/events/{}/requests : {}", userId, eventId, body);
        eventService.changeRequestStatus(body, userId, eventId);
    }
}
