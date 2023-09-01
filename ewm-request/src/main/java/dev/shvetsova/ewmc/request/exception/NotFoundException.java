package dev.shvetsova.ewmc.request.exception;

import dev.shvetsova.ewmc.request.utils.Constants;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class NotFoundException extends RuntimeException implements ApiException {

    private final String reason;
    private String timestamp;

    public NotFoundException(String massage) {
        super(massage);
        reason = "NotFoundException";
    }

    public NotFoundException(String message, String reason) {
        super(message);
        this.reason = reason;
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
                .timestamp(timestamp.isBlank() ? LocalDateTime.now().format(Constants.FORMATTER) : timestamp)
                .build();
    }
}
