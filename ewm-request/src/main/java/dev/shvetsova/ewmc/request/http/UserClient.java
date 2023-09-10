package dev.shvetsova.ewmc.request.http;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ewm-users")
public interface UserClient {

    @GetMapping("/users/{userId}/check")
    Boolean checkExistById(@PathVariable(value = "userId") long userId);
}


