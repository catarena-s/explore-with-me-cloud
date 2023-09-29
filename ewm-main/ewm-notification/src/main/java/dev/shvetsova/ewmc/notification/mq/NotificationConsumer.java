package dev.shvetsova.ewmc.notification.mq;

import dev.shvetsova.ewmc.dto.notification.NewNotificationDto;
import dev.shvetsova.ewmc.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class NotificationConsumer {
    public final NotificationService notificationService;

    @Bean
    public Consumer<Message<NewNotificationDto>> newNotificationConsumer() {
        return message -> notificationService.addNotification(message.getPayload());
    }
}
