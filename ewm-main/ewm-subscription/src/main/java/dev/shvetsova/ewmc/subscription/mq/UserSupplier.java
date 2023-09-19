package dev.shvetsova.ewmc.subscription.mq;

import dev.shvetsova.ewmc.dto.mq.FriendshipRequestMq;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.util.concurrent.Queues;

import java.util.function.Supplier;

@Configuration
@RequiredArgsConstructor
public class UserSupplier {
    private Sinks.Many<Message<FriendshipRequestMq>> innerBus = Sinks.many().multicast().onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false);

    @Bean
    public Supplier<Flux<Message<FriendshipRequestMq>>> newFriendshipRequestProduce() {
        return () -> innerBus.asFlux();// будет считывать данные из потока Flux (как только туда попадают новые сообщения)
    }

    public void sendFriendshipRequest(FriendshipRequestMq dto) {
        innerBus.emitNext(MessageBuilder.withPayload(dto).build(), Sinks.EmitFailureHandler.FAIL_FAST);
    }
}
