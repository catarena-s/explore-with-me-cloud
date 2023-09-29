package dev.shvetsova.ewmc.request.api;

import dev.shvetsova.ewmc.dto.request.ParticipationRequestDto;
import dev.shvetsova.ewmc.request.service.request.RequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class PrivateRequestController {
    private final RequestService requestService;

    @PostMapping("/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addParticipationRequest(
            @RequestParam(value = "eventId") long eventId,
            @AuthenticationPrincipal Jwt jwt) {
        final String userId = jwt.getSubject();
        log.debug("Request received GET /users/{}/requests?eventId={}", userId, eventId);
        return requestService.addParticipationRequest(userId, eventId);
    }

    @GetMapping("/requests/events/{eventId}")
    public List<ParticipationRequestDto> getEventParticipants(
            @PathVariable(value = "eventId") long eventId,
            @AuthenticationPrincipal Jwt jwt) {
        final String userId = jwt.getSubject();
        log.debug("Request received GET /users/{}/events/{}/requests", userId, eventId);
        return requestService.getEventParticipants(userId, eventId);
    }

    @GetMapping("/requests/{id}")
    public ParticipationRequestDto getUserRequests(
            @PathVariable(value = "id") long id,
            @AuthenticationPrincipal Jwt jwt) {
        final String userId = jwt.getSubject();
        log.debug("Request received GET /users/{}/requests", userId);
        return requestService.getUserRequest(userId,id);
    }
    @GetMapping("/requests")
    public List<ParticipationRequestDto> getUserRequests(
            @AuthenticationPrincipal Jwt jwt) {
        final String userId = jwt.getSubject();
        log.debug("Request received GET /users/{}/requests", userId);
        return requestService.getUserRequests(userId);
    }

    @GetMapping("/friends/requests")
    public List<Long> getFriendsRequests(
            @RequestParam(value = "friendsId") List<Long> friendsId,
            @AuthenticationPrincipal Jwt jwt) {
        final String userId = jwt.getSubject();
        log.debug("Request received GET /users/{}/friends/requests", userId);
        return requestService.getFriendsEventRequests(userId, friendsId);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(
            @PathVariable(value = "requestId") long requestId,
            @AuthenticationPrincipal Jwt jwt) {
        final String userId = jwt.getSubject();
        log.debug("Request received GET /users/{}/requests/{}/cancel", userId, requestId);
        return requestService.cancelRequest(userId, requestId);
    }

    /**
     * Скрыть события от друзей<br>
     * - Изменить видимость можно только у подтвержденных запросов на участие<br>
     * - Изменять можно только свои запросы
     */
    @PatchMapping("/requests/hide")
    public List<ParticipationRequestDto> hideParticipation(
            @RequestParam(value = "ids") List<Long> ids,
            @AuthenticationPrincipal Jwt jwt) {
        final String userId = jwt.getSubject();
        log.debug("Request received PATCH /users/{}/requests/hide?ids={}", userId, ids);
        return requestService.changeVisibilityEventParticipation(userId, ids, true);
    }

    /**
     * Показать события друзьям<br>
     * - Изменить видимость можно только у подтвержденных запросов на участие<br>
     * - Изменять можно только свои запросы
     */
    @PatchMapping("/requests/show")
    public List<ParticipationRequestDto> showParticipation(
            @RequestParam(value = "ids") List<Long> ids,
            @AuthenticationPrincipal Jwt jwt) {
        final String userId = jwt.getSubject();
        log.debug("Request received PATCH /users/{}/requests/show?ids={}", userId, ids);
        return requestService.changeVisibilityEventParticipation(userId, ids, false);
    }

    @GetMapping("/requests/check/{userId}")
    public boolean checkRequest(
            @PathVariable(value = "userId") String uId) {
        log.debug("Request received GET /users/{}/events", uId);
        return requestService.isExistByRequester(uId);
    }
}
