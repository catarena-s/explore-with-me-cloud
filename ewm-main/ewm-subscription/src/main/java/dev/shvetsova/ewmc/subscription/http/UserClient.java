package dev.shvetsova.ewmc.subscription.http;

import dev.shvetsova.ewmc.dto.user.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ewm-users")
public interface UserClient {

    @GetMapping("/users/{userId}")
    UserDto findUserById(@PathVariable(value = "userId") String userId);

    @GetMapping("/users/{userId}/list?ids={ids}")
    List<UserDto> getUserList(@PathVariable(value = "userId") String userId,
                              @RequestParam(value = "ids") List<Long> ids);


}

