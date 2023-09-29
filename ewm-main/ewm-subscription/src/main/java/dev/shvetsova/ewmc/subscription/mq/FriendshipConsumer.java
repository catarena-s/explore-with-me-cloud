package dev.shvetsova.ewmc.subscription.mq;

import dev.shvetsova.ewmc.dto.mq.EventInfoMq;
import dev.shvetsova.ewmc.dto.mq.FriendshipRequestMq;
import dev.shvetsova.ewmc.subscription.service.subs.FriendService;
import dev.shvetsova.ewmc.subscription.service.subs.FriendshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class FriendshipConsumer {
    public final FriendshipService friendshipService;
    public final FriendService friendService;

    @Bean
    public Consumer<Message<FriendshipRequestMq>> approveFriendshipConsumer() {
        return message -> friendshipService.approveFriendship(message.getPayload());
    }

    @Bean
    public Consumer<Message<EventInfoMq>> newEventFromUserConsumer() {
        return message -> friendService.sendNotificationToFriends(message.getPayload());
    }
}
