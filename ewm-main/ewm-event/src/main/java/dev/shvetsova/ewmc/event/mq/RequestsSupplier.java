package dev.shvetsova.ewmc.event.mq;

import dev.shvetsova.ewmc.dto.mq.RequestMqDto;
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
public class RequestsSupplier {
    private final Sinks.Many<Message<RequestMqDto>> requestBus = Sinks.many().multicast().onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false);

    @Bean
    public Supplier<Flux<Message<RequestMqDto>>> changeStatusRequestsProduce() {
        return requestBus::asFlux;// будет считывать данные из потока Flux (как только туда попадают новые сообщения)
    }

    public void changeStatusRequests(RequestMqDto requests) {
        requestBus.emitNext(MessageBuilder.withPayload(requests).build(), Sinks.EmitFailureHandler.FAIL_FAST);
    }
}
