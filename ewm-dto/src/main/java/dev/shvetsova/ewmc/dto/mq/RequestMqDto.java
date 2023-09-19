package dev.shvetsova.ewmc.dto.mq;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class RequestMqDto {
    private List<Long> request;
    private String userId;
    private long eventId;
    private String newStatus;
}
