package dev.shvetsova.ewmc.subscription.http;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ewm-request")
public interface RequestClient {

    @GetMapping("/users/{userId}/friends/requests")
    List<Long> getParticipateEventList(@PathVariable(value = "userId") String userId,
                                       @RequestParam(value = "friendsId") List<Long> friendsId);
}


