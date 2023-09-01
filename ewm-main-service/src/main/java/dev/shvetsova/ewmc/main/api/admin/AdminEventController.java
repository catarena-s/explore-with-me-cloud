package dev.shvetsova.ewmc.main.api.admin;

import dev.shvetsova.ewmc.common.dto.event.EventFullDto;
import dev.shvetsova.ewmc.common.dto.event.UpdateEventAdminRequest;
import dev.shvetsova.ewmc.main.service.event.EventService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static dev.shvetsova.ewmc.common.Constants.FROM;
import static dev.shvetsova.ewmc.common.Constants.PAGE_SIZE;
import static dev.shvetsova.ewmc.common.Constants.YYYY_MM_DD_HH_MM_SS;


@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdminEventController {
    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> getEventsByAdmin(
            //список id пользователей, чьи события нужно найти
            @RequestParam(value = "users", required = false) List<Long> users,
            //список состояний в которых находятся искомые события
            @RequestParam(value = "states", required = false) List<String> states,
            //список id категорий в которых будет вестись поиск
            @RequestParam(value = "categories", required = false) List<Long> categories,
            //дата и время не раньше которых должно произойти событие
            @RequestParam(value = "rangeStart", required = false)
            @DateTimeFormat(pattern = YYYY_MM_DD_HH_MM_SS) LocalDateTime rangeStart,
            //дата и время не позже которых должно произойти событие
            @RequestParam(value = "rangeEnd", required = false)
            @DateTimeFormat(pattern = YYYY_MM_DD_HH_MM_SS) LocalDateTime rangeEnd,
            //количество событий, которые нужно пропустить для формирования текущего набора
            @PositiveOrZero @RequestParam(value = "from", defaultValue = FROM) Integer from,
            //количество событий в наборе
            @Positive @RequestParam(value = "size", defaultValue = PAGE_SIZE) Integer size
    ) {
        log.debug("Request received GET /admin/events");
        log.debug("RequestParams: users={},states={},categories={},rangeStart={}, rangeEnd={}, from={}, size={} ",
                users, states, categories, rangeStart, rangeEnd, from, size);
        return eventService.getEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventByAdmin(@Valid @RequestBody(required = false) UpdateEventAdminRequest body,
                                           @PathVariable long eventId) {
        log.debug("Request received PATCH /admin/events/{}:{}", eventId, body);
        return eventService.updateEventByAdmin(body, eventId);
    }
}
