package dev.shvetsova.ewmc.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {
    @JsonProperty("status")
    private String status;//Код статуса HTTP-ответа
    @JsonProperty("reason")
    private String reason;//Общее описание причины ошибки
    @JsonProperty("message")
    private String message;//Сообщение об ошибке
    @JsonProperty("timestamp")
    private String timestamp;
    @JsonProperty("errors")
    private List<String> errors;//Список стектрейсов или описания ошибок
}
