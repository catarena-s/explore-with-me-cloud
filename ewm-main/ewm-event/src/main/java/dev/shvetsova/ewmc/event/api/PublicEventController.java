package dev.shvetsova.ewmc.event.api;

import dev.shvetsova.ewmc.dto.event.EventShortDto;
import dev.shvetsova.ewmc.dto.event.EventFullDto;
import dev.shvetsova.ewmc.event.enums.SortType;
import dev.shvetsova.ewmc.event.service.event.EventService;
import dev.shvetsova.ewmc.utils.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping(path = "/events")
@Slf4j
@RequiredArgsConstructor
@Validated
public class PublicEventController {
    private final EventService eventService;

    @GetMapping("/{id}")
    public EventFullDto getPublishedEvent(@PathVariable long id, HttpServletRequest request) {
        log.debug("Request received GET {}", request.getRequestURI());
        return eventService.getPublishedEvent(id, request);
    }

    @GetMapping("/list")
    public List<EventShortDto> getPublishedEvents(@RequestParam List<Long> ids) {
        log.debug("Request received GET {}", ids);
        return eventService.findEventsByIds(ids);
    }

    @GetMapping
    public List<EventShortDto> getPublishedEvents(
            @RequestParam(value = "text", required = false) String text,
            @RequestParam(value = "categories", required = false) List<Long> categories,
            @RequestParam(value = "paid", required = false) Boolean paid,
            @RequestParam(value = "rangeStart", required = false)
            @DateTimeFormat(pattern = Constants.YYYY_MM_DD_HH_MM_SS) LocalDateTime rangeStart,
            @RequestParam(value = "rangeEnd", required = false)
            @DateTimeFormat(pattern = Constants.YYYY_MM_DD_HH_MM_SS) LocalDateTime rangeEnd,
            @RequestParam(value = "onlyAvailable", defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(value = "sort", required = false) SortType sort,
            @PositiveOrZero @RequestParam(value = "from", defaultValue = Constants.FROM) Integer from,
            @Positive @RequestParam(value = "size", defaultValue = Constants.PAGE_SIZE) Integer size,
            HttpServletRequest request
    ) {
        log.debug("Request received GET /events");
        log.debug("RequestParams: text='{}',categories={},paid={},rangeStart={},rangeEnd={},onlyAvailable={},sort='{}',from={},size={}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        return eventService.getPublishedEvents(
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request);
    }
}
