package dev.shvetsova.ewmc.compilation.exception;

import dev.shvetsova.ewmc.exception.ApiExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionHandler extends ApiExceptionHandler {
}
