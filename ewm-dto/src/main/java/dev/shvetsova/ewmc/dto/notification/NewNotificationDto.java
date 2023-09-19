package dev.shvetsova.ewmc.dto.notification;

import dev.shvetsova.ewmc.enums.MessageType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewNotificationDto {
    private String userId;
    private String senderId;
    private MessageType messageType;
    private String text;
}
