package dev.shvetsova.ewmc.request.http;

import dev.shvetsova.ewmc.utils.UserFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ewm-users")
public interface UserClient extends UserFeignClient {

    @GetMapping("/users/{userId}/check")
    Boolean checkExistById(@PathVariable(value = "userId") long userId);
}


