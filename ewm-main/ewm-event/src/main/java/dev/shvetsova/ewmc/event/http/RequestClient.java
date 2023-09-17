package dev.shvetsova.ewmc.event.http;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ewm-users")
public interface RequestClient {

    @GetMapping("/users/{userId}/friends/requests")
    List<Long> getParticipateEventList(@PathVariable(value = "userId") long userId,
                                       @RequestParam(value = "friendsId") List<Long> friendsId);

}


