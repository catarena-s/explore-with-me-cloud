package dev.shvetsova.ewmc.dto.notification;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificationDto {
    private long id;
    private String userId;
    private String text;
    private boolean idRead;
    private LocalDateTime created;
}
