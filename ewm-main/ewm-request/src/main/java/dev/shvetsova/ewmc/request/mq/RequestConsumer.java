package dev.shvetsova.ewmc.request.mq;

import dev.shvetsova.ewmc.dto.mq.RequestStatusMqDto;
import dev.shvetsova.ewmc.request.service.request.RequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RequestConsumer {
    public final RequestService requestService;

    @Bean
    public Consumer<Message<RequestStatusMqDto>> changeStatusRequestsConsumer() {

        return message -> {
            log.info("Get message :{}", message.getPayload());
            requestService.changeStatusRequests(message.getPayload());
        };
    }
}
