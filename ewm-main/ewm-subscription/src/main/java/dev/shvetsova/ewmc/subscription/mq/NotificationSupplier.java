package dev.shvetsova.ewmc.subscription.mq;

import dev.shvetsova.ewmc.dto.notification.NewNotificationDto;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.support.MessageBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.util.concurrent.Queues;
import org.springframework.messaging.Message;
import java.util.function.Supplier;

@Configuration
@Getter
@Slf4j
public class NotificationSupplier {
    private Sinks.Many<Message<NewNotificationDto>> innerBus = Sinks.many().multicast().onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false);

    @Bean
    public Supplier<Flux<Message<NewNotificationDto>>> newNotificationProduce() {
        return () -> innerBus.asFlux();// будет считывать данные из потока Flux (как только туда попадают новые сообщения)
    }

    public void sendNewMessage(NewNotificationDto dto) {
        innerBus.emitNext(MessageBuilder.withPayload(dto).build(), Sinks.EmitFailureHandler.FAIL_FAST);
        log.info("Message sent: {}", dto);
    }
}
