package dev.shvetsova.ewmc.subscription.mq;

import dev.shvetsova.ewmc.dto.mq.FriendshipRequestMq;
import dev.shvetsova.ewmc.mq.RabbitSupplier;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

@Configuration
@RequiredArgsConstructor
public class FriendshipRequestSupplier extends RabbitSupplier<FriendshipRequestMq> {
    @Bean
    public Supplier<Flux<Message<FriendshipRequestMq>>> newFriendshipRequestProduce() {
        return sink::asFlux;
    }

}
