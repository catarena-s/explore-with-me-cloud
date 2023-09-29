package dev.shvetsova.ewmc.event.mq;

import dev.shvetsova.ewmc.dto.mq.RequestMqDto;
import dev.shvetsova.ewmc.event.service.event.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

@Configuration
@RequiredArgsConstructor
public class EventConsumer {
    public final EventService service;

    @Bean
    public java.util.function.Consumer<Message<RequestMqDto>> newRequestConsumer() {
        return message -> service.getNewRequest(message.getPayload());
    }
}
