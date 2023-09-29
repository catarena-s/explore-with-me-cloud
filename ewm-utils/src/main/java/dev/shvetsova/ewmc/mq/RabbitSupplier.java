package dev.shvetsova.ewmc.mq;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import reactor.core.publisher.Sinks;
import reactor.util.concurrent.Queues;

public abstract class RabbitSupplier<T> {
    protected final Sinks.Many<Message<T>> sink = Sinks.many().multicast()
            .onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false);

    public void sendMessageToQueue(T obj) {
        sink.emitNext(MessageBuilder.withPayload(obj).build(), Sinks.EmitFailureHandler.FAIL_FAST);
    }
}
