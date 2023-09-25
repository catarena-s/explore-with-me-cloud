package dev.shvetsova.ewmc.event.mq;

import dev.shvetsova.ewmc.dto.mq.EventInfoMq;
import dev.shvetsova.ewmc.dto.mq.RequestStatusMqDto;
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
public class EventSupplier{
    @Value("${user-events.topic.name}")
    private String TOPIC_EVENT_FROM_USER_NAME;

    @Value("${notification.topic.name}")
    private String TOPIC_NOTIFICATION_NAME;

    @Value("${event-request.topic.name}")
    private String TOPIC_REQUEST_NAME;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void changeStatusRequests(RequestStatusMqDto dto) {
        kafkaTemplate.send(TOPIC_REQUEST_NAME, dto);
        log.info("Message sent: {}", dto);
    }
    public void newEventFromUserProduce(EventInfoMq dto) {
        kafkaTemplate.send(TOPIC_EVENT_FROM_USER_NAME, dto);
        log.info("Message sent: {}", dto);
    }

    public void sendNewMessage(NewNotificationDto dto) {
        kafkaTemplate.send(TOPIC_NOTIFICATION_NAME, dto);
        log.info("Message sent: {}", dto);
    }
}
