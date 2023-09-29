package dev.shvetsova.ewmc.subscription.http;

import dev.shvetsova.ewmc.subscription.security.OAuthFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ewm-request", configuration = OAuthFeignConfig.class)
public interface RequestClient {

    @GetMapping("/users/friends/requests")
    List<Long> getParticipateEventList(@RequestParam(value = "friendsId") List<Long> friendsId);
}


