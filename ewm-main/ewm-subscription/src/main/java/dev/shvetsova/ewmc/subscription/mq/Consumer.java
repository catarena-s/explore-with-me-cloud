package dev.shvetsova.ewmc.subscription.mq;

import dev.shvetsova.ewmc.dto.mq.EventInfoMq;
import dev.shvetsova.ewmc.subscription.service.subs.FriendService;
import dev.shvetsova.ewmc.subscription.service.subs.FriendshipService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class Consumer {
    public final FriendService friendService;
    public final FriendshipService friendshipService;

    @KafkaListener(topics = "${user-events.topic.name}")
    public void getNewEventsInfoFromFriend(EventInfoMq eventInfo) {
        friendService.sendNotificationToFollowers(eventInfo);
    }

    @KafkaListener(topics = "${friendship-request.topic.name}")
    public void approveFriendship(Long friendshipId) {
        friendshipService.approveFriendship(friendshipId);
    }
}
