package dev.shvetsova.compilation.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.shvetsova.ewmc.exception.*;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class FeignExceptionHandler extends ApiFeignExceptionHandler {

}

