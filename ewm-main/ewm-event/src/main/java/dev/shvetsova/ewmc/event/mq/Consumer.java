package dev.shvetsova.ewmc.event.mq;

import dev.shvetsova.ewmc.dto.mq.RequestMqDto;
import dev.shvetsova.ewmc.event.service.event.EventService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Configuration
@Getter
@Slf4j
@RequiredArgsConstructor
public class Consumer {
    public final EventService eventService;

    @KafkaListener(topics = "${event.topic.name}")
    public void requestConsume(RequestMqDto dto) {
        log.info("get from kafka: " + dto);
        eventService.getRequest(dto);
    }
}
