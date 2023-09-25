package dev.shvetsova.ewmc.subscription.mq;

import dev.shvetsova.ewmc.dto.mq.FriendshipRequestMq;
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

    @Value("${friendship.topic.name}")
    private String TOPIC_FRIENDSHIP_NAME;

    @Value("${notification.topic.name}")
    private String TOPIC_NOTIFICATION_NAME;

    private final KafkaTemplate<String, Object> kafkaT;

    public void friendshipRequest(FriendshipRequestMq dto) {
        kafkaT.send(TOPIC_FRIENDSHIP_NAME, dto);
        log.info("Message sent: {}", dto);
    }

    public void sendNewMessage(NewNotificationDto dto) {
        kafkaT.send(TOPIC_NOTIFICATION_NAME, dto);
        log.info("Message sent: {}", dto);
    }


}
