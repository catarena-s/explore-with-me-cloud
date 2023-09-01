package dev.shvetsova.ewmc.users.exception;

import lombok.Getter;


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
                .timestamp(timestamp)
                .build();
    }
}