package dev.shvetsova.ewmc.request.mq;

import dev.shvetsova.ewmc.dto.notification.NewNotificationDto;
import dev.shvetsova.ewmc.mq.RabbitSupplier;
import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

@Configuration
@Getter
public class NotificationSupplier extends RabbitSupplier<NewNotificationDto> {
    @Bean
    public Supplier<Flux<Message<NewNotificationDto>>> newNotificationProduce() {
        return sink::asFlux;
    }

}
