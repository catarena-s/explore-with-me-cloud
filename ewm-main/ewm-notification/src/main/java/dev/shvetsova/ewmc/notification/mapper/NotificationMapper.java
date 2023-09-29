package dev.shvetsova.ewmc.notification.mapper;

import dev.shvetsova.ewmc.dto.notification.NewNotificationDto;
import dev.shvetsova.ewmc.dto.notification.NotificationDto;
import dev.shvetsova.ewmc.notification.model.Notification;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NotificationMapper {

    public static NotificationDto toDto(Notification notification) {
        return NotificationDto.builder()
                .id(notification.getId())
                .userId(notification.getConsumerId())
                .text(notification.getText())
                .idRead(notification.isRead())
                .created(notification.getCreated())
                .build();
    }

    public static Notification fromDto(NewNotificationDto dto) {
        return Notification.builder()
                .text(dto.getText())
                .created(LocalDateTime.now())
                .consumerId(dto.getUserId())
                .senderId(dto.getSenderId())
                .messageType(dto.getMessageType())
                .read(false)
                .build();
    }
}
