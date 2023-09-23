package dev.shvetsova.ewmc.users.http;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ewm-event")
public interface EventClient {
    @GetMapping("users/events/check/{userId}")
    Boolean checkEvents(@PathVariable String userId);
}

