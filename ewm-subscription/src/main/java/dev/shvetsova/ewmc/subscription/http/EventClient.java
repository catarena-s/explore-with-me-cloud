package dev.shvetsova.ewmc.subscription.http;

import dev.shvetsova.ewmc.subscription.dto.event.EventShortDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ewm-event")
public interface EventClient {

    @GetMapping("/users/{userId}/events/fiends")
    List<EventShortDto> getEventList(@PathVariable(value = "userId") long userId,
                                     @RequestParam(value = "friendsId") List<Long> friendsId);

    @GetMapping("/events/list")
    List<EventShortDto> getEventListByIds(@RequestParam(name = "ids") List<Long> eventListIds);
}


