package dev.shvetsova.ewmc.exception;


import dev.shvetsova.ewmc.utils.Constants;

import java.time.LocalDateTime;

public class FallbackException extends RuntimeException implements ApiException {
    private final String reason;
    private final String timestamp;

    public FallbackException(String message) {
        super(message);
        timestamp = LocalDateTime.now().format(Constants.FORMATTER);
        this.reason = "Fallback Exception";
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
