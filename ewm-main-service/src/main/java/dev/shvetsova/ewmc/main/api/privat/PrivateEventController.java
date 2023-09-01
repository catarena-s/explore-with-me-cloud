package dev.shvetsova.ewmc.main.api.privat;

import dev.shvetsova.ewmc.common.dto.event.EventFullDto;
import dev.shvetsova.ewmc.common.dto.event.EventShortDto;
import dev.shvetsova.ewmc.common.dto.event.NewEventDto;
import dev.shvetsova.ewmc.common.dto.event.UpdateEventUserRequest;
import dev.shvetsova.ewmc.main.service.event.EventService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static dev.shvetsova.ewmc.common.Constants.FROM;
import static dev.shvetsova.ewmc.common.Constants.PAGE_SIZE;

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
        return eventService.getEvent(userId, eventId);
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
                                         @PositiveOrZero @RequestParam(value = "from", defaultValue = FROM) int from,
                                         @Positive @RequestParam(value = "size", defaultValue = PAGE_SIZE) int size) {
        log.debug("Request received GET /users/{}/events?from={}&size={}", userId, from, size);
        return eventService.getEvents(userId, from, size);
    }

    @GetMapping("/check")
    public boolean checkEvents(@PathVariable(value = "userId") long userId) {
        log.debug("Request received GET /users/{}/events", userId);
        return eventService.isExistByInitiator(userId);
    }
}
