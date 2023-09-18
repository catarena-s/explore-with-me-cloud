package dev.shvetsova.ewmc.request.mq;

import dev.shvetsova.ewmc.dto.mq.RequestMqDto;
import dev.shvetsova.ewmc.request.service.request.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class RequestConsumer {
    public final RequestService requestService;

    @Bean
    public Consumer<Message<RequestMqDto>> changeStatusRequestsConsumer() {
        return message -> requestService.changeStatusRequests(message.getPayload()); // будет считывать данные из потока Flux (как только туда попадают новые сообщения)
    }
}
