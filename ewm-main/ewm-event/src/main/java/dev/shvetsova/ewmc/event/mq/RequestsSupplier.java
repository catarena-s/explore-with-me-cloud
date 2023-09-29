package dev.shvetsova.ewmc.event.mq;

import dev.shvetsova.ewmc.dto.mq.RequestStatusMqDto;
import dev.shvetsova.ewmc.mq.RabbitSupplier;
import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

@Configuration
@Getter
public class RequestsSupplier extends RabbitSupplier<RequestStatusMqDto> {

    @Bean
    public Supplier<Flux<Message<RequestStatusMqDto>>> changeStatusRequestsProduce() {
        return sink::asFlux;
    }

}
