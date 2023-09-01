package dev.shvetsova.ewmc.subscription.exception;

import dev.shvetsova.ewmc.subscription.utils.Constants;

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
                .timestamp(LocalDateTime.now().format(Constants.FORMATTER))
                .build();
    }
}
