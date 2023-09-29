package dev.shvetsova.ewmc.request.mq;

import dev.shvetsova.ewmc.dto.mq.RequestMqDto;
import dev.shvetsova.ewmc.mq.RabbitSupplier;
import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

@Configuration
@Getter
public class RequestSupplier extends RabbitSupplier<RequestMqDto> {
    @Bean
    public Supplier<Flux<Message<RequestMqDto>>> newRequestProduce() {
        return sink::asFlux;
    }

}
