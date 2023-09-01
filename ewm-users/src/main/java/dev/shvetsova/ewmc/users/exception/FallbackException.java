package dev.shvetsova.ewmc.users.exception;

import dev.shvetsova.ewmc.users.utils.Constants;

import java.time.LocalDateTime;

public class FallbackException extends RuntimeException implements ApiException {
    private String reason="Fallback Exception";
    private final String timestamp;

    public FallbackException(String message) {
        super(message);
        timestamp = LocalDateTime.now().format(Constants.FORMATTER);
    }

    public FallbackException(String message, String reason) {
        super(message);
        this.reason = reason;
        timestamp = LocalDateTime.now().format(Constants.FORMATTER);
    }

    public FallbackException(String message, String reason, String timestamp) {
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
