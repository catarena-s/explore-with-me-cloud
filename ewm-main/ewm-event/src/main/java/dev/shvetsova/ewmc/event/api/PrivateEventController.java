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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PrivateEventController {
    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto saveEvent(@Valid @RequestBody NewEventDto body,
                                  @PathVariable long userId) {
        log.debug("Request received POST /users/{}/events : {}", userId, body);
        return eventService.saveEvent(userId, body);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable(value = "userId") long userId,
                                 @PathVariable(value = "eventId") long eventId) {
        log.debug("Request received GET /users/{}/events/{}", userId, eventId);
        return eventService.getEventById(eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventByUser(@Valid @RequestBody UpdateEventUserRequest body,
                                          @PathVariable(value = "userId") long userId,
                                          @PathVariable(value = "eventId") long eventId) {
        log.debug("Request received PATCH /users/{}/events/{} : {}", userId, eventId, body);
        return eventService.updateEventByUser(body, userId, eventId);
    }

    @GetMapping
    public List<EventShortDto> getEvents(@PathVariable(value = "userId") long userId,
                                         @PositiveOrZero @RequestParam(value = "from", defaultValue = Constants.FROM) int from,
                                         @Positive @RequestParam(value = "size", defaultValue = Constants.PAGE_SIZE) int size) {
        log.debug("Request received GET /users/{}/events?from={}&size={}", userId, from, size);
        return eventService.getPublishedEvents(userId, from, size);
    }

    @GetMapping("/check")
    public boolean checkEvents(@PathVariable(value = "userId") long userId) {
        log.debug("Request received GET /users/{}/events", userId);
        return eventService.isExistByInitiator(userId);
    }

    @GetMapping("/fiends")
    public List<EventShortDto> getEventList(@PathVariable(value = "userId") long userId,
                                            @RequestParam(value = "friendsId") List<Long> friendsId) {
        log.debug("Request received GET /users/{}/events", userId);
        return eventService.getPublishedEvents(userId, friendsId);
    }

    @GetMapping("/up/{id}")
    public boolean upConfirmedRequests(@PathVariable(value = "userId") long userId,
                                       @PathVariable(value = "id") long id) {
        log.debug("Request received GET /users/{}/events", userId);
        eventService.upConfirmedRequests(userId, id);
        return true;
    }

    @PatchMapping("/{eventId}/requests")
    public void changeRequestStatus(@RequestBody EventRequestStatusUpdateRequest body,
                                    @PathVariable(value = "userId") long userId,
                                    @PathVariable(value = "eventId") long eventId) {
        log.debug("Request received PATCH /users/{}/events/{}/requests : {}", userId, eventId, body);
        eventService.changeRequestStatus(body, userId, eventId);
    }
}
