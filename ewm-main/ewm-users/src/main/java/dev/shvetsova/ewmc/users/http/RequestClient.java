package dev.shvetsova.ewmc.users.http;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ewm-request")
public interface RequestClient {
    @GetMapping("users/{userId}/requests/check")
    Boolean checkRequests(@PathVariable long userId);
}

