package dev.shvetsova.ewmc.common.exception;

import java.time.LocalDateTime;

public class ResponseException extends RuntimeException implements ApiException {
    private final String reason;

    public ResponseException(String message) {
        super(message);
        reason = "ResponseException";
    }

    public ResponseException(String message, String reason) {
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
