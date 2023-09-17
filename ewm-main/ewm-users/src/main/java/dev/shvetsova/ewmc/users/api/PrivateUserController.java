package dev.shvetsova.ewmc.users.api;

import dev.shvetsova.ewmc.dto.user.UserDto;
import dev.shvetsova.ewmc.users.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public UserDto getUserById(@PathVariable(value = "userId") long userId) {
        log.debug("Request received GET /users/{}", userId);
        return userService.getUserById(userId);
    }

    @GetMapping("/check")
    public boolean checkExistById(@PathVariable(value = "userId") long userId) {
        log.debug("Request received GET /users/{}", userId);
        return userService.isExistUser(userId);
    }

    @GetMapping("/list")
    public List<UserDto> getUserList(@PathVariable(value = "userId") long userId,
                                     @RequestParam(value = "ids") List<Long> ids) {
        return userService.getUsersList(userId, ids);
    }
}
