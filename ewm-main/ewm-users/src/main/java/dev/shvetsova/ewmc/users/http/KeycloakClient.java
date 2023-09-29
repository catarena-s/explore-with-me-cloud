package dev.shvetsova.ewmc.users.http;

import dev.shvetsova.ewmc.dto.user.NewUserRequest;
import dev.shvetsova.ewmc.dto.user.UserDto;
import dev.shvetsova.ewmc.users.security.OAuthFeignConfig;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "ewm-keycloak", configuration = OAuthFeignConfig.class)
public interface KeycloakClient {

    @PostMapping("/kc/users")
    String registerUser(@Valid @RequestBody NewUserRequest body);

    @GetMapping("/kc/users/{userId}")
    UserDto findUserById(@PathVariable String userId);

    @DeleteMapping("/kc/users/{userId}")
    void delete(@PathVariable(value = "userId") String userId);
}

