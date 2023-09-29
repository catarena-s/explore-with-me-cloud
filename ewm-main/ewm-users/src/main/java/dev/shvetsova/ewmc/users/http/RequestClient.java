package dev.shvetsova.ewmc.users.http;

import dev.shvetsova.ewmc.users.security.OAuthFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ewm-request", configuration = OAuthFeignConfig.class)
public interface RequestClient {
    @GetMapping("users/requests/check/{userId}")
    Boolean checkRequests(@PathVariable String userId);
}

