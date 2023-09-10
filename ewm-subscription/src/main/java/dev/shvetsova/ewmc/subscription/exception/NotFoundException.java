package dev.shvetsova.ewmc.subscription.exception;

import dev.shvetsova.ewmc.subscription.utils.Constants;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class NotFoundException extends RuntimeException implements ApiException {

    private final String reason;
    private final String timestamp;

    public NotFoundException(String massage) {
        super(massage);
        reason = "NotFoundException";
        timestamp = LocalDateTime.now().format(Constants.FORMATTER);
    }

    public NotFoundException(String message, String reason) {
        super(message);
        this.reason = reason;
        timestamp = LocalDateTime.now().format(Constants.FORMATTER);
    }

    public NotFoundException(String message, String reason, String timestamp) {
        super(message);
        this.reason = reason;
        this.timestamp = timestamp;
    }

    @Override
    public ApiError getApiError() {
        return ApiError.builder()
                .message(getMessage())
                .reason(reason)
                .timestamp(timestamp)
                .build();
    }
}
