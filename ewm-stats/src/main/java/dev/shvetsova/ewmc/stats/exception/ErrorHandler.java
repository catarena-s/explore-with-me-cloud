package dev.shvetsova.ewmc.stats.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import dev.shvetsova.ewmc.stats.utils.Constants;
import jakarta.validation.ConstraintViolationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.PropertyValueException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ApiError handleHttpMessageNotReadableException(final HttpMessageNotReadableException e) {
        Throwable cause = e.getCause();
        String msg;
        if (cause instanceof ValueInstantiationException) {
            ValueInstantiationException vie = (ValueInstantiationException) cause;
            msg = vie.getOriginalMessage();
        } else msg = e.getMessage();
        return ApiError.builder()
                .reason("Http message not readable exception")
                .status(BAD_REQUEST.toString())
                .message(msg)
                .timestamp(LocalDateTime.now().format(Constants.FORMATTER))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ApiError handlePropertyValueException(final PropertyValueException e) {
        return ApiError.builder()
                .reason("Property value exception")
                .status(BAD_REQUEST.toString())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now().format(Constants.FORMATTER))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String format = "Field: '%s'. Error: %s.";
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = String.format(format, ((FieldError) error).getRejectedValue(), error.getDefaultMessage());
            errors.put(fieldName, errorMessage);
        });
        return ApiError.builder()
                .reason("Incorrectly made request.")
                .status(BAD_REQUEST.toString())
                .message(errors.toString())
                .timestamp(LocalDateTime.now().format(Constants.FORMATTER))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ApiError handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException e) {
        Throwable cause = e.getCause();
        String msg;
        if (cause instanceof ConversionFailedException) {
            ConversionFailedException cfe = (ConversionFailedException) cause;
            Throwable mostSpecificCause = cfe.getMostSpecificCause();
            msg = mostSpecificCause.getMessage();
        } else msg = e.getMessage();
        return ApiError.builder()
                .reason("Method argument type mismatch")
                .status(BAD_REQUEST.toString())
                .message(msg)
                .timestamp(LocalDateTime.now().format(Constants.FORMATTER))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ApiError handleMissingServletRequestParameterException(final MissingServletRequestParameterException e) {
        return ApiError.builder()
                .reason("Missing servlet request parameter exception")
                .status(BAD_REQUEST.toString())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now().format(Constants.FORMATTER))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataIntegrityViolationException(final DataIntegrityViolationException e) {
        return ApiError.builder()
                .message("Required request parameter")
                .status(BAD_REQUEST.toString())
                .reason(e.getMessage())
                .timestamp(LocalDateTime.now().format(Constants.FORMATTER))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ApiError handleConstraintViolationException(final ConstraintViolationException e) {
        return ApiError.builder()
                .reason("Constraint violation exception")
                .status(BAD_REQUEST.toString())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now().format(Constants.FORMATTER))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    public ApiError handleNotFoundException(final NotFoundException e) {
        return getApiError(e.getApiError(), NOT_FOUND);
    }

    @ExceptionHandler
    @ResponseStatus(CONFLICT)
    public ApiError handleConflictException(final ConflictException e) {
        return getApiError(e.getApiError(), CONFLICT);
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ApiError handleValidateException(final ValidateException e) {
        return getApiError(e.getApiError(), BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ApiError handleResponseException(final ResponseException e) {
        return getApiError(e.getApiError(), BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ApiError handleInvalidFormatException(final InvalidFormatException e) {
        return ApiError.builder()
                .reason("Illegal argument exception")
                .status(BAD_REQUEST.name())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now().format(Constants.FORMATTER))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ApiError handleInternalServerError(final Throwable e) {
        return ApiError.builder()
                .reason("An unexpected error has occurred")
                .status(INTERNAL_SERVER_ERROR.name())
                .message(e.getMessage())
                .errors(Arrays.stream(e.getStackTrace())
                        .map(Object::toString)
                        .collect(Collectors.toList()))
                .timestamp(LocalDateTime.now().format(Constants.FORMATTER))
                .build();
    }

    private ApiError getApiError(ApiError e, HttpStatus conflict) {
        return e.toBuilder()
                .status(conflict.name())
                .build();
    }
}
