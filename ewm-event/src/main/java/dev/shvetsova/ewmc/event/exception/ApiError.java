package dev.shvetsova.ewmc.event.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;


@Getter
@Builder(toBuilder = true)
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {
    @JsonProperty("status")
    private final String status;//Код статуса HTTP-ответа
    @JsonProperty("reason")
    private final String reason;//Общее описание причины ошибки
    @JsonProperty("message")
    private final String message;//Сообщение об ошибке
    @JsonProperty("timestamp")
    private final String timestamp;
    @JsonProperty("errors")
    private final List<String> errors;//Список стектрейсов или описания ошибок
}