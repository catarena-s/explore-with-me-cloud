package dev.shvetsova.ewmc.request.http;

import dev.shvetsova.ewmc.dto.event.EventFullDto;
import dev.shvetsova.ewmc.request.security.OAuthFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ewm-event",configuration = OAuthFeignConfig.class)
public interface EventClient {

    @GetMapping("/users/{userId}/events/{id}")
    EventFullDto getEventById(@PathVariable(value = "userId") Long userId,
                              @PathVariable(value = "id") long id);

    @GetMapping("/users/{userId}/events/up/{id}")
    boolean upConfirmedRequests(@PathVariable(value = "userId") Long userId,
                                @PathVariable(value = "id") long id);
}


