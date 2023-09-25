package dev.shvetsova.ewmc.notification.mq;

import dev.shvetsova.ewmc.dto.notification.NewNotificationDto;
import dev.shvetsova.ewmc.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class Consumer {
    public final NotificationService notificationService;

    @KafkaListener(topics = "${notification.topic.name}")
    public void getMessage(NewNotificationDto dto) {
        log.info("get from kafka: " + dto);
        notificationService.addNotification(dto);
    }
}
