package dev.shvetsova.ewmc.common.exception;

import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class NotFoundException extends RuntimeException implements ApiException {

    private final String reason;

    public NotFoundException(String massage) {
        super(massage);
        reason = "NotFoundException";
    }

    public NotFoundException(String message, String reason) {
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
