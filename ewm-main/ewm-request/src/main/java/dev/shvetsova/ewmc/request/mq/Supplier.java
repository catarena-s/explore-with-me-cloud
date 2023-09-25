package dev.shvetsova.ewmc.request.mq;

import dev.shvetsova.ewmc.dto.mq.RequestMqDto;
import dev.shvetsova.ewmc.dto.notification.NewNotificationDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
@Getter
@Slf4j
@RequiredArgsConstructor
public class Supplier {

    @Value("${notification.topic.name}")
    private String TOPIC_NOTIFICATION_NAME;

    @Value("${request.topic.name}")
    private String TOPIC_REQUEST_NAME;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendNewMessage(NewNotificationDto dto) {
        kafkaTemplate.send(TOPIC_REQUEST_NAME, dto);
        log.info("Message sent: {}", dto);
    }

    public void sendNewMessage(RequestMqDto dto) {
        kafkaTemplate.send(TOPIC_REQUEST_NAME, dto);
        log.info("Message sent: {}", dto);
    }
}
