package dev.shvetsova.ewmc.users.http;

import dev.shvetsova.ewmc.users.security.OAuthFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ewm-event", configuration = OAuthFeignConfig.class)
public interface EventClient {
    @GetMapping("users/events/check/{userId}")
    Boolean checkEvents(@PathVariable String userId);
}

