package dev.shvetsova.ewmc.subscription.http;

import dev.shvetsova.ewmc.dto.user.UserDto;
import dev.shvetsova.ewmc.subscription.security.OAuthFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ewm-users", configuration = OAuthFeignConfig.class)
public interface UserClient {

    @GetMapping("/users/list?ids={ids}")
    List<UserDto> getUserList(@RequestParam(value = "ids") List<Long> ids);


}

