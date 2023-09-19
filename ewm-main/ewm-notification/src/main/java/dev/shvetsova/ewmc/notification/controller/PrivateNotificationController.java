package dev.shvetsova.ewmc.notification.controller;

import dev.shvetsova.ewmc.dto.notification.NotificationDto;
import dev.shvetsova.ewmc.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/msg")
@RequiredArgsConstructor
public class PrivateNotificationController {
    private final NotificationService notificationService;

    @GetMapping("/{id}")
    public NotificationDto getMsgById(@PathVariable long id,
                                      @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        return notificationService.getById(userId, id);
    }


    @GetMapping()
    public List<NotificationDto> getAllMsg(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        return notificationService.getAllMsg(userId);
    }

    @PatchMapping("/{id}")
    public NotificationDto markAsRead(@PathVariable long id,
                                      @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        return notificationService.markAsRead(userId, id);
    }

    @PatchMapping
    public List<NotificationDto> markAsReadList(@RequestParam List<Long> ids,
                                                @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        return notificationService.markAsReadList(userId, ids);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable long id,
                           @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        notificationService.deleteById(userId, id);
    }

    @DeleteMapping
    public void deleteList(@RequestParam List<Long> ids,
                           @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        notificationService.deleteList(userId, ids);
    }
}
