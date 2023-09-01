package dev.shvetsova.ewmc.users.http;

import dev.shvetsova.ewmc.users.exception.FallbackException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ewm-event")
public interface EventClient {
    @GetMapping("users/{userId}/events/check")
    Boolean checkEvents(@PathVariable long userId);
}

