package dev.shvetsova.ewmc.request.mq;

import dev.shvetsova.ewmc.dto.notification.NewNotificationDto;
import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.util.concurrent.Queues;

import java.util.function.Supplier;

@Configuration
@Getter
public class NotificationSupplier {
    private Sinks.Many<Message<NewNotificationDto>> messageBus = Sinks.many().multicast().onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false);

    @Bean
    public Supplier<Flux<Message<NewNotificationDto>>> newNotificationProduce() {
        return () -> messageBus.asFlux();// будет считывать данные из потока Flux (как только туда попадают новые сообщения)
    }

    public void sendNewMessage(NewNotificationDto dto) {
        messageBus.emitNext(MessageBuilder.withPayload(dto).build(), Sinks.EmitFailureHandler.FAIL_FAST);
    }
}
