package dev.shvetsova.ewmc.users.mq;

import dev.shvetsova.ewmc.dto.mq.FriendshipRequestMq;
import dev.shvetsova.ewmc.users.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class Consumer {
    public final UserService userService;

    @KafkaListener(topics = "${friendship.topic.name}")
    public void getMessage(FriendshipRequestMq friendshipRequestMq) {
        log.info("get from kafka: " + friendshipRequestMq);
        userService.getFriendshipRequest(friendshipRequestMq);
    }
}
