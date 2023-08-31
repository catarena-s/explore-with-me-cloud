package dev.shvetsova.ewmc.main.api.privat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import dev.shvetsova.ewmc.main.dto.user.UserDto;
import dev.shvetsova.ewmc.main.service.user.UserService;

@RestController
@RequestMapping("/users/{userId}")
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
    public UserDto changeSubscribeMode(@PathVariable(value = "userId") long userId,
                                       @RequestParam(value = "auto", defaultValue = "true") boolean isAutoSubscribe
    ) {
        log.debug("Request received PATCH /users/{}/subs?auto={}", userId, isAutoSubscribe);
        return userService.changeSubscribeMode(userId, isAutoSubscribe);
    }
}
