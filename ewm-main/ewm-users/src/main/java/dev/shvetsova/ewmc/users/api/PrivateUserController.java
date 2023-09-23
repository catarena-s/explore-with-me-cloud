package dev.shvetsova.ewmc.users.api;

import dev.shvetsova.ewmc.dto.user.UserDto;
import dev.shvetsova.ewmc.users.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/")
@RequiredArgsConstructor
@Slf4j
public class PrivateUserController {
    private final UserService userService;

    /**
     * Изменение режима подписки на пользователя.<br>
     * true - разрешен автоматический прием запросов дружбы<br>
     * false - необходимо подтверждение
     */
    @PatchMapping("/subs")
    public UserDto changeSubscribeMode(@RequestParam(value = "auto", defaultValue = "true") boolean isAutoSubscribe,
                                       @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        log.debug("Request received PATCH /users/{}/subs?auto={}", userId, isAutoSubscribe);
        return userService.changeSubscribeMode(userId, isAutoSubscribe);
    }

    @GetMapping("/list")
    public List<UserDto> getUserList(@RequestParam(value = "ids") List<String> ids) {
        return userService.getUsersList(ids);
    }
}
