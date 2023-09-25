package dev.shvetsova.ewmc.request.mq;

import dev.shvetsova.ewmc.dto.mq.RequestStatusMqDto;
import dev.shvetsova.ewmc.request.service.request.RequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class Consumer {
    public final RequestService requestService;

    @KafkaListener(topics = "${request-status.topic.name}")
    public void changeStatusRequestsConsumer(RequestStatusMqDto dto) {
        log.info("get from kafka: " + dto);
        requestService.changeStatusRequests(dto);
    }
}
