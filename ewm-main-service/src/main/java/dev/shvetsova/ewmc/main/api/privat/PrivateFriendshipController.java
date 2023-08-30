package dev.shvetsova.ewmc.main.api.privat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import dev.shvetsova.ewmc.main.dto.subs.FriendshipShortDto;
import dev.shvetsova.ewmc.main.dto.subs.FriendshipDto;
import dev.shvetsova.ewmc.main.service.subs.FriendshipService;

import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/friendships")
@RequiredArgsConstructor
@Slf4j
@Validated

public class PrivateFriendshipController {
    private final FriendshipService friendshipService;

    /**
     * Отправка запрос на дружбу<br>
     * POST /users/{userId}/friendships/{friendId} <br>
     * - дружба не взаимная <br>
     * - нельзя добавить повторный запрос если текущий статус PENDING или APPROVED (Ожидается код ошибки 409) <br>
     * - нельзя подписаться на самого себя (Ожидается код ошибки 409) <br>
     * - если для пользователя отключена пре-модерация запросов на дружбу, то запрос должен автоматически
     * перейти в состояние подтвержденного <br>
     */
    @PostMapping("/{friendId}")
    @ResponseStatus(HttpStatus.CREATED)
    public FriendshipDto requestFriendship(
            @PathVariable("userId") long followerId,
            @PathVariable("friendId") long friendId) {
        log.debug("Request received POST '/users/{}/friendships/{}'", followerId, friendId);
        return friendshipService.requestFriendship(followerId, friendId);
    }

    /**
     * Удаление отправленного запроса на дружбу<br>
     * DELETE /users/{userId}/friendships/{subsId} <br>
     */
    @DeleteMapping("/{subsId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteFriendshipRequest(
            @PathVariable("userId") long followerId,
            @PathVariable("subsId") long subsId) {
        log.debug("Request received DELETE /users/{}/friendships/{}", followerId, subsId);
        friendshipService.deleteFriendshipRequest(followerId, subsId);
    }

    /**
     * Подтверждение полученного запроса на дружбу<br>
     * PATCH /users/{userId}/friendships/approve?ids={ids} <br>
     * - Подтвердить можно только запросы в ожидании(PENDING) (Ожидается код ошибки 409)
     */
    @PatchMapping("/approve")
    public List<FriendshipShortDto> approveFriendship(
            @PathVariable("userId") long followerId,
            @RequestParam(value = "ids") List<Long> ids) {
        log.debug("Request received PATCH '/users/{}/friendships/approve?ids={}'", followerId, ids);
        return friendshipService.approveFriendship(followerId, ids);
    }

    /**
     * Отклонение полученного запроса на дружбу<br>
     * PATCH /users/{userId}/friendships/reject?ids={ids} <br>
     * - Отклонить можно только запросы в состоянии PENDING или APPROVED (Ожидается код ошибки 409)
     */
    @PatchMapping("/reject")
    public List<FriendshipShortDto> rejectFriendship(
            @PathVariable("userId") long followerId,
            @RequestParam(value = "ids") List<Long> ids) {
        log.debug("Request received PATCH '/users/{}/friendships/reject?ids={}'", followerId, ids);
        return friendshipService.rejectFriendship(followerId, ids);
    }

    /**
     * Получить список поданных текущим пользователем заявок на дружбу<BR>
     * GET /users/{userId}/friendships?filter={filter} <br>
     * filter: ALL, PENDING, APPROVED, REJECTED
     */
    @GetMapping("/requests")
    public List<FriendshipShortDto> getFriendshipRequests(
            @PathVariable("userId") long followerId,
            @RequestParam(value = "filter", defaultValue = "ALL") String filter
    ) {
        log.debug("Request received POST /users/{}/friendships?filter={}", followerId, filter);
        return friendshipService.getFriendshipRequests(followerId, filter);
    }

    /**
     * Получить список полученных текущим пользователем заявок на дружбу<br>
     * GET /users/{userId}/followers?filter={filter} <br>
     * filter: ALL, PENDING, APPROVED, REJECTED
     */
    @GetMapping("/followers")
    public List<FriendshipShortDto> getIncomingFriendRequests(
            @PathVariable("userId") long userId,
            @RequestParam(value = "filter", defaultValue = "ALL") String filter
    ) {
        log.debug("Request received POST /users/{}/followers?filter={}", userId, filter);
        return friendshipService.getIncomingFriendRequests(userId, filter);
    }
}
