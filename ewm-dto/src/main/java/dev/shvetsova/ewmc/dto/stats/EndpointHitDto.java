package dev.shvetsova.ewmc.dto.stats;


import com.fasterxml.jackson.annotation.JsonFormat;
import dev.shvetsova.ewmc.utils.Constants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class EndpointHitDto {
    @NotBlank(message = "App cannot be empty or null")
    private String app;
    @NotBlank(message = "Uri cannot be empty or null")
    private String uri;
    @NotBlank(message = "Ip cannot be empty or null")
    private String ip;
    @NotNull(message = "Timestamp cannot be empty or null")
    @JsonFormat(pattern = Constants.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime timestamp;

    public EndpointHitDto(@NotBlank(message = "App cannot be empty or null") String app, @NotBlank(message = "Uri cannot be empty or null") String uri, @NotBlank(message = "Ip cannot be empty or null") String ip, @NotNull(message = "Timestamp cannot be empty or null") LocalDateTime timestamp) {
        this.app = app;
        this.uri = uri;
        this.ip = ip;
        this.timestamp = timestamp;
    }

    public EndpointHitDto() {
    }

    public static EndpointHitDtoBuilder builder() {
        return new EndpointHitDtoBuilder();
    }

    public @NotBlank(message = "App cannot be empty or null") String getApp() {
        return this.app;
    }

    public @NotBlank(message = "Uri cannot be empty or null") String getUri() {
        return this.uri;
    }

    public @NotBlank(message = "Ip cannot be empty or null") String getIp() {
        return this.ip;
    }

    public @NotNull(message = "Timestamp cannot be empty or null") LocalDateTime getTimestamp() {
        return this.timestamp;
    }

    public void setApp(@NotBlank(message = "App cannot be empty or null") String app) {
        this.app = app;
    }

    public void setUri(@NotBlank(message = "Uri cannot be empty or null") String uri) {
        this.uri = uri;
    }

    public void setIp(@NotBlank(message = "Ip cannot be empty or null") String ip) {
        this.ip = ip;
    }

    @JsonFormat(pattern = Constants.YYYY_MM_DD_HH_MM_SS)
    public void setTimestamp(@NotNull(message = "Timestamp cannot be empty or null") LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public static class EndpointHitDtoBuilder {
        private @NotBlank(message = "App cannot be empty or null") String app;
        private @NotBlank(message = "Uri cannot be empty or null") String uri;
        private @NotBlank(message = "Ip cannot be empty or null") String ip;
        private @NotNull(message = "Timestamp cannot be empty or null") LocalDateTime timestamp;

        EndpointHitDtoBuilder() {
        }

        public EndpointHitDtoBuilder app(@NotBlank(message = "App cannot be empty or null") String app) {
            this.app = app;
            return this;
        }

        public EndpointHitDtoBuilder uri(@NotBlank(message = "Uri cannot be empty or null") String uri) {
            this.uri = uri;
            return this;
        }

        public EndpointHitDtoBuilder ip(@NotBlank(message = "Ip cannot be empty or null") String ip) {
            this.ip = ip;
            return this;
        }

        @JsonFormat(pattern = Constants.YYYY_MM_DD_HH_MM_SS)
        public EndpointHitDtoBuilder timestamp(@NotNull(message = "Timestamp cannot be empty or null") LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public EndpointHitDto build() {
            return new EndpointHitDto(this.app, this.uri, this.ip, this.timestamp);
        }
    }
}