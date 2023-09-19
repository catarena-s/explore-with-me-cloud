package dev.shvetsova.ewmc.notification.service;

import dev.shvetsova.ewmc.dto.notification.NewNotificationDto;
import dev.shvetsova.ewmc.dto.notification.NotificationDto;

import java.util.List;

public interface NotificationService {
    void addNotification(NewNotificationDto dto);

    NotificationDto getById(String userId, long id);

    List<NotificationDto> getAllMsg(String userId);

    NotificationDto markAsRead(String userId, long id);

    List<NotificationDto> markAsReadList(String userId, List<Long> ids);

    void deleteById(String userId, long id);

    void deleteList(String userId, List<Long> ids);
}
