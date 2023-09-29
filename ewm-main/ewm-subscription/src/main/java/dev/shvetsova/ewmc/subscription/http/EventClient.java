package dev.shvetsova.ewmc.subscription.http;

import dev.shvetsova.ewmc.dto.event.EventShortDto;
import dev.shvetsova.ewmc.subscription.security.OAuthFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ewm-event", configuration = OAuthFeignConfig.class)
public interface EventClient {

    @GetMapping("/users/events/fiends")
    List<EventShortDto> getEventList(@RequestParam(value = "friendsId") List<Long> friendsId);

    @GetMapping("/events/list")
    List<EventShortDto> getEventListByIds(@RequestParam(name = "ids") List<Long> eventListIds);
}


