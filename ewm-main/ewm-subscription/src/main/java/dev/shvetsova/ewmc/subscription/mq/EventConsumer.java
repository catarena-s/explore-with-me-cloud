package dev.shvetsova.ewmc.subscription.mq;

import dev.shvetsova.ewmc.dto.mq.EventInfoMq;
import dev.shvetsova.ewmc.subscription.service.subs.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class EventConsumer {
    public final FriendService friendService;

    @Bean
    public Consumer<Message<EventInfoMq>> newEventFromUserConsumer() {
        return message -> friendService.sendNotificationToFriends(message.getPayload()); // будет считывать данные из потока Flux (как только туда попадают новые сообщения)
    }
}
