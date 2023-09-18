package dev.shvetsova.ewmc.notification.service;

import dev.shvetsova.ewmc.dto.notification.NewNotificationDto;
import dev.shvetsova.ewmc.dto.notification.NotificationDto;

import java.util.List;

public interface NotificationService {
    void addNotification(NewNotificationDto dto);

    NotificationDto getById(long userId, long id);

    List<NotificationDto> getAllMsg(long userId);

    NotificationDto markAsRead(long userId, long id);

    List<NotificationDto> markAsReadList(long userId, List<Long> ids);

    void deleteById(long userId, long id);

    void deleteList(long userId, List<Long> ids);
}
