package dev.shvetsova.ewmc.compilation.http;

import dev.shvetsova.ewmc.compilation.dto.event.EventShortDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ewm-event")
public interface EventClient {

    @GetMapping("/events/list")
    List<EventShortDto> findEventsByIds(@RequestParam(name = "ids") List<Long> eventIdList);
}


