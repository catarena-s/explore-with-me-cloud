package dev.shvetsova.ewmc.event.http;

import dev.shvetsova.ewmc.event.security.OAuthFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ewm-users",configuration = OAuthFeignConfig.class)
public interface RequestClient {

    @GetMapping("/users/friends/requests")
    List<Long> getParticipateEventList(@RequestParam(value = "friendsId") List<Long> friendsId);

}


