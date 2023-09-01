package dev.shvetsova.ewmc.common.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static dev.shvetsova.ewmc.common.Constants.YYYY_MM_DD_HH_MM_SS;


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
    @JsonFormat(pattern = YYYY_MM_DD_HH_MM_SS)
    private final LocalDateTime timestamp;
    @JsonProperty("errors")
    private final List<String> errors;//Список стектрейсов или описания ошибок
}
