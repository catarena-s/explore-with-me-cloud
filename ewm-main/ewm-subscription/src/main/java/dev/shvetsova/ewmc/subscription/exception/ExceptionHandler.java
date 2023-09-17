package dev.shvetsova.ewmc.subscription.exception;

import dev.shvetsova.ewmc.exception.ApiExceptionHandler;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionHandler extends ApiExceptionHandler {
}
