package dev.shvetsova.ewmc.event.mq;

import dev.shvetsova.ewmc.dto.mq.EventInfoMq;
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
public class EventSupplier {
    private final Sinks.Many<Message<EventInfoMq>> eventBus = Sinks.many().multicast().onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false);

    @Bean
    public Supplier<Flux<Message<EventInfoMq>>> newEventFromUserProduce() {
        return eventBus::asFlux;// будет считывать данные из потока Flux (как только туда попадают новые сообщения)
    }

    public void newEventFromUser(EventInfoMq requests) {
        eventBus.emitNext(MessageBuilder.withPayload(requests).build(), Sinks.EmitFailureHandler.FAIL_FAST);
    }
}
