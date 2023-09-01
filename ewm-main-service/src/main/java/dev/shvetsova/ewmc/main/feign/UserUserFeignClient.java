package dev.shvetsova.ewmc.main.feign;

import dev.shvetsova.ewmc.common.dto.user.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ewm-users")
public interface UserUserFeignClient {

    @GetMapping("/users/{userId}")
    UserDto findUserById(@PathVariable(value = "userId") long userId);

    @GetMapping("/users/{userId}?type={type}")
    List<UserDto> getUserList(@RequestParam(name = "type") String type,
                              @PathVariable(value = "userId") long userId);

    @GetMapping("/users/{userId}/check")
    Boolean checkExistById(@PathVariable(value = "userId") long userId);

}


