package dev.shvetsova.ewmc.subscription.api;

import dev.shvetsova.ewmc.dto.subs.FriendshipDto;
import dev.shvetsova.ewmc.dto.subs.FriendshipShortDto;
import dev.shvetsova.ewmc.subscription.service.subs.FriendshipService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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


import java.util.List;

@RestController
@RequestMapping(path = "/users/friendships")
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
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable("friendId") String friendId) {
        String userId = jwt.getSubject();
        log.debug("Request received POST '/users/{}/friendships/{}'", userId, friendId);
        return friendshipService.requestFriendship(userId, friendId);
    }

    /**
     * Удаление отправленного запроса на дружбу<br>
     * DELETE /users/{userId}/friendships/{subsId} <br>
     */
    @DeleteMapping("/{subsId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteFriendshipRequest(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable("subsId") Long subsId) {
        String userId = jwt.getSubject();
        log.debug("Request received DELETE /users/{}/friendships/{}", userId, subsId);
        friendshipService.deleteFriendshipRequest(userId, subsId);
    }

    /**
     * Подтверждение полученного запроса на дружбу<br>
     * PATCH /users/{userId}/friendships/approve?ids={ids} <br>
     * - Подтвердить можно только запросы в ожидании(PENDING) (Ожидается код ошибки 409)
     */
    @PatchMapping("/approve")
    public List<FriendshipShortDto> approveFriendship(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(value = "ids") List<Long> ids) {
        String userId = jwt.getSubject();
        log.debug("Request received PATCH '/users/{}/friendships/approve?ids={}'", userId, ids);
        return friendshipService.approveFriendship(userId, ids);
    }

    /**
     * Отклонение полученного запроса на дружбу<br>
     * PATCH /users/{userId}/friendships/reject?ids={ids} <br>
     * - Отклонить можно только запросы в состоянии PENDING или APPROVED (Ожидается код ошибки 409)
     */
    @PatchMapping("/reject")
    public List<FriendshipShortDto> rejectFriendship(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(value = "ids") List<Long> ids) {
        String userId = jwt.getSubject();
        log.debug("Request received PATCH '/users/{}/friendships/reject?ids={}'", userId, ids);
        return friendshipService.rejectFriendship(userId, ids);
    }

    /**
     * Получить список поданных текущим пользователем заявок на дружбу<BR>
     * GET /users/{userId}/friendships?filter={filter} <br>
     * filter: ALL, PENDING, APPROVED, REJECTED
     */
    @GetMapping("/requests")
    public List<FriendshipShortDto> getFriendshipRequests(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(value = "filter", defaultValue = "ALL") String filter
    ) {
        String userId = jwt.getSubject();
        log.debug("Request received POST /users/{}/friendships?filter={}", userId, filter);
        return friendshipService.getFriendshipRequests(userId, filter);
    }

    /**
     * Получить список полученных текущим пользователем заявок на дружбу<br>
     * GET /users/{userId}/followers?filter={filter} <br>
     * filter: ALL, PENDING, APPROVED, REJECTED
     */
    @GetMapping("/followers")
    public List<FriendshipShortDto> getIncomingFriendRequests(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(value = "filter", defaultValue = "ALL") String filter
    ) {
        String userId = jwt.getSubject();
        log.debug("Request received POST /users/{}/followers?filter={}", userId, filter);
        return friendshipService.getIncomingFriendRequests(userId, filter);
    }
}
