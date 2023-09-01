package dev.shvetsova.ewmc.users.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class FeignExceptionHandler implements ErrorDecoder {

    // вызывается каждый раз при ошибке вызова через Feign
    @Override
    public Exception decode(String methodKey, Response response) {
        HttpStatus responseStatus = HttpStatus.valueOf(response.status());
        ApiError message;
        switch (responseStatus) {
            case NOT_FOUND -> {
                message = getMessage(response.body());
                return new NotFoundException(message.getMessage(), message.getReason(), message.getTimestamp());
            }
            case CONFLICT -> {
                message = getMessage(response.body());
                return new ConflictException(message.getMessage(), message.getReason(), message.getTimestamp());
            }
            case SERVICE_UNAVAILABLE -> {
                return new FallbackException("Service unavailable:" + methodKey);
            }
            default -> {
                return new ResponseException(response.body().toString());
            }
        }
    }

    private ApiError getMessage(Response.Body body) {
        ApiError message;
        try (InputStream bodyIs = body.asInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            message = mapper.readValue(bodyIs, ApiError.class);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return message;
    }
}

