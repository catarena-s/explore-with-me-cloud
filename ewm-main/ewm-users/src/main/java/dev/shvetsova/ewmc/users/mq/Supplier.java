package dev.shvetsova.ewmc.users.mq;

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

    @Value("${friendship-request.topic.name}")
    private String TOPIC_FRIENDSHIP_REQUEST_NAME;

    @Value("${notification.topic.name}")
    private String TOPIC_NOTIFICATION_NAME;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void approveFriendship(Long dto) {
        kafkaTemplate.send(TOPIC_FRIENDSHIP_REQUEST_NAME, dto);
        log.info("Message sent: {}", dto);
    }

    public void sendMessage(NewNotificationDto dto) {
        kafkaTemplate.send(TOPIC_NOTIFICATION_NAME, dto);
        log.info("Message sent: {}", dto);
    }
}
