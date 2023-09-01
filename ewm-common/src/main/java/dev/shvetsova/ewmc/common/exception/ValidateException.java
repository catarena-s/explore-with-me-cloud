package dev.shvetsova.ewmc.common.exception;

import java.time.LocalDateTime;

public class ValidateException extends RuntimeException implements ApiException {
    private final String reason;

    public ValidateException(String message) {
        super(message);
        reason = "ValidateException";
    }

    public ValidateException(String message, String reason) {
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