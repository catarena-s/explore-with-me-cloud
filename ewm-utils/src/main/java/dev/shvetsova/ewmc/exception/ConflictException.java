package dev.shvetsova.ewmc.exception;


import java.time.LocalDateTime;

import static dev.shvetsova.ewmc.utils.Constants.FORMATTER;

public class ConflictException extends RuntimeException implements ApiException {
    private final String reason;
    private final String timestamp;

    public ConflictException(String message) {
        super(message);
        reason = "Conflict exception";
        timestamp = LocalDateTime.now().format(FORMATTER);
    }

    public ConflictException(String message, String reason) {
        super(message);
        this.reason = reason;
        timestamp = LocalDateTime.now().format(FORMATTER);
    }

    public ConflictException(String message, String reason, String timestamp) {
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
