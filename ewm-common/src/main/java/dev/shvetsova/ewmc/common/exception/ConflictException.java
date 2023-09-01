package dev.shvetsova.ewmc.common.exception;

import java.time.LocalDateTime;

public class ConflictException extends RuntimeException implements ApiException {
    private final String reason;

    public ConflictException(String message) {
        super(message);
        reason = "Conflict exception";
    }

    public ConflictException(String message, String reason) {
        super(message);
        this.reason = reason;
    }

    @Override
    public ApiError getApiError() {
        return ApiError.builder()
                .message(getMessage())
                .reason(reason)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
