package dev.shvetsova.ewmc.notification.controller;

import dev.shvetsova.ewmc.dto.notification.NotificationDto;
import dev.shvetsova.ewmc.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/msg")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/{id}")
    public NotificationDto getMsgById(@PathVariable long userId, @PathVariable long id) {
        return notificationService.getById(userId, id);
    }


    @GetMapping()
    public List<NotificationDto> getAllMsg(@PathVariable long userId) {
        return notificationService.getAllMsg(userId);
    }

    @PatchMapping("/{id}")
    public NotificationDto markAsRead(@PathVariable long userId, @PathVariable long id) {
        return notificationService.markAsRead(userId, id);
    }

    @PatchMapping
    public List<NotificationDto> markAsReadList(@PathVariable long userId,
                                                @RequestParam List<Long> ids) {
        return notificationService.markAsReadList(userId, ids);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable long userId, @PathVariable long id) {
        notificationService.deleteById(userId, id);
    }

    @DeleteMapping
    public void deleteList(@PathVariable long userId, @RequestParam List<Long> ids) {
        notificationService.deleteList(userId, ids);
    }
}
