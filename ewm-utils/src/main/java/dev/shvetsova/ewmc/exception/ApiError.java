package dev.shvetsova.ewmc.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


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

    public ApiError(String status, String reason, String message, String timestamp, List<String> errors) {
        this.status = status;
        this.reason = reason;
        this.message = message;
        this.timestamp = timestamp;
        this.errors = errors;
    }

    public ApiError() {
    }

    public static ApiErrorBuilder builder() {
        return new ApiErrorBuilder();
    }

    public String getStatus() {
        return this.status;
    }

    public String getReason() {
        return this.reason;
    }

    public String getMessage() {
        return this.message;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public List<String> getErrors() {
        return this.errors;
    }

    public ApiErrorBuilder toBuilder() {
        return new ApiErrorBuilder().status(this.status).reason(this.reason).message(this.message).timestamp(this.timestamp).errors(this.errors);
    }

    public static class ApiErrorBuilder {
        private String status;
        private String reason;
        private String message;
        private String timestamp;
        private List<String> errors;

        ApiErrorBuilder() {
        }

        @JsonProperty("status")
        public ApiErrorBuilder status(String status) {
            this.status = status;
            return this;
        }

        @JsonProperty("reason")
        public ApiErrorBuilder reason(String reason) {
            this.reason = reason;
            return this;
        }

        @JsonProperty("message")
        public ApiErrorBuilder message(String message) {
            this.message = message;
            return this;
        }

        @JsonProperty("timestamp")
        public ApiErrorBuilder timestamp(String timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        @JsonProperty("errors")
        public ApiErrorBuilder errors(List<String> errors) {
            this.errors = errors;
            return this;
        }

        public ApiError build() {
            return new ApiError(this.status, this.reason, this.message, this.timestamp, this.errors);
        }

        public String toString() {
            return "ApiError.ApiErrorBuilder(status=" + this.status + ", reason=" + this.reason + ", message=" + this.message + ", timestamp=" + this.timestamp + ", errors=" + this.errors + ")";
        }
    }
}
