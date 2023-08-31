package dev.shvetsova.ewmc.main.api.privat;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import dev.shvetsova.ewmc.main.dto.event.EventShortDto;
import dev.shvetsova.ewmc.main.dto.user.UserDto;
import dev.shvetsova.ewmc.main.service.subs.FriendService;

import java.util.List;

import static dev.shvetsova.ewmc.main.utils.Constants.FROM;
import static dev.shvetsova.ewmc.main.utils.Constants.PAGE_SIZE;

@RestController
@RequestMapping(path = "/users/{userId}")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PrivateFriendController {
    private final FriendService friendService;


    /**
     * Получить список друзей пользователя<br>
     * GET /users/{userId}/friends
     */
    @GetMapping("/friends")
    public List<UserDto> getFriends(@PathVariable("userId") long userId) {
        log.debug("Request received POST '/users/{}/friends'", userId);
        return friendService.getFriends(userId);
    }

    /**
     * Получить список подписчиков текущего пользователя<br>
     * GET /users/{userId}/followers
     */
    @GetMapping("/followers")
    public List<UserDto> getFollowers(@PathVariable("userId") long userId) {
        log.debug("Request received POST '/users/{}'", userId);
        return friendService.getFollowers(userId);
    }

    /**
     * Получить события в которых будут участвовать друзья текущего пользователя<br>
     * GET /users/{userId}/friends/share?from={from}&size={size}
     */
    @GetMapping("/friends/share")
    public List<EventShortDto> getParticipateEvents(
            @PathVariable("userId") long userId,
            @PositiveOrZero @RequestParam(value = "from", defaultValue = FROM) int from,
            @Positive @RequestParam(value = "size", defaultValue = PAGE_SIZE) int size) {
        log.debug("Request received GET /users/{}/friends/share?from={}&size={}", userId, from, size);
        return friendService.getParticipateEvents(userId, from, size);
    }

    /**
     * Получить список событий опубликованных друзьями текущего пользователя<br>
     * GET /users/{userId}/friends/events?from={from}&size={size}
     */
    @GetMapping("/friends/events")
    public List<EventShortDto> getFriendEvents(
            @PathVariable("userId") long userId,
            @PositiveOrZero @RequestParam(value = "from", defaultValue = FROM) int from,
            @Positive @RequestParam(value = "size", defaultValue = PAGE_SIZE) int size) {
        log.debug("Request received GET /users/{}/friends/events?from={}&size={}", userId, from, size);
        return friendService.getFriendEvents(userId, from, size);
    }

}
