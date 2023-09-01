package dev.shvetsova.ewmc.users.exception;

import dev.shvetsova.ewmc.users.utils.Constants;

import java.time.LocalDateTime;

public class ConflictException extends RuntimeException implements ApiException {
    private final String reason;
    private final String timestamp;
    public ConflictException(String message) {
        super(message);
        reason = "Conflict exception";
        timestamp = LocalDateTime.now().format(Constants.FORMATTER);
    }

    public ConflictException(String message, String reason) {
        super(message);
        this.reason = reason;
        timestamp = LocalDateTime.now().format(Constants.FORMATTER);
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
