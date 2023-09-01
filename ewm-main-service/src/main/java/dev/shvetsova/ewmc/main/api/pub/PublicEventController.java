package dev.shvetsova.ewmc.main.api.pub;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import dev.shvetsova.ewmc.common.dto.event.EventFullDto;
import dev.shvetsova.ewmc.common.dto.event.EventShortDto;
import dev.shvetsova.ewmc.common.enums.SortType;
import dev.shvetsova.ewmc.main.service.event.EventService;

import java.time.LocalDateTime;
import java.util.List;

import static dev.shvetsova.ewmc.common.Constants.FROM;
import static dev.shvetsova.ewmc.common.Constants.PAGE_SIZE;
import static dev.shvetsova.ewmc.common.Constants.YYYY_MM_DD_HH_MM_SS;

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

    @GetMapping
    public List<EventShortDto> getPublishedEvents(
            @RequestParam(value = "text", required = false) String text,
            @RequestParam(value = "categories", required = false) List<Long> categories,
            @RequestParam(value = "paid", required = false) Boolean paid,
            @RequestParam(value = "rangeStart", required = false)
            @DateTimeFormat(pattern = YYYY_MM_DD_HH_MM_SS) LocalDateTime rangeStart,
            @RequestParam(value = "rangeEnd", required = false)
            @DateTimeFormat(pattern = YYYY_MM_DD_HH_MM_SS) LocalDateTime rangeEnd,
            @RequestParam(value = "onlyAvailable", defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(value = "sort", required = false) SortType sort,
            @PositiveOrZero @RequestParam(value = "from", defaultValue = FROM) Integer from,
            @Positive @RequestParam(value = "size", defaultValue = PAGE_SIZE) Integer size,
            HttpServletRequest request
    ) {
        log.debug("Request received GET /events");
        log.debug("RequestParams: text='{}',categories={},paid={},rangeStart={},rangeEnd={},onlyAvailable={},sort='{}',from={},size={}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        return eventService.getPublishedEvents(
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request);
    }
}
